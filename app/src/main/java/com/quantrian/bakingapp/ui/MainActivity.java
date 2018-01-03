package com.quantrian.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.quantrian.bakingapp.Adapters.RecipeCardAdapter;
import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.FetchNetworkRecipes;
import com.quantrian.bakingapp.utils.TaskCompleteListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<Recipe> mRecipes;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.main_recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.main_swipe_refresh_layout);

        mContext = this;

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipeData(mContext);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout_main)).setTitle("Baking Time");
        loadRecipeData(this);
    }

    //Checks to see if we're connected to the internet and if so calls the AsyncTask
    //Otherwise displays a Toast that the app isn't connected.
    //TODO Implement a broadcast reciever to detect connectivity status changes
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
                startActivity(i);
            }
        });
        mRecyclerView.setAdapter(recipeCardAdapter);
    }

    public class FetchRecipeTaskCompleteListener implements TaskCompleteListener<ArrayList<Recipe>> {
        @Override
        public void onTaskComplete(ArrayList<Recipe> result){
            mRecipes = result;
            setAdapter();
        }
    }
}
