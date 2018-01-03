package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
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
    private BottomNavigationView bottomNavigationView;
    private ArrayList<Step> mSteps;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        String recipeTitle = getIntent().getStringExtra("RECIPE");
        if (!recipeTitle.equals("NOVALUE"))
            this.setTitle(recipeTitle);

        ActionBar actionBar=getSupportActionBar();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

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
        mPosition = getIntent().getIntExtra("POSITION", 0);


        updateView();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_left:
                                mPosition--;
                                updateView();
                                break;
                            case R.id.action_right:
                                mPosition++;
                                updateView();
                                break;
                        }
                        return true;
                    }
                });
    }

    private void updateView() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("current_step", mSteps.get(mPosition));

        if (mPosition == 0)
            bottomNavigationView.findViewById(R.id.action_left).setEnabled(false);
        if (mPosition == mSteps.size() - 1)
            bottomNavigationView.findViewById(R.id.action_right).setEnabled(false);
        RecipeStepDetailFragment masterListFragment = new RecipeStepDetailFragment();
        masterListFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_detail_frame, masterListFragment)
                .commit();
    }
}