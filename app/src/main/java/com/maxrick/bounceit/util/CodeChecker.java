package com.maxrick.bounceit.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.max.jumpingapp.R;
import com.maxrick.bounceit.views.RecommendScreen;
import com.maxrick.bounceit.views.StartScreen;
import com.maxrick.bounceit.views.UnvalidRecommendationCode;

import layout.GemFragment;

/**
 * Created by max on 7/10/2016.
 */
public class CodeChecker {
    private final StartScreen startScreen;
    private GemFragment gemFragment;

    public CodeChecker(StartScreen startScreen, GemFragment gemFragment){
        this.startScreen = startScreen;
        this.gemFragment = gemFragment;
    }
    public void buttonActivateRecommendationCodeClicked(View view, String fullCode) {
        if ((fullCode.length() > 0)) {
            String prefix = fullCode.substring(0, 1);
            String code = fullCode.substring(1, fullCode.length());
            if (prefix.equals(RecommendScreen.ACTIVATIONCODE_PREFIX)) {
                if (PrefsHandler.activationCodeUsed(startScreen.getSharedPreferences(PrefsHandler.GANME_PREFS, 0))) {
                    Snackbar.make(view, R.string.sorry_code_for_new_users, Snackbar.LENGTH_LONG).show();
                } else {
                    activateRecommendationCode(view, code);
                    return;
                }
            }
            if (prefix.equals(RecommendScreen.THANKYOUCODE_PREFIX)) {
                activateThankYouCode(view, code);
                return;
            }
        }
        //default
        Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG)
                .setAction("Get Code", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(startScreen, RecommendScreen.class);
                        startScreen.startActivity(intent);
                    }
                }).show();
        return;
    }

    private void activateRecommendationCode(View view, String code) {
        try {
            String recommenderId = RecommendScreen.recommenderIdOf(code);
            if (Integer.valueOf(recommenderId) != PrefsHandler.getId(startScreen.getSharedPreferences(PrefsHandler.GANME_PREFS, 0))) {
                SharedPreferences sharedPrefs = startScreen.getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
                if (!PrefsHandler.alreadyUsed(sharedPrefs, code)) {
                    PrefsHandler.addGem(sharedPrefs);
                    PrefsHandler.activationUsed(sharedPrefs);
                    gemFragment.updateGemText();
                    PrefsHandler.invalidate(sharedPrefs, code);
                    String thankYouCode = RecommendScreen.createThankYouCode(recommenderId);
                    popupThankYouCode(thankYouCode, view.getContext());
                } else {
                    Snackbar.make(view, R.string.sorry_code_used, Snackbar.LENGTH_LONG).show();
                }
            } else {
                Snackbar.make(view, R.string.sorry_code_only_for_others, Snackbar.LENGTH_LONG).show();
            }
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {
            Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG).show();
        }
    }

    private void activateThankYouCode(View view, String code) {
        try {
            String recommenderId = RecommendScreen.recommenderIdOfThankYou(code);
            SharedPreferences sharedPreferences = startScreen.getSharedPreferences(PrefsHandler.GANME_PREFS, 0);
            if (Integer.valueOf(recommenderId) == PrefsHandler.getId(sharedPreferences)) {
                if (!PrefsHandler.alreadyUsed(sharedPreferences, code)) {
                    PrefsHandler.addGem(sharedPreferences);
                    PrefsHandler.invalidate(sharedPreferences, code);
                    gemFragment.updateGemText();
                    Snackbar.make(view, R.string.congrats_free_gem, Snackbar.LENGTH_LONG).show();
                    return;
                }
                Snackbar.make(view, R.string.sorry_code_used, Snackbar.LENGTH_LONG).show();
                return;
            }
            Snackbar.make(view, R.string.sorry_code_only_for_others, Snackbar.LENGTH_LONG).show();
            return;
        } catch (UnvalidRecommendationCode unvalidRecommendationCode) {

        }
        Snackbar.make(view, R.string.sorry_code_not_valid, Snackbar.LENGTH_LONG).show();//also invalid if recommender and thisid is not the same
    }

    private void popupThankYouCode(final String thankYouCode, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.congrats_free_gem) + context.getString(R.string.also_thank_your_friend));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startScreen.send(thankYouCode);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                popupReallyCancel(thankYouCode, context);
            }
        });
        builder.create().show();
    }

    private void popupReallyCancel(final String thankYouCode, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.sure_no_thank_you_code_question));
        builder.setPositiveButton(R.string.send_thank_you_code_after_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startScreen.send(thankYouCode);
            }
        });
        builder.setNegativeButton(R.string.definately_no_thank_you_code, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
}
