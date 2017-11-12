package com.customviews.library.verticalstepper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * This class extends the default RecyclerView with a few changes to improve the handling of the installation steps.
 */
public class VerticalStepperLayout extends RecyclerView {

    public VerticalStepperLayout(Context context) {
        super(context);
        init();
    }

    public VerticalStepperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalStepperLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //setLayoutManager(new WrappingLinearLayoutManager(getContext()));
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(adapter instanceof VerticalStepperAdapter) {
            super.setAdapter(adapter);
        }
    }

    public void smoothScrollToCurrentPosition() {
        Adapter adapter = getAdapter();
        if(adapter != null && adapter instanceof VerticalStepperAdapter) {
            super.smoothScrollToPosition(((VerticalStepperAdapter) adapter).getCurrentStepNumber());
        }
    }
}
