package org.app.mboostertwv2.listAdapter_folder.Slider.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class StackTransformer extends BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		ViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
	}

}
