package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.quantrian.bakingapp.adapters.RecipeCardAdapter;
import com.quantrian.bakingapp.adapters.RecipeDetailAdapter;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Ingredient;
import com.quantrian.bakingapp.models.Step;
import com.quantrian.bakingapp.utils.PrettyStrings;

import java.util.ArrayList;


/**
 *
 */
public class MasterListFragment extends Fragment {

    private static final String SAVED_LAYOUT_MANAGER = "layout_manager";
    OnStepClickListener mCallback;

    private static final String TAG = "FRAGLOG";
    private RecyclerView mRecyclerView;
    private Parcelable mLayoutManagerState;

    public MasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start 0");
        // Inflate the layout for this fragment
        String toDisplay = "Oh no he didn't";
        final ArrayList<Step> mSteps;
        final Context context = getActivity();
        final ArrayList<Ingredient> mIngredients;

        toDisplay=this.getArguments().getString("RECIPE");
        getActivity().setTitle(toDisplay);

        mSteps=this.getArguments().getParcelableArrayList("STEPS");
        mIngredients = this.getArguments().getParcelableArrayList("INGREDIENTS");

        View rootView= inflater.inflate(R.layout.fragment_master_list, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();


        float density = getResources().getDisplayMetrics().density;
        int width = displayMetrics.widthPixels ;
        if(   rootView.findViewById(R.id.master_list_scrollview) !=null
            && (orientation== Surface.ROTATION_90 || orientation==Surface.ROTATION_270)){
            Log.d(TAG, "Screen Rotated.  Width is "+ width);

            LinearLayout.LayoutParams widthParams = new LinearLayout
                    .LayoutParams(Math.round(width*3/8),LinearLayout.LayoutParams.MATCH_PARENT);
            ((ScrollView) rootView.findViewById(R.id.master_list_scrollview))
                    .setLayoutParams(widthParams);
        }

        mRecyclerView = rootView.findViewById(R.id.master_list_fragment_listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        RecipeDetailAdapter recipeDetailAdapter = new RecipeDetailAdapter(context, mSteps);
        recipeDetailAdapter.setOnItemClickListener(new RecipeCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(context, "You clicked item at position "+ position, Toast.LENGTH_SHORT).show();
                mCallback.onStepSelected(position);

            }
        });
        mRecyclerView.setAdapter(recipeDetailAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext()
        ,gridLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        final TextView textylicious = rootView.findViewById(R.id.master_list_fragment_texty);
        textylicious.setText(getRecipeIngredients(mIngredients));
        return rootView;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepClick Listener");
        }
    }

    private static String getRecipeIngredients(ArrayList<Ingredient> ingredientList){
        StringBuilder ingredientsText= new StringBuilder();
        for (Ingredient ingredient : ingredientList){
            ingredientsText.append(PrettyStrings.ingredientToString(ingredient))
                    .append(System.lineSeparator());
        }
        return ingredientsText.toString();
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outstate);
            outstate.putParcelable(SAVED_LAYOUT_MANAGER,
                    mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null){
            mLayoutManagerState=savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLayoutManagerState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerState);
        }
    }

}
