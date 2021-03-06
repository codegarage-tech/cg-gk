package com.reversecoder.gk7.customer.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.reversecoder.gk7.customer.R;

public class FloatingViewService extends Service {

    private WindowManager mWindowManager;
    private FloatingActionMenu mImgFloatingView;
    private View view;
    private boolean mIsFloatingViewAttached = false;

    @Override
    public IBinder onBind(Intent intent) {
        //Not use this method
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mIsFloatingViewAttached) {
            mWindowManager.addView(mImgFloatingView, mImgFloatingView.getLayoutParams());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

//        LayoutInflater i = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//OR
        LayoutInflater i = LayoutInflater.from(getApplicationContext());
        view = i.inflate(R.layout.float_image, null);

//        mImgFloatingView = new ImageView(this);
//        mImgFloatingView.setImageResource(R.mipmap.ic_launcher);
        mImgFloatingView = (FloatingActionMenu) view.findViewById(R.id.menu_blue);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        mWindowManager.addView(view, params);

        mImgFloatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
            }
        });

//        mImgFloatingView.setOnTouchListener(new View.OnTouchListener() {
//            private int initialX;
//            private int initialY;
//            private float initialTouchX;
//            private float initialTouchY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = params.x;
//                        initialY = params.y;
//                        initialTouchX = event.getRawX();
//                        initialTouchY = event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
//                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
//                        mWindowManager.updateViewLayout(view, params);
//                        return true;
//                }
//                return false;
//            }
//        });
//
        mIsFloatingViewAttached = true;
    }

    public void removeView() {
//        if (mImgFloatingView != null){
//            mWindowManager.removeView(mImgFloatingView);
//            mIsFloatingViewAttached = false;
//        }

        if (view != null) {
            mWindowManager.removeView(view);
            mIsFloatingViewAttached = false;
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT);
        super.onDestroy();
        removeView();
    }
}

