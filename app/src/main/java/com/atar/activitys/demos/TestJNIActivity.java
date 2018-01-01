package com.atar.activitys.demos;

import android.os.Bundle;
import android.view.View;
import android.widget.CommonToast;

import com.atar.activitys.AtarRefreshScrollViewActivity;
import com.atar.activitys.R;

public class TestJNIActivity extends AtarRefreshScrollViewActivity {

    static {
        System.loadLibrary("hello-jni");
    }

    public native String helloFromC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addScrollView(R.layout.activity_test_jni);
    }

    @Override
    protected void initScrollControl() {
        findViewById(R.id.txtjni).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String str = helloFromC();
        CommonToast.show(str);
    }
}
