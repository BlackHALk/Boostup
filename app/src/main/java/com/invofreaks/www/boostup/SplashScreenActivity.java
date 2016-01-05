package com.invofreaks.www.boostup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashScreenActivity extends Activity {

    Animation alpha,scale,alphaS;
    TextView wel;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        wel = (TextView) findViewById(R.id.welcomeTextView);
        view = findViewById(android.R.id.content);

        alpha = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
        scale = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale);
        alphaS = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha2);

        wel.startAnimation(scale);
        //wel.startAnimation(alpha);

        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                wel.startAnimation(alpha);
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        wel.startAnimation(alphaS);

                        finish();
                        Intent mainA = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainA);
                        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
