package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
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
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS,0);
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
        if(0 == PrefsHandler.getId(sharedPreferences)){
//            int seed = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID).hashCode();
            //new Random(seed)...
            int my_id = new Random().nextInt(90000)+10000; //range 10000..99999
            System.out.println("my uuid: "+my_id);
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



    public void buttonPlayClicked(View view){
        System.out.println("StartScreen -- buttonPlayClicked()");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(TUTORIAL_EXTRA, tutorialSelected());
        startActivity(intent);
    }

    private boolean tutorialSelected() {
        CheckBox checkBox =(CheckBox)findViewById(R.id.tutorial);
        boolean tutorialMode = checkBox.isChecked();
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.GANME_PREFS, 0);
        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
        prefEdit.putBoolean(TUTORIAL_MODE, tutorialMode);
        prefEdit.apply();
        return tutorialMode;
    }

    public void buttonHighScoresClicked(View view){
        Intent intent = new Intent(this, Highscores.class);
        startActivity(intent);
    }

    public void buttonRecommendAppClicked(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi\nThis app \"bounce it\" is amazing. You should get it");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.recommend_to)));
    }

    public void buttonShopClicked(View view){
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void buttonActivateRecommendationCodeClicked(View view){
        String code = ((EditText) findViewById(R.id.recommendationCode)).getText().toString();
        try {
            String recommenderId=RecommendScreen.recommenderIdOf(code);
//            if(Integer.valueOf(recommenderId) != PrefsHandler.getId(getSharedPreferences(MainActivity.GANME_PREFS, 0))){
                PrefsHandler.addGem(getSharedPreferences(MainActivity.GANME_PREFS,0));
                ((GemFragment)getSupportFragmentManager().findFragmentByTag("gemFragment")).updateGemText();
                String thankYouCode = RecommendScreen.createThankYouCode(recommenderId);
                //@// TODO: 5/28/2016 send code
            //@// TODO: 5/28/2016 hide textfield 
//            }
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {
            unvalidRecommendationCode.printStackTrace();
        }
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        gemFragment.gemButtonClicked(view);
    }
}
