package com.reversecoder.gk7.driver.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.event.ClickGuard;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.google.android.gms.maps.model.LatLng;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.model.NewJobAlertDetail;
import com.reversecoder.gk7.driver.model.WrapperCommonResponse;
import com.reversecoder.gk7.driver.service.FloatingViewService;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;

import java.util.Locale;

public class MapNavigationActivity extends BaseActivity {

    private boolean mIsFloatingViewShow; //Flag variable used to identify if the Floating View is visible or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_map_navigation);
        mIsFloatingViewShow = false;

//        Uri gmmIntentUri = Uri.parse("google.navigation:q=Bashundhara+Bus+Stop,+Dhaka");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);


//        showFloatingView();

/*

        LatLng destination = new LatLng(23.810332, 90.412518);
        String uri = "google.navigation:q=Bashundhara+Bus+Stop,+Dhaka";
        Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String
                .format(Locale.US, uri, destination.latitude, destination.longitude)));
//        if (canHandleIntent(this, navIntent))
        startActivity(navIntent);

//        else
//            Toast.makeText(this, "Please install Google Navigation",
//                    Toast.LENGTH_LONG).show();

//        if you have multiple apps to deal with navigation processes and you want to go directly for google Navigation,
// you can also include the line with the "setClassName" call to the activity:
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

*/

        Button btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Toast.makeText(MapNavigationActivity.this,"Button click guard",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFloatingView() {
        startService(new Intent(getApplicationContext(), FloatingViewService.class));
        mIsFloatingViewShow = true;
    }

    private void hideFloatingView() {
        stopService(new Intent(getApplicationContext(), FloatingViewService.class));
        mIsFloatingViewShow = false;
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        showFloatingView();
//    }
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        hideFloatingView();
//    }

}
