package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.max.jumpingapp.game.GamePanel;
import com.max.jumpingapp.types.Score;

public class MainActivity extends AppCompatActivity {

    public static final String HIGH_SCORE_PREFS = "highScorePrefs";
    public static final String HIGH_SCORES = "highScores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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
        setHighScore(score);
        Runtime.getRuntime().gc();
        System.out.print("died");
        Intent intent = new Intent(this, DiedScreen.class);
        intent.putExtra("score", score.toInt());
        startActivity(intent);
        finish();
    }

    public int[] getHighScores(){
        SharedPreferences gameprefs = getSharedPreferences(HIGH_SCORE_PREFS, 0);
        String scoreString =gameprefs.getString(HIGH_SCORES, "");
        int[] scoreNums = null;
        if(scoreString.length() > 0) {
            String[] scores = scoreString.split("\\|");
            scoreNums = new int[scores.length];
            for (int i = 0; i < scoreNums.length; i++) {
                scoreNums[i] = Integer.valueOf(scores[i]);
            }
        }
        return scoreNums;
    }

    private void setHighScore(Score score) {
        SharedPreferences gameprefs = getSharedPreferences(HIGH_SCORE_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        String scores = gameprefs.getString(HIGH_SCORES, "");
        String[] newScores;
        if(scores.length()>0){
            String[] exScores = scores.split("\\|");
            newScores = new String[Math.min(exScores.length+1, 10)];
            int move = 0;
            for(int i = 0; i<exScores.length; i++){
                int scoreNum = Integer.valueOf(exScores[i]);//@// TODO: 4/9/2016 check if cast works
                if(score.betterThan(scoreNum)){
                    newScores[i] = score.valueAsString();
                    move = 1;
                    score = new Score(0);
                }
                if(i+move < 10){
                    newScores[i+move] = String.valueOf(scoreNum);
                }
            }
            if(exScores.length < newScores.length && move == 0){
                newScores[newScores.length-1] = score.valueAsString();
            }
        }else{
            newScores = new String[1];
            newScores[0] = score.valueAsString();
        }
        String scoreString = TextUtils.join("|", newScores);
        System.out.println("highscores: "+scoreString);
        scoreEdit.putString(HIGH_SCORES, scoreString);
        scoreEdit.apply();
    }
}
