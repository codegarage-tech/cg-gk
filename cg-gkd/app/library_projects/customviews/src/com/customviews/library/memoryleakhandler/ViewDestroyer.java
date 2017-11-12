package com.customviews.library.memoryleakhandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * @author rashedul
 *
 *         This class is used for unbinding resources consumed by Views in
 *         Activity.
 * 
 *         Android framework does not free resources immediately which are
 *         consumed by Views and this leads to OutOfMemoryError sometimes
 *         although there are no user mistakes.
 * 
 * @see http://code.google.com/p/android/issues/detail?id=8488
 */

public class ViewDestroyer {
    /**
	 * Removes the reference to the activity from every view in a view hierarchy
	 * (listeners, images etc.). This method should be called in the onDestroy()
	 * method of each activity.
	 * 
	 * @param view
	 *            View to free from memory
	 * 
	 */
	public static void unbindReferences(View view) {
		try {
			if (view != null) {
				unbindViewReferences(view);
				if (view instanceof ViewGroup) {
					unbindViewGroupReferences((ViewGroup) view);
				}
			}
		} catch (Exception ignore) {
			/*
			 * whatever exception is thrown just ignore it because a crash is
			 * always worse than this method not doing what it's supposed to do
			 */
		}
	}

	/**
	 * Removes the reference to the activity from every view in a view hierarchy
	 * (listeners, images etc.). This method should be called in the onDestroy()
	 * method of each activity.
	 * 
	 * @param view
	 *            View to free from memory
	 * 
	 */
	public static void unbindReferences(Activity activity, int viewID) {
		try {
			View view = activity.findViewById(viewID);
			if (view != null) {
				unbindViewReferences(view);
				if (view instanceof ViewGroup) {
					unbindViewGroupReferences((ViewGroup) view);
				}
			}
		} catch (Exception ignore) {
			/*
			 * whatever exception is thrown just ignore it because a crash is
			 * always worse than this method not doing what it's supposed to do.
			 */
		}
	}

	/**
	 * Used for unbind viewgroup references
	 *
	 */
	private static void unbindViewGroupReferences(ViewGroup viewGroup) {
		int nrOfChildren = viewGroup.getChildCount();
		for (int i = 0; i < nrOfChildren; i++) {
			View view = viewGroup.getChildAt(i);
			unbindViewReferences(view);
			if (view instanceof ViewGroup) {
				unbindViewGroupReferences((ViewGroup) view);
			}
		}

		try {
			viewGroup.removeAllViews();
		} catch (Exception ignore) {
			// AdapterViews, ListViews and potentially other ViewGroups don't
			// support the removeAllViews operation
		}
	}

	/**
	 * Used for unbind view references
	 *
	 */
	@SuppressLint("NewApi")
	private static void unbindViewReferences(View view) {
		// Set everything to null (API Level 8)
		try {
			view.setOnClickListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setOnCreateContextMenuListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setOnFocusChangeListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setOnKeyListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setOnLongClickListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setOnClickListener(null);
		} catch (Exception ignore) {
		}

		try {
			view.setTouchDelegate(null);
		} catch (Exception ignore) {
		}

		Drawable d = view.getBackground();
		if (d != null) {
			try {
				d.setCallback(null);
			} catch (Exception ignore) {
			}
		}

		if (view instanceof ImageView) {
			ImageView imageView = (ImageView) view;
			d = imageView.getDrawable();
			if (d != null) {
				d.setCallback(null);
			}

			if (d instanceof BitmapDrawable) {
				Bitmap bm = ((BitmapDrawable) d).getBitmap();
				bm.recycle();
			}

			imageView.setImageDrawable(null);
		} else if (view instanceof WebView) {
			((WebView) view).destroyDrawingCache();
			((WebView) view).removeAllViews();
			((WebView) view).destroy();
		}

		try {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackground(null);
			} else {
				view.setBackgroundDrawable(null);
			}
		} catch (Exception ignore) {
		}

		try {
			view.setAnimation(null);
		} catch (Exception ignore) {
		}

		try {
			view.setContentDescription(null);
		} catch (Exception ignore) {
		}

		try {
			view.setTag(null);
		} catch (Exception ignore) {
		}
	}

	/**
	 * This method tells you how many total bytes of heap your app is allowed to
	 * use.
	 *
	 */

	public static long allowedHeapForApp() {
		Runtime rt = Runtime.getRuntime();
		long maxMemory = rt.maxMemory();
		Log.v("allowedHeapForApp", Long.toString(maxMemory));
		return maxMemory;
	}

	/**
	 * This method tells you approximately how many megabytes of heap your app
	 * should use if it wants to be properly respectful of the limits of the
	 * present device, and of the rights of other apps to run without being
	 * repeatedly forced into the onStop() / onResume() cycle as they are rudely
	 * flushed out of memory while your elephantine app takes a bath in the
	 * Android jacuzzi.
	 */
	public static int maxAllowedHeapForApp(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass();
		Log.v("maxAllowedHeapForApp", Integer.toString(memoryClass));
		return memoryClass;
	}

	/**
	 * for getting available memory information
	 */
	public static long availableMemory(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem;
		return availableMegs;
	}

	// unbindViewReferences is better
	private void nullViewDrawablesRecursive(View view) {
		if (view != null) {
			try {
				ViewGroup viewGroup = (ViewGroup) view;

				int childCount = viewGroup.getChildCount();
				for (int index = 0; index < childCount; index++) {
					View child = viewGroup.getChildAt(index);
					nullViewDrawablesRecursive(child);
				}
			} catch (Exception e) {
			}

			nullViewDrawable(view);
		}
	}

	// unbindViewReferences is better
	private void nullViewDrawable(View view) {
		try {
			view.setBackgroundDrawable(null);
		} catch (Exception e) {
		}

		try {
			ImageView imageView = (ImageView) view;
			((BitmapDrawable) imageView.getDrawable()).getBitmap().recycle();
			imageView.setImageDrawable(null);
			imageView.setBackgroundDrawable(null);
		} catch (Exception e) {
		}
	}
}