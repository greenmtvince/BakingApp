package com.quantrian.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements OnStepClickListener{
    private int currentStep;
    private ArrayList<Step> steps;
    private Boolean mTwoPane;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentStep=0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Bundle bundle = getIntent().getExtras();
        steps = bundle.getParcelableArrayList("STEPS");

        recipeName = getIntent().getStringExtra("RECIPE");

        MasterListFragment fraggy = new MasterListFragment();
        fraggy.setArguments(bundle);
        FragmentManager fragmentManager1 = getSupportFragmentManager();

        fragmentManager1.beginTransaction()
                .replace(R.id.recipe_detail_frame, fraggy)
                .commit();

        if (findViewById(R.id.recipe_detail_linear_layout)!=null){
            mTwoPane=true;
            updateStepDetailPane();
        } else {
            mTwoPane=false;
        }

    }

    public void onStepSelected(int position){
        Toast.makeText(this, "Position Clicked = "+position, Toast.LENGTH_SHORT).show();
        currentStep=position;
        if(mTwoPane){
            updateStepDetailPane();
        } else {
            launchStepDetailActivity();
        }
    }

    private void launchStepDetailActivity() {
        Intent i=new Intent(this,RecipeStepDetailActivity.class);
                i.putExtra("STEP",steps.get(currentStep));
                i.putExtra("STEPS", steps);
                i.putExtra("POSITION",currentStep);
                i.putExtra("RECIPE",recipeName);
                startActivity(i);
    }

    public void updateStepDetailPane(){
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        Bundle b = new Bundle();

        b.putParcelable("current_step",steps.get(currentStep));

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
        stepDetailFragment.setArguments(b);
        stepDetailFragment.newInstance(steps.get(currentStep));
        fragmentManager2.beginTransaction()
                .replace(R.id.landscape_step_detail, stepDetailFragment)
                .commit();
    }

}
