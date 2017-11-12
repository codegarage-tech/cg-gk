package com.customviews.library.effects;

import android.view.View;

import com.customviews.library.utils.CustomEffect;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ZipperEffect implements CustomEffect {

	@Override
	public void initView(View item, int position, int scrollDirection) {
		boolean isEven = position % 2 == 0;
		ViewHelper.setTranslationX(item, (isEven ? -1 : 1) * item.getWidth());
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		animator.translationX(0);
	}

}
