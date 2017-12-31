package com.quantrian.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.quantrian.bakingapp.data.ContractIngredient;
import com.quantrian.bakingapp.data.ContractRecipe;
import com.quantrian.bakingapp.data.ContractStep;
import com.quantrian.bakingapp.data.DbHelperRecipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;

/**
 * Borrowed from ud851-Exerises/Lesson07-Waitlist to get the hang of local unit testing.
 * Modified to accommodate my multi-table database schema.
 */

@RunWith(AndroidJUnit4.class)
public class dbTest {

    /* Context used to perform operations on the database and create WaitlistDbHelper */
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    /* Class reference to help load the constructor on runtime */
    private final Class mDbHelperClass = DbHelperRecipe.class;

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete the database to do so.
     */
    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void create_database_test() throws Exception{


        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        String[] tableNames = new String[]{ContractRecipe.RecipeEntry.TABLE_NAME,
                ContractIngredient.IngredientEntry.TABLE_NAME,
                ContractStep.StepEntry.TABLE_NAME
            };

        for (String tableName : tableNames) {

        /* This Cursor will contain the names of each table in our database */
            Cursor tableNameCursor = database.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                            tableName + "'",

                    null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
            String errorInCreatingDatabase =
                    "Error: This means that the database has not been created correctly";
            assertTrue(errorInCreatingDatabase,
                    tableNameCursor.moveToFirst());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
            assertEquals("Error: Your database was created without the expected tables.",
                    tableName, tableNameCursor.getString(0));

        /* Always close a cursor when you are done with it */
            tableNameCursor.close();
        }
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * The purpose is to test that the database is working as expected
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void insert_single_record_test_recipe() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = recipeValues(1);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert Recipe into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                ContractRecipe.RecipeEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from recipe query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }

    @Test
    public void insert_single_record_test_step() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = stepValues();

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                ContractStep.StepEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert Step into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                ContractStep.StepEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from recipe query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }

    @Test
    public void insert_single_record_test_ingredient() throws Exception{

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = ingredientValues(1);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                ContractIngredient.IngredientEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert Step into the database", -1, firstRowId);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                ContractIngredient.IngredientEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from recipe query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }


    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void autoincrement_test() throws Exception{

        /* First, let's ensure we have some values in our table initially */
        insert_single_record_test_recipe();

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = recipeValues(1);

        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                testValues);

        assertEquals("ID Autoincrement test failed!",
                firstRowId + 1, secondRowId);

    }


    /**
     * Tests that child entries in the step and ingredient table are linked to a single parent
     * recipe entry.
     */
    @Test
    public void linked_tables_database_test() throws Exception{

        /* Insert 2 rows before we upgrade to check that we dropped the database correctly */

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Insert Recipe into database and get first row ID back */
        long firstRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                recipeValues(1));
        long fourthtRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                recipeValues(2));

        /* Insert Steps into database */
        long secondRowId = database.insert(
                ContractIngredient.IngredientEntry.TABLE_NAME,
                null,
                ingredientValues(1));
        long thirdRowId = database.insert(
                ContractIngredient.IngredientEntry.TABLE_NAME,
                null,
                ingredientValues(1));
        long fifthRowId = database.insert(
                ContractIngredient.IngredientEntry.TABLE_NAME,
                null,
                ingredientValues(2));

        //dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        ContractRecipe.RecipeEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        Cursor tCursor = database.rawQuery(
                "SELECT "+ ContractRecipe.RecipeEntry.COLUMN_NAME+", " +
                        ContractIngredient.IngredientEntry.COLUMN_INGREDIENT + " FROM "+
                        ContractRecipe.RecipeEntry.TABLE_NAME + ", " +
                        ContractIngredient.IngredientEntry.TABLE_NAME+ " WHERE " +
                        ContractRecipe.RecipeEntry.COLUMN_ID+"="+
                        ContractIngredient.IngredientEntry.COLUMN_FK_RECIPE,null


        );

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */


        /* Cursor.moveToFirst will return false if there are no records returned from your query */

        assertTrue("Your query probably sucks",
                tCursor.moveToFirst());
        assertEquals("You didn't match",3, tCursor.getCount());


        tCursor.close();
        database.close();
    }

    /**
     * Tests that onUpgrade works by inserting 2 rows then calling onUpgrade and verifies that the
     * database has been successfully dropped and recreated by checking that the database is there
     * but empty
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void upgrade_database_test() throws Exception{

        /* Insert 2 rows before we upgrade to check that we dropped the database correctly */

        /* Use reflection to try to run the correct constructor whenever implemented */
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = recipeValues(1);


        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                testValues);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                testValues);

        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        ContractRecipe.RecipeEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                ContractRecipe.RecipeEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */

        assertFalse("Database doesn't seem to have been dropped successfully when upgrading",
                wCursor.moveToFirst());

        tableNameCursor.close();
        database.close();
    }

    /**
     * Deletes the entire database.
     */
    void deleteTheDatabase(){
        try {
            /* Use reflection to get the database name from the db helper class */
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the WaitlistDbHelper");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }

    public ContentValues recipeValues(int i){
        ContentValues testValues = new ContentValues();
        testValues.put(ContractRecipe.RecipeEntry.COLUMN_ID, i);
        testValues.put(ContractRecipe.RecipeEntry.COLUMN_IMAGE, "");
        testValues.put(ContractRecipe.RecipeEntry.COLUMN_NAME, "Apple Crisp");
        testValues.put(ContractRecipe.RecipeEntry.COLUMN_SERVINGS, 7);
        return testValues;
    }

    public ContentValues ingredientValues(int fk){
        ContentValues testValues = new ContentValues();
        testValues.put(ContractIngredient.IngredientEntry.COLUMN_QUANTITY, 5);
        testValues.put(ContractIngredient.IngredientEntry.COLUMN_MEASURE, "GigaWhats?");
        testValues.put(ContractIngredient.IngredientEntry.COLUMN_INGREDIENT, "NEWT LIVER");
        testValues.put(ContractIngredient.IngredientEntry.COLUMN_FK_RECIPE,fk);
        return testValues;
    }

    public ContentValues stepValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(ContractStep.StepEntry.COLUMN_ID, 1);
        testValues.put(ContractStep.StepEntry.COLUMN_SHORT_DESCRIPTION, "Some Intro");
        testValues.put(ContractStep.StepEntry.COLUMN_DESCRIPTION, "Some Longer Introduction");
        testValues.put(ContractStep.StepEntry.COLUMN_THUMBNAIL_URL, "");
        testValues.put(ContractStep.StepEntry.COLUMN_VIDEO_URL, "m.youtube.com");
        testValues.put(ContractStep.StepEntry.COLUMN_FK_RECIPE, 1);
        return testValues;
    }

}
