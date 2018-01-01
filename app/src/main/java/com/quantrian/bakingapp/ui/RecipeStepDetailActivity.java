package com.quantrian.bakingapp.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private StepPagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);
        Step step= getIntent().getParcelableExtra("STEP");
        mSteps=getIntent().getParcelableArrayListExtra("STEPS");
        //((TextView) findViewById(R.id.bananas_textview)).setText(step.description);

        mPagerAdapter = new StepPagerAdapter(getFragmentManager());
        mPager = (ViewPager) findViewById(R.id.recipe_step_detail_pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(getIntent().getIntExtra("POSITION",0));
        mPager.setOffscreenPageLimit(1);

    }

    private class StepPagerAdapter extends FragmentStatePagerAdapter{

        public StepPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return RecipeStepDetailFragment.newInstance(mSteps.get(position));
        }

        @Override
        public int getCount() {
            return mSteps.size();
        }
    }
}
