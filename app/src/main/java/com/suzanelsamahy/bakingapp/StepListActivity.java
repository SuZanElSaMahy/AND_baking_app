package com.suzanelsamahy.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.suzanelsamahy.bakingapp.base.BaseAppCompatActivity;
import com.suzanelsamahy.bakingapp.models.Recipe;
import com.suzanelsamahy.bakingapp.models.Step;

import java.util.List;

import static com.suzanelsamahy.bakingapp.MainActivity.RECIPE_Bool;
import static com.suzanelsamahy.bakingapp.MainActivity.RECIPE_INT;

public class StepListActivity extends BaseAppCompatActivity implements StepListFragment.NavigateToStepListFragment {


    private boolean paneMode;
    private int clickedPostion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        if (getIntent().getExtras() != null) {

          //  paneMode = getIntent().getBooleanExtra(RECIPE_Bool, false);
            Recipe args = getIntent().getParcelableExtra(RECIPE_INT);
            clickedPostion = getIntent().getIntExtra(RECIPE_Bool,0);
            paneMode = getResources().getBoolean(R.bool.isTwoPane);

            if (paneMode) {

                if(savedInstanceState==null){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    StepListFragment mainFragment = StepListFragment.getInstance(args);
                    fragmentManager.beginTransaction().add(R.id.step_container, mainFragment)
                            .commit();

                    MasterDetailFragment detailFragmentFragment = MasterDetailFragment.getInstance(clickedPostion,args.getSteps());
                    fragmentManager.beginTransaction().add(R.id.step_detail_container, detailFragmentFragment,"detail")
                            .commit();
                }
            } else {
                if(savedInstanceState==null) {
                    StepListFragment mainFragment = StepListFragment.getInstance(args);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.step_container, mainFragment)
                            .addToBackStack(null).commit();
                }
            }

           setTitle(args.getName());
        }

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount()> 0) {
            getSupportFragmentManager().popBackStackImmediate();
            //  finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStepNavigationListeneer(List<Step> stepItems, int pos) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("detail");

            if(paneMode){

                MasterDetailFragment mainFragment = MasterDetailFragment.getInstance(pos,stepItems);
                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.step_detail_container, mainFragment,"detail")
                            .commit();

//                if(fragment!=null){
//
//                    Bundle args = new Bundle();
//                    args.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) stepItems);
//                    args.putInt("pos",pos);
//                    fragment.setArguments(args);
//                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.step_detail_container, fragment,"detail")
//                            .commit();
//
//                } else {
//                    MasterDetailFragment mainFragment = MasterDetailFragment.getInstance(pos,stepItems);
//                    fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.step_detail_container, mainFragment,"detail")
//                            .commit();
//                }

            } else {
                MasterDetailFragment mainFragment = MasterDetailFragment.getInstance(pos,stepItems);
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.step_container, mainFragment)
                        .commit();
            }
    }
}
