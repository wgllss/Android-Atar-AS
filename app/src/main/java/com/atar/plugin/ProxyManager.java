package com.atar.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.plugin.PluginManager;
import android.support.v4.app.FragmentActivity;
import android.utils.ShowLog;
import android.view.View;

import com.atar.activitys.demos.ProxyActivity;

/**
 * ****************************************************************************************************************************************************************************
 * 代理管理器
 *
 * @author:Atar
 * @createTime: 2018/8/27 下午2:17
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public class ProxyManager implements PluginInterface {

    private String TAG = ProxyActivity.class.getSimpleName();

    public static final String CLASS_NAME = "CLASS_NAME_KEY";

    private FragmentActivity activity;
    private PluginInterface pluginInterface;

    public static ProxyManager getInstance(FragmentActivity activity) {
        ProxyManager mProxyManager = new ProxyManager();
        mProxyManager.activity = activity;
        return mProxyManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            if (activity == null) {
                return;
            }
            String className = activity.getIntent().getStringExtra(CLASS_NAME);
            if (PluginManager.getInstance().getPluginDexClassLoader() != null) {
                Class<?> aClass = PluginManager.getInstance().getPluginDexClassLoader().loadClass(className);
                //创建该Activity的示例
                Object newInstance = aClass.newInstance();
                //程序健壮性检查
                if (newInstance instanceof PluginInterface) {
                    pluginInterface = (PluginInterface) newInstance;
                    //将代理Activity的实例传递给三方Activity
                    pluginInterface.attachContext(activity);
                    //创建bundle用来与三方apk传输数据
                    //调用三方Activity的onCreate，
                    pluginInterface.onCreate(savedInstanceState);
                }
            }
        } catch (Exception e) {
            ShowLog.e(TAG, e);
        }
    }

    @Override
    public void attachContext(FragmentActivity context) {
        if (pluginInterface != null) {
            pluginInterface.attachContext(activity);
        }
    }

    @Override
    public void onRestart() {
        if (pluginInterface != null) {
            pluginInterface.onRestart();
        }
    }

    @Override
    public void onStart() {
        if (pluginInterface != null) {
            pluginInterface.onStart();
        }
    }

    @Override
    public void onResume() {
        if (pluginInterface != null) {
            pluginInterface.onResume();
        }
    }

    @Override
    public void onPause() {
        if (pluginInterface != null) {
            pluginInterface.onPause();
        }
    }

    @Override
    public void onStop() {
        if (pluginInterface != null) {
            pluginInterface.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (pluginInterface != null) {
            pluginInterface.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (pluginInterface != null) {
            pluginInterface.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (pluginInterface != null) {
            pluginInterface.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getPluginResources();
    }

    public Intent getIntent(Intent intent) {
        if (activity == null) {
            return intent;
        }
        Intent newIntent = new Intent(activity, ProxyActivity.class);
        newIntent.putExtra(CLASS_NAME, intent.getComponent().getClassName());
        return newIntent;
    }

    @Override
    public void onChangeSkin(int skinType) {
        if (pluginInterface != null) {
            pluginInterface.onChangeSkin(skinType);
        }
    }
}
