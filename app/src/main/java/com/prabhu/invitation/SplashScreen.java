package com.prabhu.invitation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 * Created by prabhu on 17-12-2017.
 */

public class SplashScreen extends Activity{
    private static int SPLASH_TIME_OUT= 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        ImageView splashImage = findViewById(R.id.imgSplash);
        splashImage.setAlpha((float)0.2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent= new Intent(SplashScreen.this,MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
