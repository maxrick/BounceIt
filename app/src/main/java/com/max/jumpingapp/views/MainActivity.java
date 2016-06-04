package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.max.jumpingapp.R;
import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.tutorial.TutorialGamePanel;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.util.PrefsHandler;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String GANME_PREFS = "ganmePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String serialNumber = Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : id;
        System.out.println("id: "+id+"\nserial number: "+serialNumber);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean tutorial_mode = getIntent().getBooleanExtra(StartScreen.TUTORIAL_EXTRA, false);
        GamePanel gamePanel;
        int[] highScores = PrefsHandler.getThreeHighScores(getSharedPreferences(GANME_PREFS, 0));
        int playerImgage = PrefsHandler.getPlayerImage(getSharedPreferences(GANME_PREFS, 0));
        if(tutorial_mode){
            gamePanel = TutorialGamePanel.create(this,highScores, playerImgage, Shop.leftOfImage(playerImgage), Shop.rightOfImage(playerImgage));
        }else {
            gamePanel = GamePanel.create(this,highScores, playerImgage, Shop.leftOfImage(playerImgage), Shop.rightOfImage(playerImgage));
        }
        setContentView(gamePanel);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
      //      return true;
       // }

        return super.onOptionsItemSelected(item);
    }

    public void changeToDiedScreen(Score score){
        Runtime.getRuntime().gc();
        System.out.print("died");
        Intent intent = new Intent(this, DiedScreen.class);
        intent.putExtra("score", score.toInt());
        startActivity(intent);
        finish();
    }


}
