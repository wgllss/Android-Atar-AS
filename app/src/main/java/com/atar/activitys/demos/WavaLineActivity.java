package com.atar.activitys.demos;

import android.graphics.Color;
import android.os.Bundle;

import com.atar.activitys.AtarCommonActivity;
import com.atar.activitys.R;
import com.atar.widgets.SiriView;

public class WavaLineActivity extends AtarCommonActivity {

    private SiriView sirview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_wava_line);
    }

    @Override
    protected void initControl() {
        sirview = (SiriView) findViewById(R.id.sirview);
    }

    @Override
    protected void initValue() {
        setActivityTitle("wave曲线");


        // 停止波浪曲线
        sirview.stop();
// 设置曲线高度，height的取值是0f~1f
        sirview.setWaveHeight(0.5f);
// 设置曲线的粗细，width的取值大于0f
        sirview.setWaveWidth(5f);
// 设置曲线颜色
        sirview.setWaveColor(Color.rgb(39, 188, 136));
// 设置曲线在X轴上的偏移量，默认值为0f
        sirview.setWaveOffsetX(0f);
// 设置曲线的数量，默认是4
        sirview.setWaveAmount(40);
// 设置曲线的速度，默认是0.1f
        sirview.setWaveSpeed(0.1f);
    }
}
