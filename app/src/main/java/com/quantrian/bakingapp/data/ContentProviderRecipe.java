package com.quantrian.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Vinnie on 12/30/2017.
 */

public class ContentProviderRecipe extends ContentProvider{
    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;
    public static final int INGREDIENTS_WITH_RECIPE_ID =202;
    public static final int STEPS_WITH_RECIPE_ID = 302;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, ContractRecipe.PATH_RECIPE, RECIPES);
        uriMatcher.addURI(ContractRecipe.AUTHORITY, ContractRecipe.PATH_RECIPE+"/#", RECIPES_WITH_ID);
        uriMatcher.addURI(ContractStep.AUTHORITY,ContractStep.PATH_STEP+"/#", STEPS_WITH_RECIPE_ID);
        uriMatcher.addURI(ContractIngredient.AUTHORITY, ContractIngredient.PATH_INGREDIENT+"/#", INGREDIENTS_WITH_RECIPE_ID);

        return uriMatcher;
    }

    private DbHelperRecipe mDbHelperRecipe;

    @Override
    public boolean onCreate() {
        mDbHelperRecipe= new DbHelperRecipe(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelperRecipe.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        String recipe_id;
        String mSelection;
        String[] mSelectionArgs;

        Cursor retCursor;
        switch (match){
            case RECIPES:
                retCursor= db.query(ContractRecipe.RecipeEntry.TABLE_NAME, projection, selection,
                        selectionArgs,null,null,sortOrder);
                break;
            case RECIPES_WITH_ID:
                recipe_id = uri.getPathSegments().get(1);
                mSelection ="id=?";
                mSelectionArgs =  new String[]{recipe_id};
                retCursor = db.query(ContractRecipe.RecipeEntry.TABLE_NAME, projection,mSelection,
                        mSelectionArgs,null,null,sortOrder);
                break;
            case STEPS_WITH_RECIPE_ID:
                recipe_id = uri.getPathSegments().get(1);
                mSelection ="fk_recipe=?";
                mSelectionArgs =  new String[]{recipe_id};
                retCursor = db.query(ContractStep.StepEntry.TABLE_NAME, projection,mSelection,
                        mSelectionArgs,null,null,sortOrder);
                break;
            case INGREDIENTS_WITH_RECIPE_ID:
                recipe_id = uri.getPathSegments().get(1);
                mSelection ="fk_recipe=?";
                mSelectionArgs =  new String[]{recipe_id};
                retCursor = db.query(ContractIngredient.IngredientEntry.TABLE_NAME, projection,mSelection,
                        mSelectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    /*
    * The risk with exposing the recipe step and ingredient inserts is that someone could potentially
    * insert an orphaned step or ingredient not associated with a recipe.  There's probably a better
    * content provider strategy.
     */

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelperRecipe.getWritableDatabase();
        long id;
        int match =sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case RECIPES:
                id = db.insert(ContractRecipe.RecipeEntry.TABLE_NAME, null, contentValues);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(ContractRecipe.RecipeEntry.CONTENT_URI,id);
                } else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            case INGREDIENTS_WITH_RECIPE_ID:
                id = db.insert(ContractIngredient.IngredientEntry.TABLE_NAME, null,contentValues);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(ContractIngredient.IngredientEntry.CONTENT_URI,id);
                } else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            case STEPS_WITH_RECIPE_ID:
                id = db.insert(ContractStep.StepEntry.TABLE_NAME,null,contentValues);
                if (id>0){
                    returnUri = ContentUris.withAppendedId(ContractStep.StepEntry.CONTENT_URI,id);
                } else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mDbHelperRecipe.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        String id;
        int itemDeleted;

        switch (match){
            //I'm not exposing the delete Steps or Ingredients so that the database doesn't end up
            //with orphaned steps or ingredients.  Or a recipe ends up with missing items.  Deleting
            //the recipe will clean up the rest.
            case RECIPES_WITH_ID:
                id = uri.getPathSegments().get(1);
                itemDeleted = db.delete(ContractStep.StepEntry.TABLE_NAME, "fk_recipe=?",
                        new String[]{id});
                itemDeleted += db.delete(ContractIngredient.IngredientEntry.TABLE_NAME,
                        "fk_recipe=?", new String[]{id});
                itemDeleted += db.delete(ContractRecipe.RecipeEntry.TABLE_NAME, "id=?",
                        new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if (itemDeleted !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return itemDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        return 0;
    }
}
