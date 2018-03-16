package com.atar.adapters;

import android.activity.CommonActivity;
import android.adapter.CommonRecyclerAdapter;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atar.activitys.R;
import com.atar.activitys.demos.DemoRefreshRecyclerViewActivity;

import java.util.List;

/**
 * Created by atar on 2018/1/17 0017.
 */

public class RecyclerViewAdapter extends CommonRecyclerAdapter<String> {
    private int columnWidth;

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public RecyclerViewAdapter(List<?> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        setContext(parent.getContext());
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.adapter_recycler_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getList() != null && getList().get(position) != null && getList().get(position).length() > 0) {
            ((CommonActivity) getContext()).LoadImageView(getList().get(position), ((MyViewHolder) viewHolder).img_pic, 0, getAnimateFirstListener(), 0);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_pic;

        public MyViewHolder(View view) {
            super(view);
            img_pic = (ImageView) view.findViewById(R.id.img_pic);
            img_pic.setLayoutParams(new LinearLayout.LayoutParams(columnWidth, columnWidth));
        }
    }
}
