package com.max.jumpingapp.util;

import android.content.SharedPreferences;

import com.max.jumpingapp.R;
import com.max.jumpingapp.types.Score;//// TODO: 5/27/2016 not good
import com.max.jumpingapp.views.NoScoresException;
import com.max.jumpingapp.views.Shop;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by max on 5/27/2016.
 */
public class PrefsHandler {

    public static final String HIGH_SCORES = "highScores";
    public static final String GEMS = "gems";

    public static int[] getHighScores(SharedPreferences sharedPreferences){
        try {
            System.out.println("highscores are read");
            Set<String> scoreSet = sharedPreferences.getStringSet(HIGH_SCORES, null);
            ArrayList<Score> scoreList = Score.toArrayList(scoreSet);
            int[] scoreNums = new int[scoreList.size()];
            for (int i = 0; i < scoreList.size(); i++) {
                scoreNums[i] = scoreList.get(i).toInt();
            }
            return scoreNums;
        }catch(NoScoresException e){
            return null;
        }
    }

    public static int getPlayerImage(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(Shop.PLAYER_IMAGE, R.drawable.playerimage);
    }

    public static int getGems(SharedPreferences sharedPreferences){
        return sharedPreferences.getInt(GEMS, 0);
    }
}
