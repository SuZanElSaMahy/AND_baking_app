package com.suzanelsamahy.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RecipeProvider extends ContentProvider {

    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;

    public static final int INGREDIENTS = 200;
    public static final int INGREDIENTS_WITH_ID = 201;


    public static final int STEPS = 300;
    public static final int STEPS_WITH_ID = 301;

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_RECIPES + "/#", RECIPES_WITH_ID);

        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGRDIENTS, INGREDIENTS);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_INGRDIENTS + "/#", INGREDIENTS_WITH_ID);


        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(RecipeContract.AUTHORITY, RecipeContract.PATH_STEPS + "/#", STEPS_WITH_ID);

        return uriMatcher;
    }


    // Member variable for a PlantDbHelper that's initialized in the onCreate() method
    private RecipeDbHelper mRecipeDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mRecipeDbHelper = new RecipeDbHelper(context);
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {

            case RECIPES: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeEntry.RECIPE_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }


            case INGREDIENTS: {
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(RecipeContract.RecipeEntry.INGRDIENT_TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } catch (SQLException e){
                    e.printStackTrace();

                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mRecipeDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the plants directory
            case RECIPES: {
                retCursor = db.query(RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            }

            case RECIPES_WITH_ID: {
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            }

            // Query for the plants directory
            case INGREDIENTS: {
                retCursor = db.query(RecipeContract.RecipeEntry.INGRDIENT_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }


            case INGREDIENTS_WITH_ID: {
                String _id = uri.getPathSegments().get(1);
                retCursor = db.query(RecipeContract.RecipeEntry.INGRDIENT_TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{_id},
                        null,
                        null,
                        sortOrder);
                break;
            }


            case STEPS: {
                retCursor = db.query(RecipeContract.RecipeEntry.STEP_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }


            case STEPS_WITH_ID: {
                String _id = uri.getPathSegments().get(1);
                retCursor = db.query(RecipeContract.RecipeEntry.STEP_TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{_id},
                        null,
                        null,
                        sortOrder);
                break;
            }


            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mRecipeDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the plants directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case RECIPES: {
                // Insert new values into the database
                long id = db.insert(RecipeContract.RecipeEntry.RECIPE_TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            case INGREDIENTS: {
                // Insert new values into the database
                long _id = db.insert(RecipeContract.RecipeEntry.INGRDIENT_TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI, _id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }


            case STEPS: {
                // Insert new values into the database
                long _id = db.insert(RecipeContract.RecipeEntry.STEP_TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.STEP_CONTENT_URI, _id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }


            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case RECIPES:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case INGREDIENTS:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeEntry.INGRDIENT_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case STEPS:
                numRowsDeleted = mRecipeDbHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeEntry.STEP_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
