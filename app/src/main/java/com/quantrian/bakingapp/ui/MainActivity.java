package com.quantrian.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.quantrian.bakingapp.adapters.RecipeCardAdapter;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.widget.RecipeWidgetProvider;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.FetchNetworkRecipes;
import com.quantrian.bakingapp.utils.NetworkUtilities;
import com.quantrian.bakingapp.utils.TaskCompleteListener;

import java.util.ArrayList;
/*
*   IGNORE FOR PROJECT EVALUATION
*
*Originally I'd intended to use a SQL Database to store data connected through a content provider
* SQL is my goto.  As I got further into the project I realized it was complete overkill for what I
* needed to do and there were better methods for the data at hand.
*
* Still this is working and tested and rather than delete it entirely I figured I'd keep it on hand
* here to use as a boilerplate for future code.
*
* FORK CODE AND DELETE
 */
public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<Recipe> mRecipes;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.main_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);

        mContext = this;

        mSharedPref = mContext.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipeData(mContext);
            }
        });

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(
                getResources().getInteger(R.integer.list_column_count),StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_main)).setTitle("Baking Time");
        loadRecipeData(this);
    }

    //Checks to see if we're connected to the internet and if so calls the AsyncTask
    //Otherwise displays a Toast that the app isn't connected.
    //
    //https://www.androidhive.info/2012/07/android-detect-internet-connection-status/
    private void loadRecipeData(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            new FetchNetworkRecipes(new FetchRecipeTaskCompleteListener()).execute();
        } else {
            Toast.makeText(this, "Not Connected to the internet",
                    Toast.LENGTH_LONG).show();
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setAdapter(){
        RecipeCardAdapter recipeCardAdapter = new RecipeCardAdapter(getApplicationContext(),mRecipes);
        recipeCardAdapter.setOnItemClickListener(new RecipeCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recipe recipe = mRecipes.get(position);
                Intent i=new Intent(getApplicationContext(),RecipeDetailActivity.class);
                i.putExtra("RECIPE",recipe.name);
                i.putExtra("INGREDIENTS",recipe.ingredients);
                i.putExtra("STEPS", recipe.steps);

                mSharedPref.edit().putString(getString(R.string.selected_recipe),
                        Integer.toString(position)).apply();

                Intent widgetIntent= new Intent(getApplicationContext(), RecipeWidgetProvider.class);
                widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[]ids = AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(new ComponentName(getApplication(), RecipeWidgetProvider.class));
                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(widgetIntent);

                startActivity(i);


            }
        });
        mRecyclerView.setAdapter(recipeCardAdapter);
    }

    public class FetchRecipeTaskCompleteListener implements TaskCompleteListener<ArrayList<Recipe>> {
        @Override
        public void onTaskComplete(ArrayList<Recipe> result){
            mRecipes = result;
            writeToSharedPreferences();
            setAdapter();
        }
    }

    public void writeToSharedPreferences(){
        mSharedPref.edit().putString(getString(R.string.json_array), NetworkUtilities.convertToString(mRecipes)).apply();
    }
}
