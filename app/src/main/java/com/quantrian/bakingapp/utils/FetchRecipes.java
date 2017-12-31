package com.quantrian.bakingapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.quantrian.bakingapp.data.ContractRecipe;
import com.quantrian.bakingapp.data.ContractRecipe.RecipeEntry;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Vinnie on 12/30/2017.
 */

public class FetchRecipes extends AsyncTask<Void, Void, Cursor> {
    private Context mContext;
    private TaskCompleteListener<ArrayList<Recipe>> mListener;

    //private ArrayList<Step> mSteps;

    public FetchRecipes(Context context, TaskCompleteListener listener){
        mContext=context;
        mListener=listener;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try{
            return mContext.getContentResolver().query(RecipeEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    RecipeEntry.COLUMN_ID);
        } catch (Exception e){
            Log.e(TAG, "Failed to Asynchronously Load Data.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute (Cursor result){
        if (mListener !=null){
            super.onPostExecute(result);
            mListener.onTaskComplete(getAllRecipes(result));
        }
    }

    private ArrayList<Recipe> getAllRecipes(Cursor result) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        if (result.moveToFirst()){
            while (!result.isAfterLast()){
                String name = result.getString(result.getColumnIndex(RecipeEntry.COLUMN_NAME));
                String image = result.getString(result.getColumnIndex(RecipeEntry.COLUMN_IMAGE));
                int id = result.getInt(result.getColumnIndex(RecipeEntry.COLUMN_ID));
                int servings = result.getInt(result.getColumnIndex(RecipeEntry.COLUMN_SERVINGS));

                //FetchSteps task = new FetchSteps(mContext,new FetchStepCompleteListener()).execute();

                Recipe r = new Recipe();
                //TODO Add calls to fetchSteps and fetchIngredients
                recipes.add(r);
                result.moveToNext();
            }
        }
        return recipes;
    }

    /*private class FetchStepCompleteListener implements TaskCompleteListener<ArrayList<Step>>{

        @Override
        public void onTaskComplete(ArrayList<Step> result) {
            mSteps = result;
        }
    }*/
}
