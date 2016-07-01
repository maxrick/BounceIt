package com.max.jumpingapp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.max.jumpingapp.R;

import layout.HomeFragment;

public class HelpPage2 extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_page2);
    }

    @Override
    public void onHomeFragmentInteraction(View view) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        homeFragment.homeButtonClicked(view);
    }
}
