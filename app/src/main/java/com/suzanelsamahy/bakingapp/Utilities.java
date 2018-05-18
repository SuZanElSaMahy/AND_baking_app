package com.suzanelsamahy.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.suzanelsamahy.bakingapp.data.RecipeContract;
import com.suzanelsamahy.bakingapp.models.Ingredient;
import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;


public class Utilities {


    public static int isFavorite(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,   // projection
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ?", // selection
                new String[]{Integer.toString(id)},   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

    public static ArrayList<Ingredient> getFavoriteIngredients(Context context, int recipeId) {
        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI,
                    null,
                    RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ?", // selection
                    new String[]{Integer.toString(recipeId)},
                    null);

        } catch (Exception e) {
            Log.e("data1", "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }

        int numRows = cursor.getCount();
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int quantityIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_QUANTITY);
            int measureIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_MEASURE);
            int ingrdientIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_NAME);
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(cursor.getLong(quantityIndex));
                ingredient.setMeasure(cursor.getString(measureIndex));
                ingredient.setIngredient(cursor.getString(ingrdientIndex));
                ingredients.add(ingredient);
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return ingredients;


    }


    public static void addFavourite(Context context, Recipe recipeObject) {

        // save recipe content
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME, recipeObject.getName());
        recipeValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, recipeObject.getId());
        context.getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
        addIngredientsToProvider(context,recipeObject.getIngredients());

    }



    public static void addIngredientsToProvider(Context context , List<Ingredient> ingredients){


        ContentValues recipeValues = new ContentValues();
        // save ingredients of a recipe
        ArrayList<ContentValues> ingredientContentValues = new ArrayList<>();
        // ContentValues mIngredientValues = new ContentValues();


        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient current = ingredients.get(i);
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_QUANTITY, current.getQuantity());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_MEASURE, current.getMeasure());
            recipeValues.put(RecipeContract.RecipeEntry.COLUMN_INGRDIENT_NAME, current.getIngredient());
            ingredientContentValues.add(recipeValues);
        }


        ContentValues[] ingredientContentValue = new ContentValues[ingredientContentValues.size()];
        ingredientContentValue = ingredientContentValues.toArray(ingredientContentValue);
        context.getContentResolver().bulkInsert(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI, ingredientContentValue);

//        if (ingredientContentValue != null && ingredientContentValue.length != 0) {
//            /* Get a handle on the ContentResolver to delete and insert data */
//            ContentResolver ingredientContentResolver = context.getContentResolver();
//            /* Delete old step data */
//            ingredientContentResolver.delete(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI, null, null);
//            /* Insert our new movie data into Movie's ContentProvider */
//            ingredientContentResolver.bulkInsert(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI, ingredientContentValue);
//        }
    }

    public static void deleteFavorite(Context context, int id) {
        context.getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ?",
                new String[]{Integer.toString(id)});

        context.getContentResolver().delete(RecipeContract.RecipeEntry.INGREDIENTS_CONTENT_URI, null, null);

    }




}
