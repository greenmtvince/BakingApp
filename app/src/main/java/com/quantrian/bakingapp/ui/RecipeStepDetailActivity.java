package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "step_detail_fragment_tag";
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Step> mSteps;
    private int mPosition;
    private RecipeStepDetailFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Update the Action Bar Title
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        String recipeTitle = getIntent().getStringExtra("RECIPE");
        if (!recipeTitle.equals("NOVALUE"))
            this.setTitle(recipeTitle);

        //Show Only the media player in Landscape
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();
        if(orientation== Surface.ROTATION_90 || orientation==Surface.ROTATION_270) {
            Log.d("ROTATE", "Screen Rotated.");
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            actionBar.hide();
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        mSteps = getIntent().getParcelableArrayListExtra("STEPS");

        if (savedInstanceState !=null){
            mPosition = savedInstanceState.getInt("current_step");
        } else {//Saved Instance State == null
            mPosition = getIntent().getIntExtra("POSITION", 0);

        }
        updateView(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_left:
                                mPosition--;
                                updateView(true);
                                break;
                            case R.id.action_right:
                                mPosition++;
                                updateView(true);
                                break;
                        }
                        return true;
                    }
                });
    }

    private void updateView(boolean btnPress) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("current_step", mSteps.get(mPosition));

        if (mPosition == 0)
            bottomNavigationView.findViewById(R.id.action_left).setEnabled(false);
        if (mPosition == mSteps.size() - 1)
            bottomNavigationView.findViewById(R.id.action_right).setEnabled(false);

        FragmentManager fm = getSupportFragmentManager();

        mRetainedFragment = (RecipeStepDetailFragment) fm.findFragmentByTag(FRAGMENT_TAG);

        if (mRetainedFragment == null||btnPress){
            mRetainedFragment = new RecipeStepDetailFragment();
            mRetainedFragment.setArguments(bundle);
            fm.beginTransaction()
                    .replace(R.id.recipe_step_detail_frame, mRetainedFragment, FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt("current_step",mPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state){
        if (state !=null){
            mPosition = state.getInt("current_step");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void  onPause(){
        super.onPause();
        if (isFinishing()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(mRetainedFragment).commit();
        }
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

}