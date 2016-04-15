package com.max.jumpingapp.views;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.max.jumpingapp.R;
import com.max.jumpingapp.types.Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Highscores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        TextView scoreView = (TextView) findViewById(R.id.high_scores_list);
//        SharedPreferences scorePrefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
//        String[] savedScores = scorePrefs.getString(MainActivity.HIGH_SCORES, "").split("\\|");
//        StringBuilder scoreBuild = new StringBuilder("");
//        for(String score : savedScores){
//            scoreBuild.append(score+"\n");
//        }
        SharedPreferences gameprefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        Set<String> scoreSet =gameprefs.getStringSet(MainActivity.HIGH_SCORES, null);
        try {
            ArrayList<Score> scoreList = Score.toArrayList(scoreSet);
            Collections.sort(scoreList);
            StringBuilder scoreBuild = new StringBuilder("");
            for(int i = 0; i<scoreList.size(); i++){
                scoreBuild.append(scoreList.get(i).nameAndValue()+"\n");
            }
            scoreView.setText(scoreBuild.toString());
        }catch (NoScoresException e){

        }

    }

    public void buttonDeleteScoresClicked(View view){
        SharedPreferences gameprefs = getSharedPreferences(MainActivity.HIGH_SCORE_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        scoreEdit.clear();
//        scoreEdit.putString(MainActivity.HIGH_SCORES, "");
        scoreEdit.apply();
    }
}
