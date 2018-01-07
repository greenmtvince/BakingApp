package com.quantrian.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements OnStepClickListener{
    private static final String DETAIL_FRAGMENT_TAG = "step_detail_fragment_tag";
    private static final String LIST_FRAGMENT_TAG = "list_fragment_tag";
    private int currentStep;
    private ArrayList<Step> steps;
    private Boolean mTwoPane;
    private String recipeName;
    private MasterListFragment mMasterListFragment;
    private RecipeStepDetailFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentStep=0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        steps = bundle.getParcelableArrayList("STEPS");

        recipeName = getIntent().getStringExtra("RECIPE");

        MasterListFragment fraggy = new MasterListFragment();
        fraggy.setArguments(bundle);
        FragmentManager fragmentManager1 = getSupportFragmentManager();

        mMasterListFragment = (MasterListFragment) fragmentManager1
                .findFragmentByTag(LIST_FRAGMENT_TAG);

        if(mMasterListFragment==null) {
            fragmentManager1.beginTransaction()
                    .replace(R.id.recipe_detail_frame, fraggy, LIST_FRAGMENT_TAG)
                    .commit();
        }

        if (findViewById(R.id.recipe_detail_linear_layout)!=null){
            mTwoPane=true;
            updateStepDetailPane(false);
        } else {
            mTwoPane=false;
        }

    }

    public void onStepSelected(int position){
        Toast.makeText(this, "Position Clicked = "+position, Toast.LENGTH_SHORT).show();
        currentStep=position;
        if(mTwoPane){
            updateStepDetailPane(true);
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

    public void updateStepDetailPane(boolean selected){
        FragmentManager fragmentManager2 = getSupportFragmentManager();


        mRetainedFragment = (RecipeStepDetailFragment) fragmentManager2
                .findFragmentByTag(DETAIL_FRAGMENT_TAG);

        if(mRetainedFragment == null||selected){
            Bundle b = new Bundle();
            b.putParcelable("current_step",steps.get(currentStep));

            mRetainedFragment = RecipeStepDetailFragment.newInstance(steps.get(currentStep));
            mRetainedFragment.setArguments(b);
            fragmentManager2.beginTransaction()
                    .replace(R.id.landscape_step_detail, mRetainedFragment,DETAIL_FRAGMENT_TAG)
                    .commit();
        }

        /*

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
        stepDetailFragment.setArguments(b);
        stepDetailFragment.newInstance(steps.get(currentStep));
        fragmentManager2.beginTransaction()
                .replace(R.id.landscape_step_detail, stepDetailFragment)
                .commit();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void  onPause(){
        super.onPause();
        if (isFinishing()&&mRetainedFragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mRetainedFragment).commit();
        }
        if (isFinishing()&&mMasterListFragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mMasterListFragment).commit();
        }
    }

}
