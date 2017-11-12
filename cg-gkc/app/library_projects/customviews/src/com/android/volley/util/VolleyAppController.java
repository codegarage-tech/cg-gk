package com.android.volley.util;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyAppController extends Application {

	public static final String TAG = VolleyAppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static VolleyAppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	public static synchronized VolleyAppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader(View placeHolderView) {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new VolleyLruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static void volleyExceptionHandling(Context context,
			VolleyError error) {

		if (error instanceof NoConnectionError) {
			Toast.makeText(context, "NoConnectionError", Toast.LENGTH_LONG)
					.show();
		} else if (error instanceof TimeoutError) {
			Toast.makeText(context, "TimeoutError", Toast.LENGTH_LONG).show();
		} else if (error instanceof AuthFailureError) {
			Toast.makeText(context, "AuthFailureError", Toast.LENGTH_LONG)
					.show();
		} else if (error instanceof ServerError) {
			Toast.makeText(context, "ServerError", Toast.LENGTH_LONG).show();
		} else if (error instanceof NetworkError) {
			Toast.makeText(context, "NetworkError", Toast.LENGTH_LONG).show();
		} else if (error instanceof ParseError) {
			Toast.makeText(context, "ParseError", Toast.LENGTH_LONG).show();
		}
	}
}
