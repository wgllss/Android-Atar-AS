package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 *****************************************************************************************************************************************************************************
 * 动态加载GIF图片
 * @author :Atar
 * @createTime:2015-3-18 9:52:59
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class DynamicLoadingLayout extends LoadingLayout {
	private AnimationDrawable mAnimation;

	public DynamicLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
	}

	@Override
	protected int getVerticalLayoutID() {
		return R.layout.pull_to_refresh_header_vertical_dynamic;
	}

	@Override
	protected int getDefaultDrawableResId() {
		return imgResID;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		// mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		setAnimation();
		// mHeaderImage.setImageResource(imgResID);
		// mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void pullToRefreshImpl() {
		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.GONE);
		}
		mHeaderImage.setImageResource(imgResID);
		setAnimation();
		// mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void refreshingImpl() {
		setHeaderTextColor(headerTextColor);
		setSubHeaderTextColor(subHeaderTextColor);
		setRefreshBgColor(mInnerLayoutBg);
		setAnimation();
		// mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void releaseToRefreshImpl() {
		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.GONE);
		}
		// setAnimation();
		// mHeaderProgress.setVisibility(View.VISIBLE);
	}

	@Override
	protected void resetImpl() {
		if (mAnimation != null) {
			mAnimation.stop();
		}
		mHeaderImage.setImageResource(imgResID);
		mHeaderProgress.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @author :Atar
	 * @createTime:2015-9-7上午11:05:38
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public void setAnimation() {
		mHeaderImage.setImageResource(imgResID);
		mHeaderImage.setVisibility(View.VISIBLE);
		if (mHeaderImage.getDrawable() instanceof AnimationDrawable) {
			mAnimation = (AnimationDrawable) mHeaderImage.getDrawable();
			if (mAnimation != null) {
				mHeaderImage.post(new Runnable() {
					@Override
					public void run() {
						mAnimation.start();
					}
				});
			}
		}
	}

	/**
	*
	*/
	public void setRefreshingDrawable(int drawableResID) {
		mHeaderImage.setImageDrawable(null);
		imgResID = drawableResID;
		setAnimation();
	}
}
