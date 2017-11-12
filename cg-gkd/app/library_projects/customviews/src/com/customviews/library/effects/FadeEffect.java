package com.customviews.library.effects;

import android.view.View;

import com.customviews.library.utils.CustomEffect;
import com.customviews.library.utils.CustomHelper;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class FadeEffect implements CustomEffect {

	private static final int DURATION_MULTIPLIER = 5;

	@Override
	public void initView(View item, int position, int scrollDirection) {
		ViewHelper.setAlpha(item, CustomHelper.TRANSPARENT);
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		animator.setDuration(CustomHelper.DURATION * DURATION_MULTIPLIER);
		animator.alpha(CustomHelper.OPAQUE);
	}

}
