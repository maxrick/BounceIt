package com.max.jumpingapp.util;

import android.content.SharedPreferences;

import com.max.jumpingapp.R;
import com.max.jumpingapp.types.Score;//// TODO: 5/27/2016 not good
import com.max.jumpingapp.views.MainActivity;
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
    public static final String PLAYER_IMAGE = "player_image";
    public static final String BOUGHT_IMAGES = "bought_images";

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
        return sharedPreferences.getInt(PLAYER_IMAGE, R.drawable.playerimage);
    }

    public static int getGems(SharedPreferences sharedPreferences){
        return sharedPreferences.getInt(GEMS, 0);
    }

    public static void addGem(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        int value = getGems(sharedPreferences) + 1;
        edit.putInt(GEMS, value);
        edit.apply();
    }

    public static void resetGems(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GEMS, 0);
        editor.apply();
    }

    public static boolean playerImageBought(SharedPreferences sharedPreferences, int playerImage) {
        String boughtImages= sharedPreferences.getString(BOUGHT_IMAGES, "");
        return boughtImages.contains(String.valueOf(playerImage));
    }

    public static void setPlayerImage(SharedPreferences sharedPreferences, int playerImage) {
        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
        prefEdit.putInt(PLAYER_IMAGE, playerImage);
        prefEdit.apply();
    }

    private static void addBoughtPlayerImage(SharedPreferences sharedPreferences, int playerImage) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String alreadyBoughtPlayerImages = sharedPreferences.getString(BOUGHT_IMAGES, "");
        alreadyBoughtPlayerImages += (String.valueOf(playerImage)+";");
        edit.putString(BOUGHT_IMAGES, alreadyBoughtPlayerImages);
        edit.apply();
    }

    public synchronized static boolean buyPlayerImage(SharedPreferences sharedPreferences, int playerImage) {
        if(getGems(sharedPreferences)>0){
            reduceGemsBy(sharedPreferences, 1);
            addBoughtPlayerImage(sharedPreferences, playerImage);
            return true;
        }
        return false;
    }

    private static void reduceGemsBy(SharedPreferences sharedPreferences, int i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GEMS, getGems(sharedPreferences) - 1);
        editor.apply();
    }

    public static void deletePlayerImages(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOUGHT_IMAGES, "");
        editor.apply();
    }
}
