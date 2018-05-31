package com.suzanelsamahy.bakingapp;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suzanelsamahy.bakingapp.adapters.StepsPagerAdapter;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterDetailFragment extends Fragment {


    public MasterDetailFragment() {
        // Required empty public constructor
    }



    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.recipe_step_tablayout)
    TabLayout recipeStepTabLayout;
    private Unbinder unbinder;
    private StepsPagerAdapter stepsPagerAdapter;
    private List<Step> stepItem;
    private int position;

    public static MasterDetailFragment getInstance(int stepNumber,List<Step> recipe) {
        MasterDetailFragment fragment = new MasterDetailFragment();
        fragment.stepItem = recipe;
        fragment.position=stepNumber;
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!=null && getArguments().containsKey("steps")) {
            this.stepItem =  getArguments().getParcelableArrayList("steps");
            this.position = getArguments().getInt("pos");
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_master_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(savedInstanceState!=null){
            stepItem = savedInstanceState.getParcelableArrayList("g");
            position =savedInstanceState.getInt("h");
        }


        stepsPagerAdapter = new StepsPagerAdapter(getActivity().getSupportFragmentManager(),stepItem);
        mViewPager.setAdapter(stepsPagerAdapter);
        mViewPager.setCurrentItem(position);
        recipeStepTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("g", (ArrayList<? extends Parcelable>) stepItem);
        savedInstanceState.putInt("h", position);
    }
}
