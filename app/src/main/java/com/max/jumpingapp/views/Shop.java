package com.max.jumpingapp.views;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.max.jumpingapp.R;
import com.max.jumpingapp.util.PrefsHandler;

import layout.GemFragment;

public class Shop extends AppCompatActivity implements GemFragment.OnGemFragmentInteractionListener {

    public static final int PLAYERIMAGE_HAT_AND_SHOES = R.drawable.playerimage_hat_and_shoes;
    public static final int PLAXERIMAGE_STICK = R.drawable.playerimage_stickfigure;
    public static final int PLAYERIMAGE = R.drawable.playerimage;
    public static final String SELECT_THIS_PLAYER = "Select this Player?";
    public static final String BUY_THIS_PLAYER = "Do you want to buy this player for 1 gem?";

    private SliderLayout slideShow;
    public int playerImage;
//@// TODO: 5/27/2016 to low cohesion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        slideShow = (SliderLayout) findViewById(R.id.slider);
        slideShow.stopAutoCycle();
        playerImage = PLAYERIMAGE;
//@// TODO: 5/28/2016 open closed principle
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView.description("player image").image(PLAYERIMAGE);
        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                popup(PLAYERIMAGE);
            }
        });

        TextSliderView textSliderView1 = new TextSliderView(this);
        textSliderView1.description("shoes and hat").image(PLAYERIMAGE_HAT_AND_SHOES);
        textSliderView1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
               popup(PLAYERIMAGE_HAT_AND_SHOES);
            }
        });

        TextSliderView textSliderView2 = new TextSliderView(this);
        textSliderView2.description("stick figure").image(PLAXERIMAGE_STICK);
        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                popup(PLAXERIMAGE_STICK);
            }
        });

        slideShow.addSlider(textSliderView);
        slideShow.addSlider(textSliderView1);
        slideShow.addSlider(textSliderView2);
    }

    @Override
    protected void onStop() {
        slideShow.stopAutoCycle();
        super.onStop();
    }
    public void popup(int playerimage_hat_and_shoes){
        this.playerImage = playerimage_hat_and_shoes;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(playerImageBought() || isDefaultImage()){
                    setPlayerImage();
                }else {
                    buyPlayerImagePopup();
                }
            }


        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(SELECT_THIS_PLAYER);
        builder.create().show();
    }

    private void buyPlayerImagePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(buyImage()){
                    setPlayerImage();
                    GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
                    gemFragment.updateGemText();
                }else {
                    System.out.println("sorry, not enough gems");//@// TODO: 5/27/2016
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(BUY_THIS_PLAYER);
        builder.create().show();
    }

    private boolean buyImage() {
        return PrefsHandler.buyPlayerImage(getSharedPreferences(MainActivity.GANME_PREFS, 0), this.playerImage);
    }

    private boolean isDefaultImage() {
        return this.playerImage == PLAYERIMAGE;
    }

    private boolean playerImageBought() {
        return PrefsHandler.playerImageBought(getSharedPreferences(MainActivity.GANME_PREFS, 0), this.playerImage);
    }

    public void setPlayerImage() {
        PrefsHandler.setPlayerImage(getSharedPreferences(MainActivity.GANME_PREFS, 0), this.playerImage);
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }

    public void deletePurchasesClicked(View view){
        PrefsHandler.deletePlayerImages(getSharedPreferences(MainActivity.GANME_PREFS, 0));
    }
}
