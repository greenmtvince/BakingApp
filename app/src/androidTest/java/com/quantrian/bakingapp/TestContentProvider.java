package com.quantrian.bakingapp;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.quantrian.bakingapp.data.ContentProviderRecipe;
import com.quantrian.bakingapp.data.ContractIngredient;
import com.quantrian.bakingapp.data.ContractRecipe;
import com.quantrian.bakingapp.data.ContractStep;
import com.quantrian.bakingapp.data.DbHelperRecipe;
import com.quantrian.bakingapp.models.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
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
/**
 * Created by Vinnie on 12/30/2017.
 */



@RunWith(AndroidJUnit4.class)
public class TestContentProvider {

    /* Context used to access various parts of the system */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete all entries in the tasks directory to do so.
     */
    @Before
    public void setUp() {
        /* Use TaskDbHelper to get access to a writable database */
        DbHelperRecipe dbHelper = new DbHelperRecipe(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(ContractRecipe.RecipeEntry.TABLE_NAME, null, null);
        database.delete(ContractStep.StepEntry.TABLE_NAME, null, null);
        database.delete(ContractIngredient.IngredientEntry.TABLE_NAME, null, null);
    }


    //================================================================================
    // Test ContentProvider Registration
    //================================================================================


    /**
     * This test checks to make sure that the content provider is registered correctly in the
     * AndroidManifest file. If it fails, you should check the AndroidManifest to see if you've
     * added a <provider/> tag and that you've properly specified the android:authorities attribute.
     */
    @Test
    public void testProviderRegistry() {

        /*
         * A ComponentName is an identifier for a specific application component, such as an
         * Activity, ContentProvider, BroadcastReceiver, or a Service.
         *
         * Two pieces of information are required to identify a component: the package (a String)
         * it exists in, and the class (a String) name inside of that package.
         *
         * We will use the ComponentName for our ContentProvider class to ask the system
         * information about the ContentProvider, specifically, the authority under which it is
         * registered.
         */
        String packageName = mContext.getPackageName();
        String taskProviderClassName = ContentProviderRecipe.class.getName();
        ComponentName componentName = new ComponentName(packageName, taskProviderClassName);

        try {

            /*
             * Get a reference to the package manager. The package manager allows us to access
             * information about packages installed on a particular device. In this case, we're
             * going to use it to get some information about our ContentProvider under test.
             */
            PackageManager pm = mContext.getPackageManager();

            /* The ProviderInfo will contain the authority, which is what we want to test */
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority =
                    "Error: TaskContentProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: TaskContentProvider not registered at " + mContext.getPackageName();
            /*
             * This exception is thrown if the ContentProvider hasn't been registered with the
             * manifest at all. If this is the case, you need to double check your
             * AndroidManifest file
             */
            fail(providerNotRegisteredAtAll);
        }
    }


    //================================================================================
    // Test UriMatcher
    //================================================================================


    private static final Uri TEST_TASKS = ContractRecipe.RecipeEntry.CONTENT_URI;
    // Content URI for a single task with id = 1
    private static final Uri TEST_TASK_WITH_ID = TEST_TASKS.buildUpon().appendPath("1").build();


    /**
     * This function tests that the UriMatcher returns the correct integer value for
     * each of the Uri types that the ContentProvider can handle. Uncomment this when you are
     * ready to test your UriMatcher.
     */
    @Test
    public void testUriMatcher() {

        /* Create a URI matcher that the TaskContentProvider uses */
        UriMatcher testMatcher = ContentProviderRecipe.buildUriMatcher();

        /* Test that the code returned from our matcher matches the expected TASKS int */
        String tasksUriDoesNotMatch = "Error: The TASKS URI was matched incorrectly.";
        int actualTasksMatchCode = testMatcher.match(TEST_TASKS);
        int expectedTasksMatchCode = ContentProviderRecipe.RECIPES;
        assertEquals(tasksUriDoesNotMatch,
                actualTasksMatchCode,
                expectedTasksMatchCode);

        /* Test that the code returned from our matcher matches the expected TASK_WITH_ID */
        String taskWithIdDoesNotMatch =
                "Error: The TASK_WITH_ID URI was matched incorrectly.";
        int actualTaskWithIdCode = testMatcher.match(TEST_TASK_WITH_ID);
        int expectedTaskWithIdCode = ContentProviderRecipe.RECIPES_WITH_ID;
        assertEquals(taskWithIdDoesNotMatch,
                actualTaskWithIdCode,
                expectedTaskWithIdCode);
    }


    //================================================================================
    // Test Insert
    //================================================================================


    /**
     * Tests inserting a single row of data via a ContentResolver
     */
    @Test
    public void testInsert() {
        setUp();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_NAME, "Test name");
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_SERVINGS, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_ID, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_IMAGE, "");

        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                ContractRecipe.RecipeEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);


        Uri uri = contentResolver.insert(ContractRecipe.RecipeEntry.CONTENT_URI, testTaskValues);

        //Query the database directly to determine if our insert worked.
        DbHelperRecipe dbHelper = new DbHelperRecipe(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor retCursor = database.query(ContractRecipe.RecipeEntry.TABLE_NAME, null,
                "id=?", new String[]{"1"},null, null, "_id");

        //Check that there's only one result there.
        assertEquals("Toomany!",1,retCursor.getCount());


        retCursor.moveToFirst();
        int idIndex = retCursor.getColumnIndexOrThrow("_id");
        long row_id = retCursor.getLong(idIndex);
        retCursor.close();

        Uri expectedUri = ContentUris.withAppendedId(ContractRecipe.RecipeEntry.CONTENT_URI, row_id);

        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, expectedUri, uri);

        /*
         * If this fails, it's likely you didn't call notifyChange in your insert method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);

        /* We are done with the database, close it now. */
        database.close();
    }


    //================================================================================
    // Test Query (for tasks directory)
    //================================================================================


    /**
     * Inserts data, then tests if a query for the tasks directory returns that data as a Cursor
     */
    @Test
    public void testQuery() {

        /* Get access to a writable database */
        DbHelperRecipe dbHelper = new DbHelperRecipe(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Create values to insert */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_NAME, "Test name");
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_SERVINGS, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_ID, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_IMAGE, "");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        String insertFailed = "Unable to insert directly into the database";
        assertTrue(insertFailed, taskRowId != -1);

        /* We are done with the database, close it now. */
        database.close();

        /* Perform the ContentProvider query */
        Cursor taskCursor = mContext.getContentResolver().query(
                ContractRecipe.RecipeEntry.CONTENT_URI,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Sort order to return in Cursor */
                null);


        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, taskCursor != null);

        /* We are done with the cursor, close it now. */
        taskCursor.close();
    }


    //================================================================================
    // Test Delete (for a single item)
    //================================================================================


    /**
     * Tests deleting a single row of data via a ContentResolver
     */
    @Test
    public void testDelete() {
        /* Access writable database */
        DbHelperRecipe helper = new DbHelperRecipe(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        /* Create a new row of task data */
        ContentValues testTaskValues = new ContentValues();
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_NAME, "Test name");
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_SERVINGS, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_ID, 1);
        testTaskValues.put(ContractRecipe.RecipeEntry.COLUMN_IMAGE, "");

        /* Insert ContentValues into database and get a row ID back */
        long taskRowId = database.insert(
                /* Table to insert values into */
                ContractRecipe.RecipeEntry.TABLE_NAME,
                null,
                /* Values to insert into table */
                testTaskValues);

        /* Always close the database when you're through with it */
        database.close();

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, taskRowId != -1);


        /* TestContentObserver allows us to test if notifyChange was called appropriately */
        TestUtilities.TestContentObserver taskObserver = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        /* Register a content observer to be notified of changes to data at a given URI (tasks) */
        contentResolver.registerContentObserver(
                /* URI that we would like to observe changes to */
                ContractRecipe.RecipeEntry.CONTENT_URI,
                /* Whether or not to notify us if descendants of this URI change */
                true,
                /* The observer to register (that will receive notifyChange callbacks) */
                taskObserver);



        /* The delete method deletes the previously inserted row with id = 1 */
        Uri uriToDelete = ContractRecipe.RecipeEntry.CONTENT_URI.buildUpon().appendPath("1").build();
        int tasksDeleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in the database";
        assertTrue(deleteFailed, tasksDeleted != 0);

        /*
         * If this fails, it's likely you didn't call notifyChange in your delete method from
         * your ContentProvider.
         */
        taskObserver.waitForNotificationOrFail();

        /*
         * waitForNotificationOrFail is synchronous, so after that call, we are done observing
         * changes to content and should therefore unregister this observer.
         */
        contentResolver.unregisterContentObserver(taskObserver);
    }

}