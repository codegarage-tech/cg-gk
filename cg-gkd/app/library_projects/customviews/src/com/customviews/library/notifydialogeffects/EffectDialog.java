package com.customviews.library.notifydialogeffects;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.customviews.library.R;

public class EffectDialog extends Dialog implements DialogInterface {

//	private final String defTextColor = "#FFFFFFFF";
//
//	private final String defDividerColor = "#11000000";
//
//	private final String defMsgColor = "#FFFFFFFF";
//
//	private final String defDialogColor = "#FFE74C3C";

	private static Context tmpContext;

	private Effectstype type = Effectstype.Slidetop;

//	private LinearLayout mLinearLayoutView;

	private LinearLayout mRelativeLayoutView;

//	private LinearLayout mLinearLayoutMsgView;
//
//	private LinearLayout mLinearLayoutTopView;

	private FrameLayout mFrameLayoutCustomView;

	private View mDialogView;

//	private View mDivider;
//
//	private TextView mTitle;
//
//	private TextView mMessage;
//
//	private ImageView mIcon;
//
//	private Button mButton1;
//
//	private Button mButton2;

	private int mDuration = -1;

	private static int mOrientation = 1;

	private boolean isCancelable = true;
	// private boolean isCancelableOnTouchOutside = true;

	private static EffectDialog instance;

	public EffectDialog(Context context) {
		super(context);
		init(context);

	}

	public EffectDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().setAttributes(
				(WindowManager.LayoutParams) params);
		// setCanceledOnTouchOutside(isCancelableOnTouchOutside);
		// setCancelable(isCancelable);

	}

	public static EffectDialog getInstance(Context context) {

		if (instance == null || !tmpContext.equals(context)) {
			synchronized (EffectDialog.class) {
				if (instance == null || !tmpContext.equals(context)) {
					instance = new EffectDialog(context, R.style.dialog_untran);
				}
			}
		}
		tmpContext = context;
		return instance;

	}

	private void init(Context context) {

		mDialogView = View.inflate(context, R.layout.dialog_effect, null);

//		 mLinearLayoutView = (LinearLayout) mDialogView
//		 .findViewById(R.id.parentPanel);
		mRelativeLayoutView = (LinearLayout) mDialogView
				.findViewById(R.id.main);
		// mLinearLayoutTopView = (LinearLayout) mDialogView
		// .findViewById(R.id.topPanel);
		// mLinearLayoutMsgView = (LinearLayout) mDialogView
		// .findViewById(R.id.contentPanel);
		// mFrameLayoutCustomView = (FrameLayout) mDialogView
		// .findViewById(R.id.customPanel);

		// mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
		// mMessage = (TextView) mDialogView.findViewById(R.id.message);
		// mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
		// mDivider = mDialogView.findViewById(R.id.titleDivider);
		// mButton1 = (Button) mDialogView.findViewById(R.id.button1);
		// mButton2 = (Button) mDialogView.findViewById(R.id.button2);

		setContentView(mDialogView);

		this.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {

				mFrameLayoutCustomView.setVisibility(View.VISIBLE);
				if (type == null) {
					type = Effectstype.Slidetop;
				}
				start(type);

			}
		});

		mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isCancelable) {
					dismiss();
				}
			}
		});
	}

	// public void toDefault() {
	// mTitle.setTextColor(Color.parseColor(defTextColor));
	// mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
	// mMessage.setTextColor(Color.parseColor(defMsgColor));
	// mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
	// }
	//
	// public EffectDialog withDividerColor(String colorString) {
	// mDivider.setBackgroundColor(Color.parseColor(colorString));
	// return this;
	// }
	//
	// public EffectDialog withDividerColor(int color) {
	// mDivider.setBackgroundColor(color);
	// return this;
	// }
	//
	// public EffectDialog withTitle(CharSequence title) {
	// toggleView(mLinearLayoutTopView, title);
	// mTitle.setText(title);
	// return this;
	// }
	//
	// public EffectDialog withTitleColor(String colorString) {
	// mTitle.setTextColor(Color.parseColor(colorString));
	// return this;
	// }
	//
	// public EffectDialog withTitleColor(int color) {
	// mTitle.setTextColor(color);
	// return this;
	// }
	//
	// public EffectDialog withMessage(int textResId) {
	// toggleView(mLinearLayoutMsgView, textResId);
	// mMessage.setText(textResId);
	// return this;
	// }
	//
	// public EffectDialog withMessage(CharSequence msg) {
	// toggleView(mLinearLayoutMsgView, msg);
	// mMessage.setText(msg);
	// return this;
	// }
	//
	// public EffectDialog withMessageColor(String colorString) {
	// mMessage.setTextColor(Color.parseColor(colorString));
	// return this;
	// }
	//
	// public EffectDialog withMessageColor(int color) {
	// mMessage.setTextColor(color);
	// return this;
	// }
	//
	// public EffectDialog withDialogColor(String colorString) {
	// mLinearLayoutView.getBackground().setColorFilter(
	// ColorUtils.getColorFilter(Color.parseColor(colorString)));
	// return this;
	// }
	//
	// public EffectDialog withDialogColor(int color) {
	// mLinearLayoutView.getBackground().setColorFilter(
	// ColorUtils.getColorFilter(color));
	// return this;
	// }
	//
	// public EffectDialog withIcon(int drawableResId) {
	// mIcon.setImageResource(drawableResId);
	// return this;
	// }
	//
	// public EffectDialog withNoIcon() {
	// mIcon.setVisibility(View.GONE);
	// return this;
	// }
	//
	// public EffectDialog withIcon(Drawable icon) {
	// mIcon.setImageDrawable(icon);
	// return this;
	// }

	public EffectDialog withDuration(int duration) {
		this.mDuration = duration;
		return this;
	}

	public EffectDialog withEffect(Effectstype type) {
		this.type = type;
		return this;
	}

	// public EffectDialog withButtonDrawable(int resid) {
	// mButton1.setBackgroundResource(resid);
	// mButton2.setBackgroundResource(resid);
	// return this;
	// }
	//
	// public EffectDialog withButton1Text(CharSequence text) {
	// mButton1.setVisibility(View.VISIBLE);
	// mButton1.setText(text);
	//
	// return this;
	// }
	//
	// public EffectDialog withButton2Text(CharSequence text) {
	// mButton2.setVisibility(View.VISIBLE);
	// mButton2.setText(text);
	// return this;
	// }
	//
	// public EffectDialog setButton1Click(View.OnClickListener click) {
	// mButton1.setOnClickListener(click);
	// return this;
	// }
	//
	// public EffectDialog setButton2Click(View.OnClickListener click) {
	// mButton2.setOnClickListener(click);
	// return this;
	// }

	public EffectDialog setCustomView(int resId, Context context) {
		View customView = View.inflate(context, resId, null);
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(customView);
		return this;
	}

	public EffectDialog setCustomView(View view, Context context) {
		if (mFrameLayoutCustomView.getChildCount() > 0) {
			mFrameLayoutCustomView.removeAllViews();
		}
		mFrameLayoutCustomView.addView(view);

		return this;
	}

	public EffectDialog isCancelableOnTouchOutside(
			boolean iCancelableOnTouchOutside) {
		this.isCancelable = iCancelableOnTouchOutside;
		setCanceledOnTouchOutside(iCancelableOnTouchOutside);
		return this;
	}

	public EffectDialog isCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		setCancelable(cancelable);
		return this;
	}

	// private void toggleView(View view, Object obj) {
	// if (obj == null) {
	// view.setVisibility(View.GONE);
	// } else {
	// view.setVisibility(View.VISIBLE);
	// }
	// }

	@Override
	public void show() {
		super.show();
	}

	private void start(Effectstype type) {
		BaseEffects animator = type.getAnimator();
		if (mDuration != -1) {
			animator.setDuration(Math.abs(mDuration));
		}
		animator.start(mRelativeLayoutView);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		// mButton1.setVisibility(View.GONE);
		// mButton2.setVisibility(View.GONE);
		mFrameLayoutCustomView.setVisibility(View.GONE);
	}

}
