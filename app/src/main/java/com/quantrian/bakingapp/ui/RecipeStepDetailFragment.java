package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.models.Step;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * //
 * to handle interaction events.
 * Use the {@link RecipeStepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURRENT_STEP = "current_step";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EXODEBUG";

    private Step mStep;
    private String mParam2;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mPlayerCurrentPosition;

    //private OnFragmentInteractionListener mListener;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step1 Parameter 1.
     * //@param param2 Parameter 2.
     * @return A new instance of fragment RecipeStepDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeStepDetailFragment newInstance(Step step1) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_STEP, step1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(CURRENT_STEP);
            //mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            ((TextView) view.findViewById(R.id.recipe_step_detail_fragment_instruction_text))
                    .setText(mStep.description);
        }
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        if(!mStep.videoURL.equals(""))
            initializePlayer(Uri.parse(mStep.videoURL));
        else
            mPlayerView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();


    }

    private void initializePlayer(Uri mediaUri){
        if (mExoPlayer== null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),trackSelector,loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), "User-Agent:QuantrianBakingApp"),
                    new DefaultExtractorsFactory(), null,null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer(){
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        releasePlayer();
        if(mExoPlayer!=null) {
            mPlayerCurrentPosition = getCurrentPlayerPosition();
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
        }
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releasePlayer();
    }

    private long getCurrentPlayerPosition() {
        return mExoPlayer.getCurrentPosition();
    }

    private void resumePlayback(){
        mExoPlayer.seekTo(mPlayerCurrentPosition);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        //Log.d(TAG, "setUserVisibleHint: Becoming Visible");
        if (this.isVisible())
        {
            if (!isVisibleToUser)
            {
                if (mExoPlayer!=null)
                    mExoPlayer.setPlayWhenReady(false);
            }
            if(isVisibleToUser)
            {
                if (mExoPlayer!=null)
                    mExoPlayer.setPlayWhenReady(true);
                /*else {
                    if(!mStep.videoURL.equals(""))
                        initializePlayer(Uri.parse(mStep.videoURL));
                    else
                        mPlayerView.setVisibility(View.GONE);
                }*/
            }
        }
    }

}
