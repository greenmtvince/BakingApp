package com.quantrian.bakingapp.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.quantrian.bakingapp.models.Recipe;

import java.util.ArrayList;

/**
 * Created by Vinnie on 12/31/2017.
 */

public class FetchNetworkRecipes extends AsyncTask<Void,Void,ArrayList<Recipe>> {
    private TaskCompleteListener<ArrayList<Recipe>> mListener;

    public FetchNetworkRecipes(TaskCompleteListener<ArrayList<Recipe>> listener){
        mListener=listener;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(Void... voids) {
        try{
            return NetworkUtilities.fetchJsonArray();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(final ArrayList<Recipe> recipeArrayList){
        if (recipeArrayList!=null){
            super.onPostExecute(recipeArrayList);
            mListener.onTaskComplete(recipeArrayList);
        }
    }
}
