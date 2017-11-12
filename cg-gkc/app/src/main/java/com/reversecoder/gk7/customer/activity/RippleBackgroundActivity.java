package com.reversecoder.gk7.customer.activity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.rippleview.RippleBackground;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.reversecoder.gk7.customer.R;

import java.util.ArrayList;


public class RippleBackgroundActivity extends BaseActivity {

    private ImageView foundDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_ripple_background);
        setActivityTitle("Delete message status");

        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.content);

        final Handler handler = new Handler();

        foundDevice = (ImageView) findViewById(R.id.foundDevice);
        ImageView button = (ImageView) findViewById(R.id.centerImage);
        button.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                rippleBackground.startRippleAnimation();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foundDevice();
                    }
                }, 3000);
            }
        });

        final RippleBackground rippleBackgroundSecond=(RippleBackground)findViewById(R.id.contentSecond);
        ImageView imageView=(ImageView)findViewById(R.id.centerImageSecond);
        imageView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                rippleBackgroundSecond.startRippleAnimation();
            }
        });
    }

    private void foundDevice() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

}
