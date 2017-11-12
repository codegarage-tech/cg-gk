package com.customviews.library.typefacehelper;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.text.SpannableString;

/**
 * Helper for setting actionbar title with custom typeface via SpannableString.
 * Contains LGE 4.1 bug workaround
 * 
 * @see http://stackoverflow.com/questions/7658725/android-java-lang-
 *      illegalargumentexception-invalid-payload-item-type
 */
public class ActionBarTypefaceHelper {

	private static final String TAG = ActionBarTypefaceHelper.class.getSimpleName();

	public static void setTitle(android.support.v7.app.ActionBar actionBar,
			SpannableString spannableString) {
		// BUGFIX
		// @see
		// http://stackoverflow.com/questions/7658725/android-java-lang-illegalargumentexception-invalid-payload-item-type
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN
				&& Build.MANUFACTURER.toUpperCase().equals("LGE")) {
			actionBar.setTitle(spannableString.toString());
		} else {
			actionBar.setTitle(spannableString);
		}
	}

	public static void setTitle(android.support.v7.app.ActionBar actionBar,
			CharSequence charSequence) {
		actionBar.setTitle(charSequence.toString());
	}

	@TargetApi(11)
	public static void setTitle(ActionBar actionBar,
			SpannableString spannableString) {
		// BUGFIX
		// @see
		// http://stackoverflow.com/questions/7658725/android-java-lang-illegalargumentexception-invalid-payload-item-type
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN
				&& Build.MANUFACTURER.toUpperCase().equals("LGE")) {
			actionBar.setTitle(spannableString.toString());
		} else {
			actionBar.setTitle(spannableString);
		}
	}

	@TargetApi(11)
	public static void setTitle(ActionBar actionBar, CharSequence charSequence) {
		actionBar.setTitle(charSequence.toString());
	}
}
