package com.quantrian.bakingapp.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.models.Step;
import static butterknife.ButterKnife.findById;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * //
 * to handle interaction events.
 * Use the {@link RecipeStepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CURRENT_STEP = "current_step";
    private static final String CURRENT_POSITION = "current_position";
    private static final String TAG = "FRAGO";

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mPlayerCurrentPosition;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step1 Parameter 1.
     * @return A new instance of fragment RecipeStepDetailFragment.
     */
    public static RecipeStepDetailFragment newInstance(Step step1) {
        Log.d(TAG, "newInstance: ");
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_STEP, step1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(CURRENT_STEP);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);



        //if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
        ((TextView) findById(view, R.id.recipe_step_detail_fragment_instruction_text))
                    .setText(mStep.description);
        //}
        mPlayerView = findById(view, R.id.playerView);

        initializeMediaSession();

        if(!mStep.videoURL.isEmpty())
            initializePlayer(Uri.parse(mStep.videoURL));
        else
            mPlayerView.setVisibility(View.GONE);



        return view;
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS|
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder =  new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY|
                                PlaybackStateCompat.ACTION_PAUSE|
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS|
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        if (mStep.videoURL!=null)
            initializePlayer(Uri.parse(mStep.videoURL));
    }

    private void initializePlayer(Uri mediaUri){
        Log.d(TAG, "initializePlayer: ");
        if (mExoPlayer== null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),trackSelector,loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), "User-Agent:QuantrianBakingApp"),
                    new DefaultExtractorsFactory(), null,null);
            if (mPlayerCurrentPosition!= C.TIME_UNSET)
                mExoPlayer.seekTo(mPlayerCurrentPosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void showNotification(PlaybackStateCompat state){

        Log.d(TAG, "showNotification: ");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),"myApp");
        int icon;
        String play_pause;

        if (state.getState()== PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = "Pause";
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = "Play";
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, "Restart",
                MediaButtonReceiver.buildMediaButtonPendingIntent( getActivity(),
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getActivity(), 0, new Intent(getActivity(), RecipeStepDetailFragment.class), 0);

        builder.setContentTitle(getString(R.string.app_name))
                .setContentText(mStep.shortDescription)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_cake_black_24dp)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle((new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1)));

        mNotificationManager = (NotificationManager) getActivity()
                .getSystemService(getActivity().NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,builder.build());

    }

    private void releasePlayer(){
        Log.d(TAG, "releasePlayer: ");
        mNotificationManager.cancelAll();
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");

        if(mExoPlayer!=null) {
            mPlayerCurrentPosition = getCurrentPlayerPosition();
            releasePlayer();
        }
        super.onPause();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    private long getCurrentPlayerPosition() {
        return mExoPlayer.getCurrentPosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outstate);
        outstate.putParcelable(CURRENT_STEP, mStep);
        outstate.putLong(CURRENT_POSITION, mPlayerCurrentPosition);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            mPlayerCurrentPosition=savedInstanceState.getLong(CURRENT_STEP);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            Log.d(TAG, "onPlayerStateChanged: PLAYING");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)){
            Log.d(TAG, "onPlayerStateChanged: PAUSED");
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());

        showNotification(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay (){
            mExoPlayer.setPlayWhenReady(true);
        }
        @Override
        public void onPause (){
            mExoPlayer.setPlayWhenReady(false);
        }
        @Override
        public void onSkipToPrevious(){
            mExoPlayer.seekTo(0);
        }
    }

    public static class MediaReciever extends BroadcastReceiver{

        public MediaReciever(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
