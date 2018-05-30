package com.suzanelsamahy.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.suzanelsamahy.bakingapp.data.SharedPrefrenceManager;
import com.suzanelsamahy.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class RecipeService extends IntentService {

    ArrayList<Ingredient> ingredients;
    public RecipeService() {
        super("RecipeService");
    }

    public static void startRecipeWidget(Context context) {
        Intent intent = new Intent(context, RecipeService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
          ingredients =  SharedPrefrenceManager.getInstance(this).loadFavorites();
            RecipeAppWidget.getIngForWidget(ingredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
