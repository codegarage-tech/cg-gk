package com.customviews.library.storehouse;//package com.bjit.awsdemo;
//
//import com.bjit.awslibrary.util.AllUtilsManager;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
///**
// * @author Md. Rashadul Alam
// *
// */
//public class MainActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		ThreadManager.runInBackgroundThenUi(new Runnable() {
//			@Override
//			public void run() {
//				// wait for that tasty WiFi
//				try {
//					// check if connected!
//					while (!isConnected(MainActivity.this)) {
//						
//						ThreadManager.runOnUi(new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								Toast.makeText(MainActivity.this, "please connect to internet", Toast.LENGTH_SHORT)
//		                                .show();
//							}
//						});
//						// Wait to connect
//						Thread.sleep(5000);
//					}
//
//				} catch (Exception e) {
//				}
//			}
//		}, new Runnable() {
//			@Override
//			public void run() {
//				// GUYS I HAVE THE WIFI!!
//				Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_SHORT).show();
//			}
//		});
//
//	}
//
//	public static boolean isConnected(Context context) {
//		ConnectivityManager connectivityManager = (ConnectivityManager) context
//		        .getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = null;
//		if (connectivityManager != null) {
//			networkInfo = connectivityManager.getActiveNetworkInfo();
//		}
//
//		return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
//	}
//}
