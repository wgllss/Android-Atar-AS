/**
 * 
 */
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 ******************************************************************************************
 * @author: Atar 
 * @createTime:2015-11-8下午5:59:15
 * @modifyTime:
 * @version: 1.0.0
 * @description:
 ******************************************************************************************
 */
public class PullToRefreshTextView extends PullToRefreshBase<TextView> {

	/**
	 *@atour: Atar
	 *@createTime:2015-11-8下午5:59:41
	 *@modifyTime:
	 *@version: 
	 *@param context:
	 *@description:
	 */
	public PullToRefreshTextView(Context context) {
		super(context);
		setSingleView(true);
	}

	public PullToRefreshTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSingleView(true);
	}

	public PullToRefreshTextView(Context context, Mode mode) {
		super(context, mode);
		setSingleView(true);
	}

	public PullToRefreshTextView(Context context, Mode mode, AnimationStyle animStyle) {
		super(context, mode, animStyle);
		setSingleView(true);
	}

	@Override
	public Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected TextView createRefreshableView(Context context, AttributeSet attrs) {
		return new TextView(context, attrs);
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
