package com.atar.fragment.demos;


import android.common.CommonHandler;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.recycler.DividerGridItemDecoration;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.utils.DeviceManager;
import android.utils.ScreenUtils;
import android.view.View;
import android.view.ViewGroup;

import com.atar.activitys.demos.DemoRefreshInFragmentActivity;
import com.atar.adapters.RecyclerViewAdapter;
import com.atar.enums.EnumMsgWhat;
import com.atar.fragment.AtarRefreshRecyclerViewFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemoRefreshRecyclerViewFragment extends AtarRefreshRecyclerViewFragment {
    private List<String> list = new ArrayList<String>();
    private RecyclerViewAdapter mRecyclerViewAdapter = new RecyclerViewAdapter(list);
    private int spanCount = 3;

    private String[] mStrings = {
            "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=506145377,118777663&fm=173&s=158349B046C27CFC402C03B803005016&w=218&h=146&img.JPEG",
            "http://img4.imgtn.bdimg.com/it/u=2975728606,2361096430&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=400401121,3823676335&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=2975728606,2361096430&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1742626185,2547278809&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2729302523,3995984350&fm=27&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=540293922,788232604&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3837060263,2881298872&fm=27&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=2568097601,1418434375&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2936881590,490535990&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=147015573,3182587356&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3983254917,1818731284&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4220572884,925147497&fm=200&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=1972873509,2904368741&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=992749367,629473581&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2298840176,1428087360&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2684437396,1351078513&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3965656371,3751907427&fm=27&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=70626598,2880991955&fm=27&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3630905306,2351688970&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2728779192,249357455&fm=27&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3670258880,1432604197&fm=200&gp=0.jpg",};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        //LinearLayoutManager
        //setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));//瀑布流布局

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        setLayoutManager(layoutManager);

        setLayoutManager(new GridLayoutManager(getActivity(), spanCount));//Grid布局列表

        int columnWidth = (DeviceManager.getScreenWidth(getActivity()) - (int) (ScreenUtils.getIntToDip(5) * (spanCount - 1))) / spanCount;
        mRecyclerViewAdapter.setColumnWidth(columnWidth);

        list.addAll(Arrays.asList(mStrings));
        mRecyclerViewAdapter.notifyDataSetChanged();

        setAdapter(mRecyclerViewAdapter);

        View view0 = new View(getActivity());

        int size = (int) ScreenUtils.getIntToDip(5);
        view0.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        view0.setBackgroundColor(Color.parseColor("#ff0000"));
        addItemDecoration(new DividerGridItemDecoration(getActivity(), GridLayoutManager.VERTICAL, view0));

//        if (getActivity() != null) {
//            ((DemoRefreshInFragmentActivity) getActivity()).setApplyScrollListenre(getRefreshView());
//        }
    }

    @Override
    public void onHandlerData(Message msg) {
        super.onHandlerData(msg);
        switch (msg.what) {
            case EnumMsgWhat.LOAD_FROM_SQL:
                setRefreshing();
                break;
            case EnumMsgWhat.LOAD_FROM_SQL_COMPLETE:
                break;
            case EnumMsgWhat.REFRESH_PULL_DOWN:
            case EnumMsgWhat.REFRESH_PULL_UP:
                CommonHandler.getInstatnce().getHandler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        onStopRefresh();
                    }
                }, 1000);
                break;
            case EnumMsgWhat.REFRESH_HANDLER:
                break;
        }
    }
}
