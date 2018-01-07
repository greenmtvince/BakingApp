package com.quantrian.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.ui.MainActivity;
import com.quantrian.bakingapp.utils.JsonUtilities;
import com.quantrian.bakingapp.utils.NetworkUtilities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();
    private static Recipe mRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        setRemoteAdapter(context,views);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);





        getRecipeFromPrefs(context);

        widgetText=mRecipe.name;


        views.setTextViewText(R.id.widget_header, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setRemoteAdapter(Context context, RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,new Intent(context,WidgetService.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void getRecipeFromPrefs(Context context){
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        int position = Integer.parseInt(preferences.
                getString(context.getString(R.string.selected_recipe),"0"));
        Log.d("WIDGETOR", "getRecipeFromPrefs: "+position);

        String json_array = preferences.getString(context.getString(R.string.json_array),null);
        //Recipe recipe =new Recipe();
        try {
            Recipe[] list = JsonUtilities.deSerialize(json_array, Recipe[].class);
            mRecipe= new ArrayList<>(Arrays.asList(list)).get(position);
        } catch (ClassNotFoundException e){
            Log.d(TAG, "ClassNotFoundException: "+e);
        }
    }
}

