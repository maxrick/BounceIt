package com.maxrick.bounceit.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.max.jumpingapp.R;

import layout.GemFragment;
import layout.HomeFragment;

public class HelpPage extends AppCompatActivity implements GemFragment.OnGemFragmentInteractionListener, HomeFragment.OnHomeFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help_page);
    }

    @Override
    public void onHomeFragmentInteraction(View view) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        homeFragment.homeButtonClicked(view);
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }
}
