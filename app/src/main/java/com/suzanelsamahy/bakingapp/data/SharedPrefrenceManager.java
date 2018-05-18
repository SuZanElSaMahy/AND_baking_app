package com.suzanelsamahy.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suzanelsamahy.bakingapp.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefrenceManager {

    private static SharedPrefrenceManager ourInstance;
    private SharedPreferences sharedPreferences;

    public SharedPrefrenceManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static SharedPrefrenceManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SharedPrefrenceManager(context);
        }
        return ourInstance;
    }



    public void saveFavorites(List<Ingredient> ings){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(ings);
        prefsEditor.putString("fav", jsonFavorites);
        prefsEditor.commit();
    }

    public static final String RECIPE = "recipe";
    public static int getFavoriteRecipeId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(RECIPE, 1);
    }
    public static void setFavoriteRecipeId(Context context, int input) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(RECIPE, input)
                .apply();
    }


    public ArrayList loadFavorites() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("fav", "");
        ArrayList<Ingredient> list = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>() {
        }.getType());
        return list;
    }
}
