package com.oriontekapp.buhos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splash);

        //hide actionBar
        getSupportActionBar().hide();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(Splash.this, MainActivity.class));
                //Animatoo.animateFade(Splash.this);
                finish();
                // Write whatever to want to do after delay specified (1 sec)
            }
        }, 1000);



    }
}