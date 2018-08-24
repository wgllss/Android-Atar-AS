package com.atar.activitys.demos;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.reflection.ThreadPoolTool;
import android.skin.SkinResourcesManager;
import android.skin.SkinUtils;
import android.utils.FileUtils;
import android.utils.ShowLog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CommonToast;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.atar.activitys.AtarCommonActivity;
import com.atar.activitys.R;
import com.atar.interfaces.ClassLoaderListener;

import java.io.File;
import java.lang.reflect.Method;

public class DemoLoadSDLayoutActivity extends AtarCommonActivity {

    private String TAG = DemoLoadSDLayoutActivity.class.getSimpleName();

    private TextView txt_load_layout;

    private String apk_sdk_path = Environment.getExternalStorageDirectory() +
            "/software/test_app.apk";

    private Resources mResources;

    private View dynamicView;
    private FrameLayout frame_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_demo_load_sdlayout);
    }

    @Override
    protected void initControl() {
        txt_load_layout = (TextView) findViewById(R.id.txt_load_layout);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
    }

    @Override
    protected void bindEvent() {
        txt_load_layout.setOnClickListener(this);
    }

    @Override
    protected void initValue() {
        setActivityTitle("加载SD卡上布局");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_load_layout:
                ThreadPoolTool.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        loadSkinResources(apk_sdk_path);
                    }
                });
                break;
        }
    }

    private void loadSkinResources(final String skinFilePath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinFilePath);
            Resources superRes = getResources();
            mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            mMyHandler.sendEmptyMessage(0);
            ShowLog.i(TAG, "loadSkinResources");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyHandler mMyHandler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mResources != null) {
                        try {
                            int resID = mResources.getIdentifier("item_yy_test", "layout", "com.example.hello.testapp");
                            XmlResourceParser mXmlResourceParser = mResources.getLayout(resID);
                            if (mXmlResourceParser != null) {
                                View view = LayoutInflater.from(DemoLoadSDLayoutActivity.this).inflate(mXmlResourceParser, frame_layout, false);
                                if (view != null) {
                                    frame_layout.addView(view);
                                    TextView textView = (TextView) view.findViewWithTag("txt_hello");
                                    textView.setText("动态加载布局成功");
                                    textView.setTextColor(Color.BLACK);

                                    ImageView imageview = (ImageView) view.findViewWithTag("img_test");
                                    imageview.setImageResource(R.mipmap.ic_launcher);
//                                    view.setBackgroundColor(Color.parseColor("#DDDDDD"));
                                }
                            }
                        } catch (Exception e) {
                            ShowLog.e(TAG, e);
                        }
                    }
                    break;
            }
        }
    }
}
