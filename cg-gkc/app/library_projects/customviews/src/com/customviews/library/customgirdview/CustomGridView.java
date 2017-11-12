package com.customviews.library.customgirdview;

import com.customviews.library.utils.CustomEffect;
import com.customviews.library.utils.CustomHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView {

	private final CustomHelper mHelper;

	public CustomGridView(Context context) {
		super(context);
		mHelper = init(context, null);
	}

	public CustomGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHelper = init(context, attrs);
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHelper = init(context, attrs);
	}

	private CustomHelper init(Context context, AttributeSet attrs) {
		CustomHelper helper = new CustomHelper(context, attrs);
		super.setOnScrollListener(helper);
		return helper;
	}

	/**
	 * @see android.widget.AbsListView#setOnScrollListener
	 */
	@Override
	public final void setOnScrollListener(OnScrollListener l) {
		mHelper.setOnScrollListener(l);
	}

	/**
	 * Sets the desired transition effect.
	 * 
	 * @param transitionEffect
	 *            Numeric constant representing a bundled transition effect.
	 */
	public void setTransitionEffect(int transitionEffect) {
		mHelper.setTransitionEffect(transitionEffect);
	}

	/**
	 * Sets the desired transition effect.
	 * 
	 * @param transitionEffect
	 *            The non-bundled transition provided by the client.
	 */
	public void setTransitionEffect(CustomEffect transitionEffect) {
		mHelper.setTransitionEffect(transitionEffect);
	}

	/**
	 * Sets whether new items or all items should be animated when they become
	 * visible.
	 * 
	 * @param onlyAnimateNew
	 *            True if only new items should be animated; false otherwise.
	 */
	public void setShouldOnlyAnimateNewItems(boolean onlyAnimateNew) {
		mHelper.setShouldOnlyAnimateNewItems(onlyAnimateNew);
	}
}
