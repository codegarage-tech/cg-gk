package com.customviews.library.effects;

import android.view.View;

import com.customviews.library.utils.CustomEffect;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class HelixEffect implements CustomEffect {

	private static final int INITIAL_ROTATION_ANGLE = 180;

	@Override
	public void initView(View item, int position, int scrollDirection) {
		ViewHelper.setRotationY(item, INITIAL_ROTATION_ANGLE);
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		animator.rotationYBy(INITIAL_ROTATION_ANGLE * scrollDirection);
	}

}
