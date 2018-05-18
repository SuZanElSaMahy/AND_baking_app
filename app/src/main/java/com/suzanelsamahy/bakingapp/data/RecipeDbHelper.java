package com.suzanelsamahy.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suzanelsamahy.bakingapp.data.RecipeContract.*;

public class RecipeDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "recipe.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the plants data
        final String SQL_CREATE_RECIPES_TABLE = "CREATE TABLE " + RecipeEntry.RECIPE_TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL)";


        final String SQL_CREATE_INGREDIENT_TABLE = "CREATE TABLE " + RecipeEntry.INGRDIENT_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "+
                        RecipeEntry.COLUMN_INGRDIENT_QUANTITY + " REAL NOT NULL, "+
                        RecipeEntry.COLUMN_INGRDIENT_MEASURE + " TEXT NOT NULL, "+
                        RecipeEntry.COLUMN_INGRDIENT_NAME + " TEXT NOT NULL "+")";



        final String SQL_CREATE_STEP_TABLE = "CREATE TABLE " + RecipeEntry.STEP_TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, "+
                RecipeEntry.COLUMN_STEP_DESC + " TEXT NOT NULL, "+
                RecipeEntry.COLUMN_STEP_SHORT_DESC + " TEXT NOT NULL, "+
                RecipeEntry.COLUMN_STEP_THUMBNAIL + " TEXT NOT NULL, "+
                RecipeEntry.COLUMN_STEP_VIDEO + " TEXT NOT NULL "+")";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.INGRDIENT_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.STEP_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
