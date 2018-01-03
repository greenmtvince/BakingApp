package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

import com.quantrian.bakingapp.Adapters.RecipeCardAdapter;
import com.quantrian.bakingapp.Adapters.RecipeDetailAdapter;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Ingredient;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;


/**
 *
 */
public class MasterListFragment extends Fragment {

    OnStepClickListener mCallback;

    private String TAG = "FRAGLOG";
    private RecyclerView mRecyclerView;

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

    private String getRecipeIngredients(ArrayList<Ingredient> ingredientList){
        StringBuilder ingredientsText= new StringBuilder();
        for (Ingredient ingredient : ingredientList){
            ingredientsText.append(prettyNumbers(ingredient.quantity))
                    .append(" ")
                    .append(ingredient.measure.toLowerCase())
                    .append("   ")
                    .append(ingredient.ingredient)
                    .append(System.lineSeparator());
        }
        return ingredientsText.toString();
    }

    private String prettyNumbers(float f){
        String prettyNumber;
        if (f == (long) f){
            prettyNumber=String.format("%d",(long) f);
        } else {
            prettyNumber=String.format("%s",f);
        }

        String[] intermediateStr = prettyNumber.split("\\.");

        Log.d(TAG, "prettyNumbers: "+ prettyNumber);

        if(intermediateStr.length>1) {
            Log.d(TAG, "prettyNumbers: " +intermediateStr[0]);
            switch (intermediateStr[1]) {
                case "25":
                    prettyNumber = intermediateStr[0] + " ¼";
                    break;
                case "5":
                    prettyNumber = intermediateStr[0] + " ½";
                    break;
                case ".75":
                    prettyNumber = intermediateStr[0] + " ¾";
                    break;
                default:
                    break;
            }
            //Handles the case of something like 0.5
            if(intermediateStr[0].equals("0")){
                prettyNumber=prettyNumber.replace("0 ","");
            }
        }

        return prettyNumber;
    }
}
