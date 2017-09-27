package com.handmark.pulltorefresh.library;///**
// * 
// */
//package com.handmark.pulltorefresh.library;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.widget.ExpandableListView;
//
//import com.atar.widgets.PinnedHeaderExpandableListView;
//import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
//
///**
// *****************************************************************************************************************************************************************************
// * 
// * @author :Atar
// * @createTime:2015-6-5�????5:14:39
// * @version:1.0.0
// * @modifyTime:
// * @modifyAuthor:
// * @description:
// *****************************************************************************************************************************************************************************
// */
//public class PullToRefreshPinnedHeaderExpandableListView extends PullToRefreshAdapterViewBase<ExpandableListView> {
//
//	public PullToRefreshPinnedHeaderExpandableListView(Context context) {
//		super(context);
//	}
//
//	public PullToRefreshPinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public PullToRefreshPinnedHeaderExpandableListView(Context context, Mode mode) {
//		super(context, mode);
//	}
//
//	public PullToRefreshPinnedHeaderExpandableListView(Context context, Mode mode, AnimationStyle style) {
//		super(context, mode, style);
//	}
//
//	@Override
//	public Orientation getPullToRefreshScrollDirection() {
//		return Orientation.VERTICAL;
//	}
//
//	@Override
//	protected ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
//		return new PinnedHeaderExpandableListView(context, attrs);
//	}
// }
