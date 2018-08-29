package com.atar.activitys.demos;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.reflection.ThreadPoolTool;
import android.utils.ShowLog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.atar.activitys.AtarCommonActivity;
import com.atar.activitys.R;
import com.atar.interfaces.ClassLoaderListener;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ClassLoaderActivity extends AtarCommonActivity {

    private String TAG = ClassLoaderActivity.class.getSimpleName();

    private String jar_sdk_path = Environment.getExternalStorageDirectory() + "/software/test.dex" +
            ".jar";

    private String apk_sdk_path = Environment.getExternalStorageDirectory() +
            "/software/test_app.apk";
    private String mClassName = "";
    private ClassLoaderListener mClassLoaderListener;

    private FrameLayout root_layout;
    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSkinResources(apk_sdk_path);//异步加载布局
        addContentView(R.layout.activity_class_loader);
        root_layout = (FrameLayout) findViewById(R.id.root_layout);
        initClassLoader(savedInstanceState);
        setActivityTitle("动态加载");
    }

    private void initClassLoader(Bundle savedInstanceState) {
        mClassName = "com.atar.interfaces.impl.TestImplClassLoaderListener";//和导出之前的包名和类名保持一致
        File dexOutputDir = getDir("dex", Context.MODE_PRIVATE);//
        File file = new File(jar_sdk_path);
        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(), dexOutputDir
                .getAbsolutePath(), null, getClassLoader());
        try {
            Class<?> clazz = classLoader.loadClass(mClassName);
//            Constructor<?> constructor = clazz.getConstructor(Context.class);//得到构造器
//            mClassLoaderListener = (ClassLoaderListener) constructor.newInstance(this);//实例化
            mClassLoaderListener = (ClassLoaderListener) clazz.newInstance();//实例化
            mClassLoaderListener.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSkinResources(final String skinFilePath) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
                            String.class);
                    addAssetPath.invoke(assetManager, skinFilePath);
                    Resources superRes = getResources();
                    mResources = new Resources(assetManager, superRes.getDisplayMetrics(),
                            superRes.getConfiguration());
                    mMyHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MyHandler mMyHandler = new MyHandler();

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (mResources != null) {
                        if (root_layout == null || mClassLoaderListener == null) {
                            sendEmptyMessageDelayed(0, 50);
                            break;
                        }
                        try {
                            int resID = mResources.getIdentifier("item_yy_test", "layout", "com" +
                                    ".example.hello.testapp");
                            XmlResourceParser mXmlResourceParser = mResources.getLayout(resID);
                            if (mXmlResourceParser != null) {
                                View view = LayoutInflater.from(ClassLoaderActivity.this).inflate
                                        (mXmlResourceParser, root_layout, false);
                                if (view != null) {
                                    root_layout.addView(view);
                                    if (mClassLoaderListener != null) {
                                        mClassLoaderListener.initControl(view);
                                        mClassLoaderListener.initValue();
                                    }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onRestart();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void ChangeSkin(int skinType) {
        super.ChangeSkin(skinType);
        if (mClassLoaderListener != null) {
            mClassLoaderListener.onChangeSkin(skinType);
        }
    }
}
