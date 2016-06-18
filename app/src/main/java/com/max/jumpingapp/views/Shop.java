package com.max.jumpingapp.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.R;
import com.max.jumpingapp.util.PrefsHandler;

import layout.GemFragment;
import layout.HomeFragment;

public class Shop extends AppCompatActivity implements GemFragment.OnGemFragmentInteractionListener, HomeFragment.OnHomeFragmentInteractionListener{

    public static final int PLAYERIMAGE_HAT_AND_SHOES = R.drawable.playerimage_hat_and_shoes;
    public static final int PLAXERIMAGE_STICK = R.drawable.playerimage_stickfigure;
    public static final int PLAYERIMAGE_DEFAULT = R.drawable.playerimage;
    public static final int PLAYERIMAGE_EGGMAN  = R.drawable.eggman;
    public TextSliderView[] textSliders;
    public Buyable[] buyables = {
            new Buyable(PLAYERIMAGE_DEFAULT, Constants.PLAYERNAME_CLASSIC, 0),
            new Buyable(PLAYERIMAGE_HAT_AND_SHOES, Constants.PLAYERNAME_SHOES_AND_HAT, 1),
            new Buyable(PLAXERIMAGE_STICK, Constants.PLAYERNAME_STICK_FIGURE, 1),
            new Buyable(PLAYERIMAGE_EGGMAN, Constants.PLAYERNAME_EGGMAN, 2)};
    public final Shop that=this;

    private SliderLayout slideShow;

    //@// TODO: 5/27/2016 to low cohesion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
    }

    private void setImageSubtitles() {
        String description = "";
        for (int i = 0; i < buyables.length; i++) {
            if (alreadyOwns(buyables[i].getImage())) {
                description = buyables[i].getDescription();
            } else {
                description = getString(R.string.unlock_for)+ " " + buyables[i].getPrice() +" "+ ((buyables[i].getPrice()>1) ? getString(R.string.gems): getString(R.string.gem));
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
                    Snackbar.make(findViewById(R.id.slider), R.string.player_set, Snackbar.LENGTH_LONG).show();
                }

            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setMessage(getString(R.string.Select_this_player_question));
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
                    Snackbar.make(findViewById(R.id.slider), R.string.player_bought_and_set, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(findViewById(R.id.slider), getString(R.string.sorry_not_enough)+" "+getString(R.string.gems), Snackbar.LENGTH_LONG).show();
                    getMoreGemsPopup();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage(getString(R.string.Do_you_want_to_buy_this_player)+buyable.getPrice()+" "+
                ((buyable.getPrice()>1) ? getString(R.string.gems): getString(R.string.gem)));
        builder.create().show();
    }

    private void getMoreGemsPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.get_more)+" "+getString(R.string.gems), new DialogInterface.OnClickListener() {
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
        builder.setMessage(getString(R.string.You_need_more_gems_do_you_want_to));
        builder.create().show();
    }

    private boolean buyImage(Buyable buyable) {
        return PrefsHandler.buyPlayerImage(getSharedPreferences(PrefsHandler.GANME_PREFS, 0), buyable);
    }

    private boolean isDefaultImage(int playerImage) {
        return playerImage == PLAYERIMAGE_DEFAULT;
    }

    private boolean alreadyOwns(int playerImage) {
        if (playerImage == PLAYERIMAGE_DEFAULT) {
            return true;
        }
        return PrefsHandler.playerImageBought(getSharedPreferences(PrefsHandler.GANME_PREFS, 0), playerImage);
    }

    public void setPlayerImage(int playerImage) {
        PrefsHandler.setPlayerImage(getSharedPreferences(PrefsHandler.GANME_PREFS, 0), playerImage);
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }

    public void deletePurchasesClicked(View view) {
        PrefsHandler.deletePlayerImages(getSharedPreferences(PrefsHandler.GANME_PREFS, 0));
        recreate();
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

    @Override
    public void onHomeFragmentInteraction(View view) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        homeFragment.homeButtonClicked(view);
    }
}
