package com.suzanelsamahy.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.suzanelsamahy.bakingapp.adapters.MainRecyclerAdapter;
import com.suzanelsamahy.bakingapp.api.presenter.BakingPresenter;
import com.suzanelsamahy.bakingapp.api.presenter.IBakingPresenter;
import com.suzanelsamahy.bakingapp.api.view.BakingView;
import com.suzanelsamahy.bakingapp.base.BaseAppCompatActivity;
import com.suzanelsamahy.bakingapp.data.SharedPrefrenceManager;
import com.suzanelsamahy.bakingapp.models.Recipe;
import com.suzanelsamahy.bakingapp.widget.RecipeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseAppCompatActivity implements
        //LoaderManager.LoaderCallbacks<List<Recipe>> ,
        BakingView {

    @BindView(R.id.main_rv)
    RecyclerView homeRecyclerView;

    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    @BindView(R.id.error_btn_retry)
    Button btnRetry;


    private Unbinder unbinder;
    private MainRecyclerAdapter recyclerAdapter;
    private boolean mTwoPane;
    public static String RECIPE_INT = " deliver";
    public static String RECIPE_Bool = " bool";
    private IBakingPresenter bakingPresenter;
    private ArrayList<Recipe> recipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            recipesList = savedInstanceState.getParcelableArrayList(getString(R.string.recipe_state));
        }
        bakingPresenter = new BakingPresenter(this);


        if (findViewById(R.id.tablet_pane_layout) != null) {
            mTwoPane = true;
            LinearLayoutManager manager = new GridLayoutManager(this, 2);
            homeRecyclerView.setLayoutManager(manager);
            recyclerAdapter = new MainRecyclerAdapter(this, new ArrayList<Recipe>());
            homeRecyclerView.setAdapter(recyclerAdapter);

        } else {
            mTwoPane = false;
            LinearLayoutManager manager = new LinearLayoutManager(this);
            homeRecyclerView.setLayoutManager(manager);
            recyclerAdapter = new MainRecyclerAdapter(this, new ArrayList<Recipe>());
            homeRecyclerView.setAdapter(recyclerAdapter);
        }

        if (isConnectedToInternet()) {
            bakingPresenter.getRecipeData();
            showProgressDialog();
        }


        //   getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

//    @Override
//    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
//        return new DataAsyncLoader(this);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
//        recyclerAdapter.setRecipes(data);
//        recyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Recipe item) {
//                Intent intent = new Intent(MainActivity.this,StepListActivity.class);
//                intent.putExtra(RECIPE_INT,item);
//                intent.putExtra(RECIPE_Bool,mTwoPane);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<Recipe>> loader) {
//        recyclerAdapter.setRecipes(new ArrayList<Recipe>());
//    }


    @Override
    public void onBakingDataRetrieveSuccess(List<Recipe> recipeList) {
        errorLayout.setVisibility(View.GONE);
        hideProgressDialog();
        recyclerAdapter.setRecipes(recipeList);
        RecipeService.startAction(getApplicationContext());

        recyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe item , int pos) {

               // Utilities.addFavourite(MainActivity.this, item);
                SharedPrefrenceManager.getInstance(MainActivity.this).saveFavorites(item.getIngredients());
                SharedPrefrenceManager.setFavoriteRecipeId(MainActivity.this, item.getId());
                RecipeService.startAction(MainActivity.this);


                Intent intent = new Intent(MainActivity.this, StepListActivity.class);
                intent.putExtra(RECIPE_INT, item);
                intent.putExtra(RECIPE_Bool, pos);
                startActivity(intent);
            }

            @Override
            public void onFavClick(Recipe recipe, boolean click) {

                if (click) {
                    Toast.makeText(MainActivity.this, "Added to Favorites", Toast.LENGTH_LONG).show();
//                    Utilities.addFavourite(MainActivity.this, recipe);
//                    SharedPrefrenceManager.getInstance(MainActivity.this).saveFavorites(recipe.getIngredients());
//                    Utilities.setFavoriteRecipeId(MainActivity.this, recipe.getId());
                  //  RecipeService.startAction(getApplicationContext());
                } else {
                    Utilities.deleteFavorite(MainActivity.this, recipe.getId());
                    Toast.makeText(MainActivity.this, "Removed From Favorites", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBakingDataFailure(String error) {
        hideProgressDialog();
        showToastMessage(error);
        errorLayout.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.error_btn_retry)
    public void retry() {
        bakingPresenter.getRecipeData();
        showProgressDialog();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.recipe_state), recipesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        bakingPresenter.onDestroy();
    }

}
