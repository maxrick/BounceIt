package com.max.jumpingapp.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.tutorial.TutorialGamePanel;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.util.PrefsHandler;

import layout.ShopFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        String serialNumber = Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : id;

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean tutorial_mode = getIntent().getBooleanExtra(StartScreen.TUTORIAL_EXTRA, false);
        GamePanel gamePanel;
        int[] highScores = PrefsHandler.getThreeHighScores(getSharedPreferences(PrefsHandler.GANME_PREFS, 0));
        int playerImgage = PrefsHandler.getPlayerImage(getSharedPreferences(PrefsHandler.GANME_PREFS, ShopFragment.PLAYERIMAGE_HAT_AND_SHOES));
        if(tutorial_mode){
            gamePanel = TutorialGamePanel.create(this,highScores, playerImgage, ShopFragment.leftOfImage(playerImgage), ShopFragment.rightOfImage(playerImgage));
        }else {
            gamePanel = GamePanel.create(this,highScores, playerImgage, ShopFragment.leftOfImage(playerImgage), ShopFragment.rightOfImage(playerImgage));
        }
        setContentView(gamePanel);

    }

    public void changeToDiedScreen(Score score){
        Runtime.getRuntime().gc();
        Intent intent = new Intent(this, DiedScreen.class);
        intent.putExtra("score", score.toInt());
        startActivity(intent);
        finish();
    }


}
