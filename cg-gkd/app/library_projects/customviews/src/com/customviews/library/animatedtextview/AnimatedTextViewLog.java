package com.customviews.library.animatedtextview;

import com.customviews.library.BuildConfig;

import android.util.Log;

public class AnimatedTextViewLog {
	public static void i(Object s) {
		if (BuildConfig.DEBUG) {
			Log.i("AnimatedTextViewLog", s.toString());
		}
	}
}
