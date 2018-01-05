package com.quantrian.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.quantrian.bakingapp.data.ContractRecipe.RecipeEntry;
import com.quantrian.bakingapp.data.ContractStep.StepEntry;
import com.quantrian.bakingapp.data.ContractIngredient.IngredientEntry;
/*
*   IGNORE FOR PROJECT EVALUATION
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
public class DbHelperRecipe extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelperRecipe(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " ("+
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_IMAGE + " TEXT" +
                "); ";
        final String SQL_CREATE_STEP_TABLE = "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                StepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                StepEntry.COLUMN_THUMBNAIL_URL + " TEXT, " +
                StepEntry.COLUMN_VIDEO_URL + " TEXT, " +
                StepEntry.COLUMN_FK_RECIPE + " INTEGER, " +
                "FOREIGN KEY(fk_recipe) REFERENCES recipes(id)"+
                "); ";
        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COLUMN_INGREDIENT + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                IngredientEntry.COLUMN_QUANTITY + " REAL, " +
                IngredientEntry.COLUMN_FK_RECIPE + " INTEGER, " +
                "FOREIGN KEY(fk_recipe) REFERENCES recipes(id)"+
                "); ";

        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_STEP_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
