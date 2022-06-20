package com.example.parstagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        },1000);

    }
}
