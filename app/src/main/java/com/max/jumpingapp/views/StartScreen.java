package com.max.jumpingapp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.max.jumpingapp.R;
import com.max.jumpingapp.util.Constants;
import com.max.jumpingapp.util.PrefsHandler;

import java.util.Random;

import layout.GemFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartScreen extends AppCompatActivity implements GemFragment.OnGemFragmentInteractionListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    public static final String TUTORIAL_EXTRA = "tutorial";
    public static final String TUTORIAL_MODE = "tutorialMode";

    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    private SliderLayout slideShow;
    public TextSliderView[] textSliders;
    public Buyable[] buyables = {
            new Buyable(Shop.PLAYERIMAGE_DEFAULT, Constants.PLAYERNAME_CLASSIC, 0),
            new Buyable(Shop.PLAYERIMAGE_HAT_AND_SHOES, Constants.PLAYERNAME_SHOES_AND_HAT, 1),
            new Buyable(Shop.PLAXERIMAGE_STICK, Constants.PLAYERNAME_STICK_FIGURE, 1),
            new Buyable(Shop.PLAYERIMAGE_EGGMAN, Constants.PLAYERNAME_EGGMAN, 2)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        generateIdOnFirstRun();
        setContentView(R.layout.activity_start_screen);
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        checkBox.setChecked(tutorialSharedPref());
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
                }
            });
            slideShow.addSlider(textSliders[i]);
        }
    }

    @Override
    protected void onRestart() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        checkBox.setChecked(tutorialSharedPref());
        super.onRestart();
    }

    private boolean tutorialSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
        boolean tutorialMode = sharedPreferences.getBoolean(TUTORIAL_MODE, true);
        return tutorialMode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.updateGemText();
    }

    private void generateIdOnFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
        if (0 == PrefsHandler.getId(sharedPreferences)) {
//            int seed = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID).hashCode();
            //new Random(seed)...
            int my_id = new Random().nextInt(90000) + 10000; //range 10000..99999
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PrefsHandler.MY_UUID, my_id);
            editor.apply();
        }
    }

    public void buttonPlayClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(TUTORIAL_EXTRA, tutorialSelected());
        startActivity(intent);
    }

    private boolean tutorialSelected() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        boolean tutorialMode = checkBox.isChecked();
        SharedPreferences sharedPreferences = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
        prefEdit.putBoolean(TUTORIAL_MODE, false);
        prefEdit.apply();
        return tutorialMode;
    }

    public void buttonHighScoresClicked(View view) {
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);
    }

    public void buttonRecommendAppClicked(View view) {//@// TODO: 6/2/2016 rename
        Intent intent = new Intent(this, HelpPage.class);
        startActivity(intent);
    }

    public void buttonShopClicked(View view) {
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void buttonActivateRecommendationCodeClicked(View view) {
        String fullCode = ((EditText) findViewById(R.id.recommendationCode)).getText().toString();
        if ((fullCode.length() > 0)) {
            String prefix = fullCode.substring(0, 1);
            String code = fullCode.substring(1, fullCode.length());
            if (prefix.equals(RecommendScreen.ACTIVATIONCODE_PREFIX)) {
                if (PrefsHandler.activationCodeUsed(getSharedPreferences(PrefsHandler.GANME_PREFS, 0))) {
                    Snackbar.make(view, R.string.sorry_code_for_new_users, Snackbar.LENGTH_LONG).show();
                } else {
                    activateRecommendationCode(view, code);
                    return;
                }
            }
            if (prefix.equals(RecommendScreen.THANKYOUCODE_PREFIX)) {
                activateThankYouCode(view, code);
                return;
            }
        }
        //default
        Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG).show();
        return;
    }

    private void activateRecommendationCode(View view, String code) {
        try {
            String recommenderId = RecommendScreen.recommenderIdOf(code);
            if (Integer.valueOf(recommenderId) != PrefsHandler.getId(getSharedPreferences(PrefsHandler.GANME_PREFS, 0))) {
                SharedPreferences sharedPrefs = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
                if (!PrefsHandler.alreadyUsed(sharedPrefs, code)) {
                    PrefsHandler.addGem(sharedPrefs);
                    PrefsHandler.activationUsed(sharedPrefs);
                    ((GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment")).updateGemText();
                    PrefsHandler.invalidate(sharedPrefs, code);
                    String thankYouCode = RecommendScreen.createThankYouCode(recommenderId);
                    popupThankYouCode(thankYouCode, view.getContext());
                } else {
                    Snackbar.make(view, R.string.sorry_code_used, Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(view, R.string.sorry_code_only_for_others, Snackbar.LENGTH_LONG).show();
            }
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {
            Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG).show();
        }
    }

    private void activateThankYouCode(View view, String code) {
        try {
            String recommenderId = RecommendScreen.recommenderIdOfThankYou(code);
            SharedPreferences sharedPreferences = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
            if (Integer.valueOf(recommenderId) == PrefsHandler.getId(sharedPreferences)) {
                if (!PrefsHandler.alreadyUsed(sharedPreferences, code)) {
                    PrefsHandler.addGem(sharedPreferences);
                    PrefsHandler.invalidate(sharedPreferences, code);
                    ((GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment")).updateGemText();
                    Snackbar.make(view, R.string.congrats_free_gem, Snackbar.LENGTH_LONG).show();
                    return;
                }
                Snackbar.make(view, R.string.sorry_code_used, Snackbar.LENGTH_LONG).show();
                return;
            }
            Snackbar.make(view, R.string.sorry_code_only_for_others, Snackbar.LENGTH_LONG).show();
            return;
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {

        }
        Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG).show();//also invalid if recommender and thisid is not the same
    }

    private void popupThankYouCode(final String thankYouCode, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.congrats_free_gem) + getString(R.string.also_thank_your_friend));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                send(thankYouCode);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popupReallyCancel(thankYouCode, context);
            }
        });
        builder.create().show();
    }

    private void popupReallyCancel(final String thankYouCode, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.sure_no_thank_you_code_question));
        builder.setPositiveButton(R.string.send_thank_you_code_after_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                send(thankYouCode);
            }
        });
        builder.setNegativeButton(R.string.definately_no_thank_you_code, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void send(String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_via)));
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }
}
