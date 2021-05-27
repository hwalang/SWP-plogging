package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends Activity {
    ImageView logo,appName, splash;
    LottieAnimationView lottieAnimationView;
    private MotionEvent event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);
        Handler handler = new Handler(Looper.myLooper());
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.team3);
        splash = findViewById(R.id.introduce_img);
        lottieAnimationView = findViewById(R.id.lottie);


        splash.animate().translationY((-3500)).setDuration(1000).setStartDelay(3000);
        logo.animate().translationY((3000)).setDuration(1000).setStartDelay(3000);
        appName.animate().translationY((3000)).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY((3000)).setDuration(1000).setStartDelay(3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(),TutorialActivity.class);
                startActivity(intent);
            }
        },4400);
        finish();

    }

}