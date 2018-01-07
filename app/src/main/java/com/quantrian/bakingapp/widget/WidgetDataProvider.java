package com.quantrian.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.quantrian.bakingapp.R;
import com.quantrian.bakingapp.models.Ingredient;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.JsonUtilities;
import com.quantrian.bakingapp.utils.NetworkUtilities;
import com.quantrian.bakingapp.utils.PrettyStrings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vinnie on 1/4/2018.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory{
    private static final String TAG = WidgetDataProvider.class.getSimpleName();
    private List<String> collection = new ArrayList<>();
    Context context;
    Intent intent;

    private void initializeData(){
        collection.clear();
        getRecipeFromPrefs(context);
        /*for (int i=1; i<10;i++){
            collection.add("ListView Item "+i);
        }*/
    }

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        initializeData();
    }

    @Override
    public void onDataSetChanged() {
        initializeData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return collection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteView.setTextViewText(android.R.id.text1, collection.get(position));
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getRecipeFromPrefs(Context context){
        SharedPreferences preferences = context
                .getSharedPreferences(context.getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        int position = Integer.parseInt(preferences.
                getString(context.getString(R.string.selected_recipe),"0"));
        Log.d("WIDGETOR", "getRecipeFromPrefs: "+position);

        String json_array = preferences.getString(context.getString(R.string.json_array),null);
        Recipe recipe =new Recipe();
        try {
            Recipe[] list = JsonUtilities.deSerialize(json_array, Recipe[].class);
            recipe= new ArrayList<>(Arrays.asList(list)).get(position);
        } catch (ClassNotFoundException e){
            Log.d(TAG, "ClassNotFoundException: "+e);
        }

        /*Recipe recipe =  ((ArrayList<Recipe>)(Object) JsonUtilities
                            .deSerializeList(json_array, Recipe[].class)).get(position);*/
        for (Ingredient ingredient : recipe.ingredients){
            collection.add(PrettyStrings.ingredientToString(ingredient));
        }
    }
}
