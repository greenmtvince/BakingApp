package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quantrian.bakingapp.Adapters.RecipeCardAdapter;
import com.quantrian.bakingapp.Adapters.RecipeDetailAdapter;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Step;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 *
 */
public class RecipeDetailFragment extends Fragment {
    private RecyclerView mRecyclerView;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String toDisplay = "Oh no he didn't";
        final ArrayList<Step> mSteps;
        final Context context = getActivity();

        toDisplay=this.getArguments().getString("RECIPE");
        mSteps=this.getArguments().getParcelableArrayList("STEPS");

        View rootView= inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        mRecyclerView = rootView.findViewById(R.id.recipe_detail_fragment_listview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        RecipeDetailAdapter recipeDetailAdapter = new RecipeDetailAdapter(context, mSteps);
        recipeDetailAdapter.setOnItemClickListener(new RecipeCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Toast.makeText(context, "You clicked item at position "+ position, Toast.LENGTH_SHORT).show();

                Intent i=new Intent(context,RecipeStepDetailActivity.class);
                i.putExtra("STEP",mSteps.get(position));
                i.putExtra("STEPS", mSteps);
                i.putExtra("POSITION",position);
                startActivity(i);

            }
        });
        mRecyclerView.setAdapter(recipeDetailAdapter);

        final TextView textylicious = rootView.findViewById(R.id.recipe_detail_fragment_texty);
        textylicious.setText(toDisplay + " Number of steps: "+ mSteps.size());
        return rootView;

    }

}
