package com.suzanelsamahy.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.suzanelsamahy.bakingapp.MainActivity;
import com.suzanelsamahy.bakingapp.R;
import com.suzanelsamahy.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class RecipeAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Ingredient> ingredients, String recipeName) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);

        initIngredients(views, ingredients, context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_view, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager, appWidgetIds);
        RecipeService.startRecipeWidget(context);

    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, ArrayList<Ingredient> ingredients, String recipeName) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,ingredients ,recipeName);
        }
    }

    private static void initIngredients(RemoteViews containerView, ArrayList<Ingredient> ingredients, Context context) {
        for (Ingredient ingredient : ingredients) {
            RemoteViews mIngredientView = new RemoteViews(context.getPackageName(),   R.layout.widget_ingredient_item);
            String fullDescription = String.valueOf(ingredient.getQuantity()) + " " + ingredient.getMeasure() + " " + ingredient.getIngredient() + "\n";
            mIngredientView.setTextViewText(R.id.widget_ingredient_name, fullDescription);
            containerView.addView(R.id.widget_ingredients_container, mIngredientView);
        }
    }

    @Override
    public void onEnabled(Context context) {
        RecipeService.startRecipeWidget(context);
        super.onEnabled(context);
        // Enter relevant functionality for when the first widget is created
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

