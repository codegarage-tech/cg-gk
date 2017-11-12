package com.customviews.library.effects;

import android.view.View;

import com.customviews.library.utils.CustomEffect;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class StandardEffect implements CustomEffect {

	@Override
	public void initView(View item, int position, int scrollDirection) {
		// no op
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		// no op
	}

}
