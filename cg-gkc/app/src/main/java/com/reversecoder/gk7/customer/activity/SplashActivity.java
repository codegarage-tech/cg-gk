package com.reversecoder.gk7.customer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.customviews.library.application.BaseActivity;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AppSettings;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_splash);
        setActivityTitle("Splash Screen");
        setToolbar(false);
        startSplashScreenTimer();
    }

    private void startSplashScreenTimer() {
        CountDownTimer mSplashTimer = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                goHomeScreen();

            }
        };
        mSplashTimer.start();
    }

    private void goHomeScreen() {
        Intent intent;
        if (AppSettings.isUserLoggedIn(getParentContext())) {
            if (AppSettings.getUserType(getParentContext()).equalsIgnoreCase(AllConstants.TEXT_DRIVER)) {
                intent = new Intent(getParentContext(), DashboardDriverNavigationDrawerActivity.class);
            } else {
                intent = new Intent(getParentContext(), DashboardCustomerNavigationDrawerActivity.class);
            }
        } else {
            intent = new Intent(getParentContext(), LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
