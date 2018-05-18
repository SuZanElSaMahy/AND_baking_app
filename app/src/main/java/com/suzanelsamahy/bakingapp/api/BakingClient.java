package com.suzanelsamahy.bakingapp.api;

import android.util.Log;

import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BakingClient {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static BakingApiService bakeApi = null;
    private static BakingClient apiClient;


    public static synchronized BakingClient getInstance() {
        if (apiClient == null) {
            apiClient = new BakingClient();
            return apiClient;
        } else {
            return apiClient;
        }
    }

    private BakingClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bakeApi = retrofit.create(BakingApiService.class);
    }


    public void getBakingResponse(final ApiCallBack apiCallBack) {
        Callback<List<Recipe>> recipeResponseCallback = new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.body() != null) {
                    apiCallBack.onSuccess(response.body());
                } else {
                    apiCallBack.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                apiCallBack.onFailure(t.getMessage());
                Log.d("api", "" + t.getMessage());
            }
        };

        Call<List<Recipe>> getRecipeList = bakeApi.getRecipes();
        getRecipeList.enqueue(recipeResponseCallback);
    }
}
