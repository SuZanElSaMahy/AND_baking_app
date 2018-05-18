package com.suzanelsamahy.bakingapp.api.view;

import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by SuZan ElsaMahy on 04-May-18.
 */

public interface BakingView {

    void onBakingDataRetrieveSuccess(List<Recipe> recipeList);

    void onBakingDataFailure(String error);

}
