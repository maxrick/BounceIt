package com.max.jumpingapp.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
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
    public static final int PLAYERIMAGE_DEFAULT = R.drawable.playerimage;
    public static final int PLAYERIMAGE_EGGMAN  = R.drawable.eggman;
    public static final String SELECT_THIS_PLAYER = "Select this Player?";
    public static final String BUY_THIS_PLAYER = "Do you want to buy this player for 1 gem?";
    private static final String MORE_GEMS_NEEDED_DO_YOU_WANT = "You need more gems for this. Do you want to";
    public TextSliderView[] textSliders;
    public Buyable[] buyables = {
            new Buyable(PLAYERIMAGE_DEFAULT, "classic player", 0),
            new Buyable(PLAYERIMAGE_HAT_AND_SHOES, "shoes and hat", 1),
            new Buyable(PLAXERIMAGE_STICK, "stick figure", 1),
            new Buyable(PLAYERIMAGE_EGGMAN, "egg man", 2)};
    public final Shop that=this;

    private SliderLayout slideShow;

    //@// TODO: 5/27/2016 to low cohesion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        slideShow = (SliderLayout) findViewById(R.id.slider);
        slideShow.stopAutoCycle();
//@// TODO: 5/28/2016 open closed principle
        textSliders = new TextSliderView[buyables.length];
        for (int i = 0; i < buyables.length; i++) {
            final Buyable buyable = buyables[i];
            textSliders[i] = new TextSliderView(this);
            textSliders[i].image(buyable.getImage());
            final int finalI = i;
            textSliders[i].setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    popup(buyable);
                }
            });
            slideShow.addSlider(textSliders[i]);
        }
        setImageSubtitles();
//        TextSliderView textSliderView = new TextSliderView(this);
//        textSliderView.description("player image").image(PLAYERIMAGE_DEFAULT);
//        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                popup(PLAYERIMAGE_DEFAULT);
//            }
//        });
//
//        TextSliderView textSliderView1 = new TextSliderView(this);
//        textSliderView1.description("shoes and hat").image(PLAYERIMAGE_HAT_AND_SHOES);
//        textSliderView1.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//               popup(PLAYERIMAGE_HAT_AND_SHOES);
//            }
//        });
//
//        TextSliderView textSliderView2 = new TextSliderView(this);
//        textSliderView2.description("stick figure").image(PLAXERIMAGE_STICK);
//        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                popup(PLAXERIMAGE_STICK);
//            }
//        });
//
//        slideShow.addSlider(textSliderView);
//        slideShow.addSlider(textSliderView1);
//        slideShow.addSlider(textSliderView2);
    }

    private void setImageSubtitles() {
        String description = "";
        for (int i = 0; i < buyables.length; i++) {
            if (alreadyOwns(buyables[i].getImage())) {
                description = buyables[i].getDescription();
            } else {
                description = "unlock for " + buyables[i].getPrice() + " gems";
            }
            System.out.println(description);
            textSliders[i].description(description);
        }
    }

    @Override
    protected void onStop() {
        slideShow.stopAutoCycle();
        super.onStop();
    }

    public void popup(final Buyable buyable) {
        if (alreadyOwns(buyable.getImage()) || isDefaultImage(buyable.getImage())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setPlayerImage(buyable.getImage());
                    Snackbar.make(findViewById(R.id.slider), "Player set", Snackbar.LENGTH_LONG).show();
                }

            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setMessage(SELECT_THIS_PLAYER);
            builder.create().show();
        } else {
            buyPlayerImagePopup(buyable);
        }
    }

    private void buyPlayerImagePopup(final Buyable buyable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (buyImage(buyable)) {
                    setPlayerImage(buyable.getImage());
                    GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
                    gemFragment.updateGemText();
                    setImageSubtitles();
                    recreate();//@// TODO: 5/29/2016 bugfix in github
//                    findViewById(R.id.slider).requestLayout();
                    Snackbar.make(findViewById(R.id.slider), "Player bought and set", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(findViewById(R.id.slider), "Sorry, not enough gems", Snackbar.LENGTH_LONG).show();
                    getMoreGemsPopup();
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

    private void getMoreGemsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("get more gems", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(that, RecommendScreen.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(MORE_GEMS_NEEDED_DO_YOU_WANT);
        builder.create().show();
    }

    private boolean buyImage(Buyable buyable) {
        return PrefsHandler.buyPlayerImage(getSharedPreferences(MainActivity.GANME_PREFS, 0), buyable);
    }

    private boolean isDefaultImage(int playerImage) {
        return playerImage == PLAYERIMAGE_DEFAULT;
    }

    private boolean alreadyOwns(int playerImage) {
        if (playerImage == PLAYERIMAGE_DEFAULT) {
            return true;
        }
        return PrefsHandler.playerImageBought(getSharedPreferences(MainActivity.GANME_PREFS, 0), playerImage);
    }

    public void setPlayerImage(int playerImage) {
        PrefsHandler.setPlayerImage(getSharedPreferences(MainActivity.GANME_PREFS, 0), playerImage);
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }

    public void deletePurchasesClicked(View view) {
        PrefsHandler.deletePlayerImages(getSharedPreferences(MainActivity.GANME_PREFS, 0));
        recreate();
//        setImageSubtitles();
//        CustomSliderLayout sliderView = (CustomSliderLayout) findViewById(R.id.slider);
//        sliderView.invalidate(0, 0,sliderView.getWidth(), sliderView.getHeight() );
//        System.out.println("slider descr: "+sliderView.getCurrentSlider().getDescription());
    }

    public static float leftOfImage(int playerImgage) {
        if(playerImgage == PLAYERIMAGE_EGGMAN){
            return 0.05F;
        }
        //default
        return 0.3F;
    }

    public static float rightOfImage(int playerImgage) {
        if(playerImgage == PLAYERIMAGE_EGGMAN){
            return 0.05F;
        }
        //default
        return 0.2F;
    }
}
