package com.example.smartintruderalertsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;

    //variables
    Animation welcome_animation;
    TextView greet;
    public static final String PREFS_GAME ="com.abhiandroid.abhiapp.GamePlay";
    public static final String GAME_SCORE= "GameScore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
   //     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        welcome_animation = AnimationUtils.loadAnimation(this, R.anim.welcome_animation);
        //SharedPreferences sharedPreferences =  getSharedPreferences("MyPref",MODE_PRIVATE);

        SharedPreferences sp = getSharedPreferences(PREFS_GAME , Context.MODE_PRIVATE);
        int sc  = sp.getInt(GAME_SCORE,0);
        if(sc==100) {

         }else {
            String input = "Congrats. You have Installed Successfully  ... ";
            Intent serviceIntent = new Intent(this, ExampleService.class);
            serviceIntent.putExtra("inputExtra", input);
            ContextCompat.startForegroundService(this, serviceIntent);

            sp.edit().putInt(GAME_SCORE,100).commit();

        }
        //Hooks
        greet = findViewById(R.id.txt_welcome);
        greet.setAnimation(welcome_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, Home.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}