package com.example.zoe.swagdragon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Created by PC on 8/08/2015.
 */
public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    TextView taglineText;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        //generate random welcome message
        taglineText = (TextView) findViewById(R.id.splash_tagline);

        int random = (int)(Math.random()*100 +1);

        if (random >= 1 && random <= 20){
            taglineText.setText(R.string.splash_tagline_one);
        } else if (random >= 21 && random <= 40){
            taglineText.setText(R.string.splash_tagline_two);
        }else if (random >= 41 && random <= 60){
            taglineText.setText(R.string.splash_tagline_three);
        }else if (random >= 61 && random <= 80){
            taglineText.setText(R.string.splash_tagline_four);
        }else {
            taglineText.setText(R.string.splash_tagline_five);
        }





        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MapsActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


}