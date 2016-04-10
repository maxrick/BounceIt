package com.max.jumpingapp.views;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.max.jumpingapp.R;

public class Highscores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        TextView scoreView = (TextView) findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        String[] savedScores = scorePrefs.getString(MainActivity.HIGH_SCORES, "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        scoreView.setText(scoreBuild.toString());
    }

    public void buttonDeleteScoresClicked(View view){
        SharedPreferences gameprefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        scoreEdit.putString(MainActivity.HIGH_SCORES, "");
        scoreEdit.commit();
    }
}
