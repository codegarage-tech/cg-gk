package com.customviews.library.animatedtextview;

import android.graphics.Canvas;
import android.util.AttributeSet;

public interface IAnimatedText {
	void init(AnimatedTextView hTextView, AttributeSet attrs, int defStyle);

	void animateText(CharSequence text);

	void onDraw(Canvas canvas);

	void reset(CharSequence text);
}
