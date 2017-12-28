package com.quantrian.bakingapp.utils;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class borrowed from the xyz-reader-starter-code from the Udacity Materials Design Project
 */

public class UrlConfig {
    public static final URL BASE_URL;
    private static String TAG = UrlConfig.class.toString();

    static {
        URL url = null;
        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        BASE_URL = url;
    }
}
