package com.suzanelsamahy.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.suzanelsamahy.bakingapp.MainActivity;
import com.suzanelsamahy.bakingapp.R;
import com.suzanelsamahy.bakingapp.data.SharedPrefrenceManager;
import com.suzanelsamahy.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class RecipeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Ingredient> ingredients, String recipeName) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_container);

        initIngredients(views, ingredients, context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_view, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static ArrayList<Ingredient> ingredients;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        try {
            int id = SharedPrefrenceManager.getFavoriteRecipeId(context);
            String recipeName;
            if (id == 1) {
                recipeName = "Nutella Pie";
            } else if (id == 2) {
                recipeName = "Brownies";
            } else if (id == 3) {
                recipeName = "Yellow Cake";
            } else {
                recipeName = "Cheese cake";
            }

            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, ingredients, recipeName);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getIngForWidget(ArrayList<Ingredient> ing) {
        ingredients = ing;
    }

    private static void initIngredients(RemoteViews containerView, ArrayList<Ingredient> ingredients, Context context) {
        for (Ingredient ingredient : ingredients) {
            RemoteViews mIngredientView = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
            String fullDescription = String.valueOf(ingredient.getQuantity()) + " " + ingredient.getMeasure() + " " + ingredient.getIngredient() + "\n";
            mIngredientView.setTextViewText(R.id.widget_ingredient_name, fullDescription);
            containerView.addView(R.id.widget_container, mIngredientView);
        }
    }

    @Override
    public void onEnabled(Context context) {
        RecipeService.startRecipeWidget(context);
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

}

