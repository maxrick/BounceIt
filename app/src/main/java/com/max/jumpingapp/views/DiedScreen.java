package com.max.jumpingapp.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.max.jumpingapp.R;
import com.max.jumpingapp.views.MainActivity;

public class DiedScreen extends AppCompatActivity {
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_died_screen);
        scoreTextView = (TextView) findViewById(R.id.scoreView);
        Intent myIntent = getIntent();
        int score = myIntent.getIntExtra("score", 0);
        scoreTextView.setText(String.valueOf(score));
    }

    public void buttonPlayClicked(View view){
        System.out.println("StartScreen -- buttonPlayClicked()");
        Runtime.getRuntime().gc();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
