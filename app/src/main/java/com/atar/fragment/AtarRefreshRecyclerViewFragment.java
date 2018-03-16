package com.atar.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atar.activitys.R;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;


public class AtarRefreshRecyclerViewFragment extends AratRefreshAbsListViewFragment<PullToRefreshRecyclerView, RecyclerView> {
    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.common_pull_to_refresh_recycle, container, false);
            setTextView((TextView) view.findViewById(R.id.txt_list_toast));
            setRefreshView((PullToRefreshRecyclerView) view.findViewById(R.id.common_pull_refresh_recyclerview));
        }
        if (view != null && view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    /**
     * RecyclerView 适配器
     *
     * @param adapter
     * @author :Atar
     * @createTime:2015-6-3上午10:44:38
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    protected void setAdapter(RecyclerView.Adapter adapter) {
        if (getRefreshView() != null) {
            getRefreshView().setAdapter(adapter);
            if (getPullView() != null) {
                getPullView().setScrollingWhileRefreshingEnabled(true);
            }
        }
    }

    protected void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (getRefreshView() != null) {
            getRefreshView().setLayoutManager(layoutManager);
        }
    }

    protected void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (getRefreshView() != null) {
            getRefreshView().addItemDecoration(itemDecoration);
        }
    }
}
