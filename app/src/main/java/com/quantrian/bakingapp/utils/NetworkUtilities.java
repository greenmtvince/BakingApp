package com.quantrian.bakingapp.utils;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;
import java.io.IOException;
import java.net.URL;
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

    public static JSONArray fetchJsonArray() {
        String itemsJson = null;
        try {
            itemsJson = fetchPlainText(UrlConfig.BASE_URL);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching items JSON", e);
            return null;
        }

        // Parse JSON
        try {
            JSONTokener tokener = new JSONTokener(itemsJson);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            return (JSONArray) val;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing items JSON", e);
        }

        return null;
    }

    /**---------------------------------------------------
     *-------Uses a Square Library (3rd Party)------------
     *----------------------------------------------------
    */

    static String fetchPlainText(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}