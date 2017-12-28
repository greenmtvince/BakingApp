package com.quantrian.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Vinnie on 12/28/2017.
 */

public class ContractIngredient {
    public static final String AUTHORITY = "com.quantrian.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_X = "X";

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_X).build();

        public static final String TABLE_NAME = "ingredients";
        //public static final String COLUMN_ID = "id";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_FK_RECIPE = "fk_recipe";

    }
}