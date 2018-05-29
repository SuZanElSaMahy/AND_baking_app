package com.suzanelsamahy.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
        int id = SharedPrefrenceManager.getFavoriteRecipeId(this);
        try {

          ingredients =  SharedPrefrenceManager.getInstance(this).loadFavorites();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
            String recipeName;
            if (id == 1) {
                    recipeName = "Nutella Pie";
                } else if(id==2) {
                    recipeName = "Brownies";
                } else if (id==3){
                    recipeName ="Yellow Cake";
                } else {
                    recipeName = "Cheese cake";
                }
                RecipeAppWidget.updateRecipeWidgets(getApplicationContext(),appWidgetManager,appWidgetIds,ingredients,recipeName);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
