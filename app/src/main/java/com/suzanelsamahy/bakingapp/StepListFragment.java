package com.suzanelsamahy.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suzanelsamahy.bakingapp.adapters.StepRecyclerAdapter;
import com.suzanelsamahy.bakingapp.models.Ingredient;
import com.suzanelsamahy.bakingapp.models.Recipe;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.suzanelsamahy.bakingapp.R.string.preposition;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepListFragment extends Fragment {

    @BindView(R.id.step_list_rv)
    RecyclerView homeRecyclerView;

    @BindView(R.id.ingredient_details_tv)
    TextView ingredientsTv;

    private Unbinder unbinder;
    private StepRecyclerAdapter recyclerAdapter;
    private Recipe recipeItem;
    private NavigateToStepListFragment mListener;

    public StepListFragment() {
        // Required empty public constructor
    }


    public static StepListFragment getInstance(Recipe recipe) {
        StepListFragment fragment = new StepListFragment();
        fragment.recipeItem = recipe;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(savedInstanceState!=null){
            recipeItem = savedInstanceState.getParcelable("recipe");
        }

        if(recipeItem!=null){

            initRecyclerView(recipeItem.getSteps());
            initIngredients(ingredientsTv,recipeItem.getIngredients());
        } else {
            initRecyclerView(new ArrayList<Step>());
        }


        return view;
    }


    private int position;

    private void initRecyclerView(List<Step> arrayList) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        homeRecyclerView.setLayoutManager(manager);
        recyclerAdapter = new StepRecyclerAdapter(getActivity(), arrayList);
        homeRecyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new StepRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step item, int pos) {
                position =pos;
                mListener.onStepNavigationListeneer(recipeItem.getSteps(),pos);
            }
        });
    }

    private void initIngredients(TextView textView, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            String concat = String.valueOf(ingredient.getQuantity()) + " " + ingredient.getMeasure();
            String joined = getString(preposition, concat);
            String fullDescription = "- " + joined + " " + ingredient.getIngredient() + "\n";
            textView.append(fullDescription);
        }
    }


    public interface NavigateToStepListFragment{
        public void onStepNavigationListeneer(List<Step> stepItems, int pos);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavigateToStepListFragment) {
            mListener = (NavigateToStepListFragment) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LocationFragmentListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("recipe", recipeItem);
    }

}




