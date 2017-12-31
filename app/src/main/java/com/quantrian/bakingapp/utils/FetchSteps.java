package com.quantrian.bakingapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.quantrian.bakingapp.data.ContractRecipe;
import com.quantrian.bakingapp.data.ContractStep;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.models.Step;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Vinnie on 12/30/2017.
 */

public class FetchSteps extends AsyncTask<Void, Void, Cursor> {
    private Context mContext;
    private TaskCompleteListener<ArrayList<Step>> mListener;

    public FetchSteps(Context context, TaskCompleteListener fetchStepCompleteListener) {
        mContext = context;
        mListener=fetchStepCompleteListener;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try{
            return mContext.getContentResolver().query(ContractRecipe.RecipeEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContractRecipe.RecipeEntry.COLUMN_ID);
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
            mListener.onTaskComplete(getAllSteps(result));
        }
    }

    private ArrayList<Step> getAllSteps(Cursor result) {
        ArrayList<Step> steps = new ArrayList<>();

        if (result.moveToFirst()){
            while (!result.isAfterLast()){
                String video = result.getString(result.getColumnIndex(ContractStep.StepEntry.COLUMN_VIDEO_URL));
                String thumb = result.getString(result.getColumnIndex(ContractStep.StepEntry.COLUMN_THUMBNAIL_URL));
                String desc = result.getString(result.getColumnIndex(ContractStep.StepEntry.COLUMN_DESCRIPTION));
                String shtdesc = result.getString(result.getColumnIndex(ContractStep.StepEntry.COLUMN_SHORT_DESCRIPTION));
                int id = result.getInt(result.getColumnIndex(ContractStep.StepEntry.COLUMN_ID));

                Step s = new Step();
                steps.add(s);
                result.moveToNext();
            }
        }
        return steps;
    }
}
