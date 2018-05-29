package com.suzanelsamahy.bakingapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.suzanelsamahy.bakingapp.RecipePagerFragment;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SuZan ElsaMahy on 07-May-18.
 */

public class StepsPagerAdapter extends FragmentStatePagerAdapter {
    public List<Step> steps = new ArrayList<>();

    public StepsPagerAdapter(FragmentManager fm, List<Step> steps) {
        super(fm);
        this.steps = steps;
        // users.remove(items.size()-1);
    }

    @Override
    public Fragment getItem(int position) {
        //Step current = steps.get(position);
        return RecipePagerFragment.getInstance(position, steps);
    }

    @Override
    public int getCount() {
        return steps.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  String.valueOf(steps.get(position).getId()) ;
    }
}
