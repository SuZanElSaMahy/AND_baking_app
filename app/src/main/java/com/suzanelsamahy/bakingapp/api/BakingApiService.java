package com.suzanelsamahy.bakingapp.api;

import com.suzanelsamahy.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface BakingApiService {
        @GET("topher/2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getRecipes();
    }
