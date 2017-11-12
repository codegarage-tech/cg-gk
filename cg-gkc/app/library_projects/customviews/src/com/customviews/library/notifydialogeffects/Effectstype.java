package com.customviews.library.notifydialogeffects;

import com.customviews.library.notifydialogeffects.BaseEffects;
import com.customviews.library.notifydialogeffects.FadeIn;
import com.customviews.library.notifydialogeffects.Fall;
import com.customviews.library.notifydialogeffects.FlipH;
import com.customviews.library.notifydialogeffects.FlipV;
import com.customviews.library.notifydialogeffects.NewsPaper;
import com.customviews.library.notifydialogeffects.RotateLeft;
import com.customviews.library.notifydialogeffects.Shake;
import com.customviews.library.notifydialogeffects.SideFall;
import com.customviews.library.notifydialogeffects.RotateBottom;
import com.customviews.library.notifydialogeffects.SlideBottom;
import com.customviews.library.notifydialogeffects.SlideLeft;
import com.customviews.library.notifydialogeffects.SlideRight;
import com.customviews.library.notifydialogeffects.SlideTop;
import com.customviews.library.notifydialogeffects.Slit;

public enum Effectstype {

	Fadein(FadeIn.class), Slideleft(SlideLeft.class), Slidetop(SlideTop.class), SlideBottom(
			SlideBottom.class), Slideright(SlideRight.class), Fall(Fall.class), Newspager(
			NewsPaper.class), Fliph(FlipH.class), Flipv(FlipV.class), RotateBottom(
			RotateBottom.class), RotateLeft(RotateLeft.class), Slit(Slit.class), Shake(
			Shake.class), Sidefill(SideFall.class);
	private Class<? extends BaseEffects> effectsClazz;

	private Effectstype(Class<? extends BaseEffects> mclass) {
		effectsClazz = mclass;
	}

	public BaseEffects getAnimator() {
		BaseEffects bEffects = null;
		try {
			bEffects = effectsClazz.newInstance();
		} catch (ClassCastException e) {
			throw new Error("Can not init animatorClazz instance");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			throw new Error("Can not init animatorClazz instance");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			throw new Error("Can not init animatorClazz instance");
		}
		return bEffects;
	}
}
