package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.max.jumpingapp.R;
import com.max.jumpingapp.util.CodeChecker;
import com.max.jumpingapp.util.PrefsHandler;

import java.util.Random;

import layout.GemFragment;
import layout.ShopFragment;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        generateIdOnFirstRun();
        setContentView(R.layout.activity_start_screen);
        CheckBox checkBox = (CheckBox) findViewById(R.id.tutorial);
        checkBox.setChecked(tutorialSharedPref());
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
        if (((ShopFragment) getSupportFragmentManager().findFragmentByTag("shopFragment")).currentPlayerCanBeSet()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(TUTORIAL_EXTRA, tutorialSelected());
            startActivity(intent);
        }
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

    public void helpClicked(View view) {
        Intent intent = new Intent(this, HelpPage.class);
        startActivity(intent);
    }

    public void buttonActivateRecommendationCodeClicked(View view) {
        String fullCode = ((EditText) findViewById(R.id.recommendationCode)).getText().toString();
        GemFragment gemFragment = (GemFragment) getSupportFragmentManager().findFragmentByTag("gemFragment");
        CodeChecker codeChecker= new CodeChecker(this, gemFragment);
        codeChecker.buttonActivateRecommendationCodeClicked(view, fullCode);
    }

    public void send(String message) {
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
