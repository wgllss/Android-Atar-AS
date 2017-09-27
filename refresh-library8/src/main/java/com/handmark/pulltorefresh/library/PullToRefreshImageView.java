/**
 * 
 */
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 ******************************************************************************************
 * @author: Atar 
 * @createTime:2015-11-8����9:02:11
 * @modifyTime:
 * @version: 1.0.0
 * @description:
 ******************************************************************************************
 */
public class PullToRefreshImageView extends PullToRefreshBase<ImageView> {
	public PullToRefreshImageView(Context context) {
		super(context);
		setSingleView(true);
	}

	public PullToRefreshImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSingleView(true);
	}

	public PullToRefreshImageView(Context context, Mode mode) {
		super(context, mode);
		setSingleView(true);
	}

	public PullToRefreshImageView(Context context, Mode mode, AnimationStyle animStyle) {
		super(context, mode, animStyle);
		setSingleView(true);
	}

	@Override
	public Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected ImageView createRefreshableView(Context context, AttributeSet attrs) {
		return new ImageView(context, attrs);
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return true;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return true;
	}
}
