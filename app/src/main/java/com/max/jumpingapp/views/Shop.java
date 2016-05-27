package com.max.jumpingapp.views;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.max.jumpingapp.R;

public class Shop extends AppCompatActivity {

    public static final int PLAYERIMAGE_HAT_AND_SHOES = R.drawable.playerimage_hat_and_shoes;
    public static final int PLAYERIMAGE = R.drawable.playerimage;
    public static final String SELECT_THIS_PLAYER = "Select this Player?";
    public static final String PLAYER_IMAGE = "player_image";
    private SliderLayout slideShow;
    public int playerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        slideShow = (SliderLayout) findViewById(R.id.slider);
        playerImage = PLAYERIMAGE;

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
               popup(R.drawable.playerimage_hat_and_shoes);
            }
        });


        slideShow.addSlider(textSliderView);
        slideShow.addSlider(textSliderView1);
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
                setPlayerImage();
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

    public void setPlayerImage() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS, 0);
        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
        prefEdit.putInt(PLAYER_IMAGE, this.playerImage);
        prefEdit.apply();
    }
}
