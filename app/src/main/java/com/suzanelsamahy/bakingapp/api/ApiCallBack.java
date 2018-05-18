package com.suzanelsamahy.bakingapp.api;

/**
 * Created by SuZan ElsaMahy on 04-May-18.
 */

public interface ApiCallBack {

    public void onFailure(String error);
    public void onSuccess(Object object);

}
