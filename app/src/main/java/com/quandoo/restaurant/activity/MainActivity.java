package com.quandoo.restaurant.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.quandoo.restaurant.R;
import com.quandoo.restaurant.fragment.CustomerListFragment;
import com.quandoo.restaurant.fragment.TableListFragment;
import com.quandoo.restaurant.utils.Communicator;


public class MainActivity extends BaseActivity implements Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new CustomerListFragment());
        Log.d("onCreate","onCreate");
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void customerClicked() {
        addFragment(new TableListFragment());
    }
}
