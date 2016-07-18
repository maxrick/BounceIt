package com.maxrick.bounceit.util;

import android.content.SharedPreferences;

import com.max.jumpingapp.R;
import com.maxrick.bounceit.types.Score;
import com.maxrick.bounceit.views.Buyable;
import com.maxrick.bounceit.views.NoScoresException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by max on 5/27/2016.
 */
public class PrefsHandler {

    public static final String GANME_PREFS = "ganmePrefs";
    public static final String HIGH_SCORES = "highScores";
    public static final String GEMS = "gems";
    public static final String PLAYER_IMAGE = "player_image";
    public static final String BOUGHT_IMAGES = "bought_images";
    public static final String MY_UUID = "my_UUID";
    public static final String USED_RECOMMENDATION_CODES = "used_recommendation_codes";
    public static final String ACTIVATION_USED = "activation_used";

    private static int[] getHighScores(SharedPreferences sharedPreferences){
        try {
            Set<String> scoreSet = sharedPreferences.getStringSet(HIGH_SCORES, null);
            ArrayList<Score> scoreList = Score.toArrayList(scoreSet);
            Collections.sort(scoreList);
            int[] scoreNums = new int[scoreList.size()];
            for (int i = 0; i < scoreList.size(); i++) {
                scoreNums[i] = scoreList.get(i).toInt();
            }
            return scoreNums;
        }catch(NoScoresException e){
            return null;
        }
    }

    public static int[] getThreeHighScores(SharedPreferences sharedPreferences){
        int[] allScores = getHighScores(sharedPreferences);
        int resultSize = allScores !=null  ? allScores.length : 0;
        if (resultSize > 3) {
            resultSize=3;
        }
        int[] scoreList = new int[resultSize];
        for(int i=0; i < resultSize && i<3; i++){
            scoreList[i] = allScores[i];
        }
        return scoreList;
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

    public synchronized static boolean buyPlayerImage(SharedPreferences sharedPreferences, Buyable buyable) {
        if(getGems(sharedPreferences)>=buyable.getPrice()){
            reduceGemsBy(sharedPreferences, buyable.getPrice());
            addBoughtPlayerImage(sharedPreferences, buyable.getImage());
            return true;
        }
        return false;
    }

    private static void reduceGemsBy(SharedPreferences sharedPreferences, int i) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(GEMS, getGems(sharedPreferences) - i);
        editor.apply();
    }

    public static void deletePlayerImages(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BOUGHT_IMAGES, "");
        editor.apply();
    }

    public static int getId(SharedPreferences sharedPreferences) {
        int myId = sharedPreferences.getInt(MY_UUID, 0);
        return myId;
    }

    public static void invalidate(SharedPreferences sharedPrefs, String code) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String oldCodes = sharedPrefs.getString(USED_RECOMMENDATION_CODES, "");
        String updatedCodes = oldCodes + code + ";";
        editor.putString(USED_RECOMMENDATION_CODES, updatedCodes);
        editor.apply();
    }

    public static boolean alreadyUsed(SharedPreferences sharedPrefs, String code) {
        String oldCodes = sharedPrefs.getString(USED_RECOMMENDATION_CODES, "");
        return  oldCodes.contains(code);
    }

    public static boolean activationCodeUsed(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean(ACTIVATION_USED, false);
    }

    public static void activationUsed(SharedPreferences sharedPrefs) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(ACTIVATION_USED, true);
        editor.apply();
    }
}
