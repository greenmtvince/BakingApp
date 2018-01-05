package com.quantrian.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.dx.command.Main;
import com.quantrian.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Vinnie on 1/4/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TestMainActivityScreen {
    public static final String RECIPE_NAME= "Nutella Pie";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    //Test that when we click an item from the RecyclerView in the Main activity that the
    //RecipeDetailActivity is launched with the appropriate fragment.
    @Test
    public void clickGridviewItem_OpensRecipeDetailActivity(){

        onView(withId(R.id.main_recycler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(allOf(instanceOf(TextView.class),withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void swipeRefreshLayout_PerformsRefresh(){

        onView(withId(R.id.main_recycler_view)).check(matches(isDisplayed()));

        onView(withId(R.id.main_swipe_refresh_layout))
                .perform(swipeDown());

        onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(allOf(instanceOf(TextView.class),withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(RECIPE_NAME)));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }


}
