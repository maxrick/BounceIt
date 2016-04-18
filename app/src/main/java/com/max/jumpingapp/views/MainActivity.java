package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String HIGH_SCORE_PREFS = "highScorePrefs";
    public static final String HIGH_SCORES = "highScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String serialNumber = Build.SERIAL != Build.UNKNOWN ? Build.SERIAL : id;
        System.out.println("id: "+id+"\nserial number: "+serialNumber);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(GamePanel.create(this, getHighScores()));

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

    public int[] getHighScores(){
        try {
            System.out.println("highscores are read");
            SharedPreferences gameprefs = getSharedPreferences(HIGH_SCORE_PREFS, 0);
            Set<String> scoreSet = gameprefs.getStringSet(HIGH_SCORES, null);
            ArrayList<Score> scoreList = Score.toArrayList(scoreSet);
            int[] scoreNums = new int[scoreList.size()];
            for (int i = 0; i < scoreList.size(); i++) {
                scoreNums[i] = scoreList.get(i).toInt();
            }

//        int[] scoreNums = null;
//        if(scoreString.length() > 0) {
//            String[] scores = scoreString.split("\\|");
//            scoreNums = new int[scores.length];
//            for (int i = 0; i < scoreNums.length; i++) {
//                scoreNums[i] = Integer.valueOf(scores[i]);
//            }
//        }
            return scoreNums;
        }catch(NoScoresException e){
            return null;
        }

    }

}
