package com.max.jumpingapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.max.jumpingapp.R;
import com.max.jumpingapp.util.MathHelper;
import com.max.jumpingapp.util.PrefsHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import layout.GemFragment;
import layout.HomeFragment;

public class RecommendScreen extends AppCompatActivity implements GemFragment.OnGemFragmentInteractionListener, HomeFragment.OnHomeFragmentInteractionListener {

    public static final int FIRST_MULTIPLICATOR = 89;
    public static final int ADD = 1451;
    public static final int SECOND_MULTIPLICATOR = 37;
    public static final int FIRST_MULTIPLICATOR_THANK_YOU = 53;
    public static final int ADD_THANK_YOU = 1733;
    public static final int SECOND_MULTIPLICATOR_THANK_YOU = 41;
    public static final String ACTIVATIONCODE_PREFIX = "1";
    public static final String THANKYOUCODE_PREFIX = "2";
    private boolean recommended = false;
    private boolean codeSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recommend_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recommended) {
            Snackbar.make(findViewById(R.id.recommendButton), R.string.reminder_to_send_activation_code, 8000).show();
        }
        if (codeSent) {
            codeSent = false;
            Snackbar.make(findViewById(R.id.recommendButton), R.string.message_wait_for_thank_you_code, 8000).show();
        }
    }

    public void buttonRecommendAppClicked(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommendation_text));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_via)));
        recommended = true;
    }

    public void buttonSendCodeClicked(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, generateActivationCode());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_via)));
        recommended = false;
        codeSent = true;
    }

    private String generateActivationCode() {
        String my_id = String.valueOf(PrefsHandler.getId(getSharedPreferences(PrefsHandler.GANME_PREFS, 0)));
        String date = RecommendScreen.currentDate();
        String mergedValues = my_id.substring(0, 1) + date.substring(0, 2) + my_id.substring(1, 2) + date.substring(2, 4) + my_id.substring(2, 3) +
                date.substring(4, 6) + my_id.substring(3, 4) + date.substring(6, 8) + my_id.substring(4, 5);
        long code = (Long.valueOf(mergedValues) * FIRST_MULTIPLICATOR + ADD) * SECOND_MULTIPLICATOR;
        int r = new Random().nextInt(10) + 1;
        code *= r;
        return ACTIVATIONCODE_PREFIX +code + "" + r;
    }

    public static String currentDate() {//@// TODO: 5/28/2016 does not belong here
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        return dateFormat.format(new Date());
    }

    public static String recommenderIdOf(String code) throws UnvalidRecommendationCode {
        try {
            long codeLong = Long.valueOf(code.substring(0, code.length() - 1));
            long randomizer = Integer.valueOf(code.substring(code.length() - 1, code.length()));
            long mergedValues = ((((codeLong / randomizer) / SECOND_MULTIPLICATOR) - ADD) / FIRST_MULTIPLICATOR);
            String date = "" + MathHelper.makeStringOfDigits(mergedValues, 10, new int[]{12, 11, 9, 8, 6, 5, 3, 2});
            if (date.equals(RecommendScreen.currentDate())) {
                return MathHelper.makeStringOfDigits(mergedValues, 10, new int[]{13, 10, 7, 4, 1});
            }


        } catch (Exception e) {

        }
        throw new UnvalidRecommendationCode();
    }

    public static String createThankYouCode(String recommenderId) {
        String date = RecommendScreen.currentDate();
        String mergedValues = recommenderId.substring(0, 1) + date.substring(0, 2) + recommenderId.substring(1, 2) + date.substring(2, 4) + recommenderId.substring(2, 3) +
                date.substring(4, 6) + recommenderId.substring(3, 4) + date.substring(6, 8) + recommenderId.substring(4, 5);
        long code = (Long.valueOf(mergedValues) * FIRST_MULTIPLICATOR_THANK_YOU + ADD_THANK_YOU) * SECOND_MULTIPLICATOR_THANK_YOU;
        int r = new Random().nextInt(9) + 1;
        code *= r;
        return THANKYOUCODE_PREFIX +code + "" + r;
    }

    public static String recommenderIdOfThankYou(String code) throws UnvalidRecommendationCode {
        try {
            long codeLong = Long.valueOf(code.substring(0, code.length() - 1));
            long randomizer = Integer.valueOf(code.substring(code.length() - 1, code.length()));
            long mergedValues = ((((codeLong / randomizer) / SECOND_MULTIPLICATOR_THANK_YOU) - ADD_THANK_YOU) / FIRST_MULTIPLICATOR_THANK_YOU);
            String date = "" + MathHelper.makeStringOfDigits(mergedValues, 10, new int[]{12, 11, 9, 8, 6, 5, 3, 2});
            if (date.equals(RecommendScreen.currentDate())) {
                return MathHelper.makeStringOfDigits(mergedValues, 10, new int[]{13, 10, 7, 4, 1});
            }


        } catch (Exception e) {

        }
        throw new UnvalidRecommendationCode();
    }

    @Override
    public void onGemFragmentInteraction(View view) {
        //do nothing
    }

    @Override
    public void onHomeFragmentInteraction(View view) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFragment");
        homeFragment.homeButtonClicked(view);
    }
}
