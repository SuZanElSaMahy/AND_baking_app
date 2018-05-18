package com.suzanelsamahy.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {


    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.suzanelsamahy.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "plants" directory
    public static final String PATH_RECIPES = "r_recipes";
    public static final String PATH_INGRDIENTS = "r_ingredients";
    public static final String PATH_STEPS = "r_step";

    public static final long INVALID_RECIPE_ID = -1;

    public static final class RecipeEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();


        public static final Uri INGREDIENTS_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGRDIENTS)
                .build();

        public static final Uri STEP_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEPS)
                .build();



        public static final String RECIPE_TABLE_NAME = "recipe";
        public static final String INGRDIENT_TABLE_NAME = "ingredient";
        public static final String STEP_TABLE_NAME = "step";


        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";


        public static final String COLUMN_INGRDIENT_QUANTITY= "ingredient_quantity";
        public static final String COLUMN_INGRDIENT_MEASURE = "ingredient_measure";
        public static final String COLUMN_INGRDIENT_NAME = "ingredient_name";


        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_STEP_SHORT_DESC = "step_short_desc";
        public static final String COLUMN_STEP_DESC = "step_desc";
        public static final String COLUMN_STEP_VIDEO = "step_video";
        public static final String COLUMN_STEP_THUMBNAIL = "step_thumbnail";


    }



}
