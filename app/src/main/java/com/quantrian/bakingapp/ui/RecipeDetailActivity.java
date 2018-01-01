package com.quantrian.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quantrian.bakingapp.R;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();

        /*String debugStr ="EMPTY AS YOUR SOUL";
        try {
            debugStr = getIntent().getExtras().getString("RECIPE");
        } catch (Exception e)
        {
            Log.d("BUGGY", "onCreate: "+debugStr +" | "+e.getMessage());
        }

        //Log.d("BUGGY", "onCreate: "+debugStr );



        Bundle bundle = new Bundle();
        bundle.putString("RECIPE", debugStr);*/
        RecipeDetailFragment fraggy = new RecipeDetailFragment();
        fraggy.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_frame, fraggy)
                .commit();

    }
}
