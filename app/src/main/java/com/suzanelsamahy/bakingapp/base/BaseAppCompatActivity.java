package com.suzanelsamahy.bakingapp.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.suzanelsamahy.bakingapp.R;


public class BaseAppCompatActivity extends AppCompatActivity {


    private BaseViewBridge baseViewBridge;
    protected Fragment curFragment;
    protected boolean connectedToInternet = true;

    public boolean isConnectedToInternet() {
        return connectedToInternet;
    }

    protected void hideKeyboard(){
        baseViewBridge.hideKeyboard();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseViewBridge = new BaseViewBridge(this);

    }

    protected void showProgressDialog() {
        baseViewBridge.showProgressDialog();
    }

    protected void hideProgressDialog() {
       baseViewBridge.hideProgressDialog();
    }

    protected void showToastMessage(String message){
        baseViewBridge.showToastMessage(message);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void showFragment(Fragment fragment, int id) {
        try {
            curFragment = fragment;
            String fragmentTag = fragment.getClass().getName();
            getSupportFragmentManager().beginTransaction()
                    .add(id, fragment, fragmentTag).addToBackStack(fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void navigateToNextFragment(Fragment fragment, int id) {
        showFragment(fragment,id);
    }

    public void replaceFragment(Fragment fragment, int id) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().add(id, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName()).commit();
        curFragment = fragment;
    }


    private ConnectivityChangeReceiver mConnectivityChangeReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        mConnectivityChangeReceiver = new ConnectivityChangeReceiver();
        registerReceiver(mConnectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectivityChangeReceiver);
    }



    private class ConnectivityChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY)) {
                    connectedToInternet = false;
                    Toast.makeText(BaseAppCompatActivity.this, getString(R.string.common_no_internet_available),Toast.LENGTH_LONG).show();
                } else {
                    connectedToInternet = true;
                }
            }
        }
    }


}
