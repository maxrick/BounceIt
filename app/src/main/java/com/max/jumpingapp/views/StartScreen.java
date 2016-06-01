package com.max.jumpingapp.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.max.jumpingapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_screen);
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        checkBox.setChecked(tutorialSharedPref());
    }

    private boolean tutorialSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS, 0);
        boolean tutorialMode = sharedPreferences.getBoolean(TUTORIAL_MODE, true);
        return tutorialMode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.updateGemText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        generateIdOnFirstRun();
        MenuItem shareItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShare = new ShareActionProvider(this);
        MenuItemCompat.setActionProvider(shareItem, mShare);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "bounce it is awesome");

        mShare.setShareIntent(shareIntent);

        return true;
    }

    private void generateIdOnFirstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS, 0);
        if (0 == PrefsHandler.getId(sharedPreferences)) {
//            int seed = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID).hashCode();
            //new Random(seed)...
            int my_id = new Random().nextInt(90000) + 10000; //range 10000..99999
            System.out.println("my uuid: " + my_id);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PrefsHandler.MY_UUID, my_id);
            editor.apply();
        }
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */


    public void buttonPlayClicked(View view) {
        System.out.println("StartScreen -- buttonPlayClicked()");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(TUTORIAL_EXTRA, tutorialSelected());
        startActivity(intent);
    }

    private boolean tutorialSelected() {
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        boolean tutorialMode = checkBox.isChecked();
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS, 0);
        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
        prefEdit.putBoolean(TUTORIAL_MODE, tutorialMode);
        prefEdit.apply();
        return tutorialMode;
    }

    public void buttonHighScoresClicked(View view) {
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);
    }

    public void buttonRecommendAppClicked(View view) {//@// TODO: 6/2/2016 rename
//        send("Hi\nThis app \"bounce it\" is amazing. You should get it");
        Intent intent = new Intent(this, RecommendScreen.class);
        startActivity(intent);
    }

    public void buttonShopClicked(View view) {
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void buttonActivateRecommendationCodeClicked(View view) {
        String code = ((EditText) findViewById(R.id.recommendationCode)).getText().toString();
        try {
            String recommenderId = RecommendScreen.recommenderIdOf(code);
//            if(Integer.valueOf(recommenderId) != PrefsHandler.getId(getSharedPreferences(MainActivity.GANME_PREFS, 0))){
            SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.GANME_PREFS, 0);
            if(!PrefsHandler.alreadyUsed(sharedPrefs, code)){
                PrefsHandler.addGem(sharedPrefs);
                ((GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment")).updateGemText();
                PrefsHandler.invalidate(sharedPrefs, code);
                String thankYouCode = RecommendScreen.createThankYouCode(recommenderId);
                popupThankYouCode(thankYouCode, view.getContext());
            }else {
                Snackbar.make(view, "Sorry, this code has already been used", Snackbar.LENGTH_LONG).show();
            }


            //@// TODO: 5/28/2016 send code
            //@// TODO: 5/28/2016 hide textfield
//            }
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {
            Snackbar.make(view, "Sorry, this code is not valid", Snackbar.LENGTH_LONG).show();
        }
    }

    private void popupThankYouCode(final String thankYouCode, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Congratulations, you received a free gem. Thank your friend by giving him a code for a free gem also.\n\nThis code is only valid for the person who sent you your voucher code.");
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
        builder.setMessage("Are you sure you don't want to send your friend the free gem? After cancelling you won't have the possibility to send the code again.");
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
