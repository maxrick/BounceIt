package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.max.jumpingapp.R;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.views.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class DiedScreen extends AppCompatActivity {

    public static final String PLAYER_NAME = "PlayerName";
    private Score score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_died_screen);
        TextView scoreTextView = (TextView) findViewById(R.id.scoreView);
        EditText nameEditText = (EditText) findViewById(R.id.nameEdit);
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);

        Intent myIntent = getIntent();
        score = new Score(Integer.valueOf(myIntent.getIntExtra("score", 0)));
        scoreTextView.setText(score.toInt()+ "");

        Set<String> scores = sharedPreferences.getStringSet(MainActivity.HIGH_SCORES, null);
        ArrayList scoreList = new ArrayList();
        try {
            scoreList = Score.toArrayList(scores);
        }catch (NoScoresException e){
        }
        if(score.isHighscore(scoreList)){
            String playerName = sharedPreferences.getString(PLAYER_NAME, "");
            if(playerName.length() >0){
                nameEditText.setText(playerName);
            }
            TextView diedMessage = (TextView) findViewById(R.id.diedMessageTextView);
            diedMessage.setText("Well, not good but a high score");
        }else{
            nameEditText.setVisibility(View.INVISIBLE);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    public void buttonPlayClicked(View view){
        System.out.println("StartScreen -- buttonPlayClicked()");
        Runtime.getRuntime().gc();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EditText nameEditText = (EditText) findViewById(R.id.nameEdit);
        setHighScore(new Score(score.toInt(), nameEditText.getText().toString()));
        setPlayerName(nameEditText.getText().toString());
    }

    private void setPlayerName(String name) {
        SharedPreferences gameprefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        scoreEdit.putString(PLAYER_NAME, name);
        scoreEdit.apply();
    }

    private void setHighScore(Score score) {
        SharedPreferences gameprefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        Set<String> scores = gameprefs.getStringSet(MainActivity.HIGH_SCORES, null);
        ArrayList scoreList = new ArrayList();
        try {
            scoreList = Score.toArrayList(scores);
        }catch (NoScoresException e){
        }

        scoreList.add(score);
        Collections.sort(scoreList);
        scoreList = Score.firstTenOf(scoreList);
        scores = Score.toSet(scoreList);
        scoreEdit.putStringSet(MainActivity.HIGH_SCORES, scores);
        scoreEdit.apply();
    }
}
