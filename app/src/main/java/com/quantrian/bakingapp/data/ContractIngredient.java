package com.quantrian.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/*   IGNORE FOR PROJECT EVALUATION
        *
        *Originally I'd intended to use a SQL Database to store data connected through a content provider
        * SQL is my goto.  As I got further into the project I realized it was complete overkill for what I
        * needed to do and there were better methods for the data at hand.
        *
        * Still this is working and tested and rather than delete it entirely I figured I'd keep it on hand
        * here to use as a boilerplate for future code.
        *
        * FORK CODE AND DELETE
        */
public class ContractIngredient {
    public static final String AUTHORITY = "com.quantrian.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_INGREDIENT = "ingredient";

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "ingredients";
        //public static final String COLUMN_ID = "id";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_FK_RECIPE = "fk_recipe";

    }
}