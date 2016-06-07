package com.max.jumpingapp.views;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.max.jumpingapp.R;
import com.max.jumpingapp.types.Score;
import com.max.jumpingapp.util.PrefsHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Highscores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        TextView scoreView = (TextView) findViewById(R.id.high_scores_list);
        placeHighscoresOn(scoreView);

    }

    private void placeHighscoresOn(TextView scoreView) {
        SharedPreferences gameprefs = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
        Set<String> scoreSet =gameprefs.getStringSet(PrefsHandler.HIGH_SCORES, null);
        try {
            ArrayList<Score> scoreList = Score.toArrayList(scoreSet);
            Collections.sort(scoreList);
            StringBuilder scoreBuild = new StringBuilder("");
            for(int i = 0; i<scoreList.size(); i++){
                scoreBuild.append(scoreList.get(i).nameAndValue()+"\n");
            }
            scoreView.setText(scoreBuild.toString());
        }catch (NoScoresException e){
            scoreView.setText("");
        }
    }

    public void buttonDeleteScoresClicked(View view){
        SharedPreferences gameprefs = getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
        SharedPreferences.Editor scoreEdit = gameprefs.edit();
        scoreEdit.clear();
        scoreEdit.apply();
        TextView scoreList = (TextView) findViewById(R.id.high_scores_list);
        placeHighscoresOn(scoreList);
        scoreList.invalidate();
    }
}
