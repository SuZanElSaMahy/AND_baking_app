package com.suzanelsamahy.bakingapp.api.presenter;

import com.suzanelsamahy.bakingapp.api.ApiCallBack;
import com.suzanelsamahy.bakingapp.api.BakingClient;
import com.suzanelsamahy.bakingapp.api.view.BakingView;
import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by SuZan ElsaMahy on 04-May-18.
 */

public class BakingPresenter implements IBakingPresenter, ApiCallBack {

    private BakingView bakingView;


    public BakingPresenter(BakingView movieView) {
        this.bakingView = movieView;

    }


    @Override
    public void getRecipeData() {
        BakingClient.getInstance().getBakingResponse(this);
    }

    @Override
    public void onDestroy() {
        bakingView = null;
    }


    @Override
    public void onFailure(String message) {
        if (bakingView != null) {
            bakingView.onBakingDataFailure(message);
        }
    }

    @Override
    public void onSuccess(Object object) {

        if (bakingView == null) {
            return;
        }
        if (object != null) {
            List<Recipe> response = (List<Recipe>) object;
            bakingView.onBakingDataRetrieveSuccess(response);
        } else {
            bakingView.onBakingDataFailure("Error no Response");
        }
    }
}
