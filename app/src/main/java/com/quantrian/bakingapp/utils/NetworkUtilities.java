package com.quantrian.bakingapp.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.quantrian.bakingapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Methods borrowed from the xyz-reader-starter-code from the Udacity Materials Design Project
 * because it is a quick working way to get a remote static JSON File specified by the project.
 *
 * I'd use a HTTP library if I was querying an API endpoint for dynamic content
 */

public class NetworkUtilities {

    private static final String TAG = NetworkUtilities.class.getName();

    public static ArrayList<Recipe> fetchJsonArray() {
        String itemsJson = null;
        try {
            itemsJson = fetchPlainText(UrlConfig.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // Parse JSON
        try {
            return convertArray(itemsJson);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    /**---------------------------------------------------
     *-------Uses a Square Library (3rd Party)------------
     *----------------------------------------------------
    */

    public static String fetchPlainText(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static ArrayList<Recipe> convertArray(String rawJson) {
        Gson gson = new Gson();
        Recipe[] rArray = gson.fromJson(rawJson, Recipe[].class);

        return new ArrayList<>(Arrays.asList(rArray));
    }


}