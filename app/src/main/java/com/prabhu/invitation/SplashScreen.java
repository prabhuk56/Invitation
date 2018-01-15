package com.prabhu.invitation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by prabhu on 17-12-2017.
 */

public class SplashScreen extends Activity{
    private static int SPLASH_TIME_OUT= 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        TextView splashText =findViewById(R.id.txtSplash);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/lobstertwo_regular.ttf" );
        splashText.setTypeface(typeface);
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
        /** Text Animation **/
        Animation animFadeIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        splashText.startAnimation(animFadeIn);
    }
}
