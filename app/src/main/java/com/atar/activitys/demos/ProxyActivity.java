package com.atar.activitys.demos;

import android.activity.CommonActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.interfaces.OnOpenDrawerCompleteListener;
import android.os.Bundle;
import android.os.Environment;
import android.plugin.PluginListener;
import android.plugin.PluginManager;

import com.atar.plugin.ProxyManager;
import com.atar.utils.IntentUtil;

public class ProxyActivity extends CommonActivity implements OnOpenDrawerCompleteListener {

    public static boolean isLoadApk = true;
    private ProxyManager mProxyManager = ProxyManager.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnOpenDrawerCompleteListener(this);
        mProxyManager.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        if (mProxyManager != null) {
            mProxyManager.onRestart();
        }
        super.onRestart();
    }

    @Override
    protected void onStart() {
        if (mProxyManager != null) {
            mProxyManager.onStart();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (mProxyManager != null) {
            mProxyManager.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mProxyManager != null) {
            mProxyManager.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mProxyManager != null) {
            mProxyManager.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mProxyManager != null) {
            mProxyManager.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mProxyManager != null) {
            mProxyManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mProxyManager != null) {
            mProxyManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Resources getResources() {
        if (mProxyManager != null) {
            return mProxyManager.getResources();
        }
        return super.getResources();
    }

    @Override
    public void startActivity(Intent intent) {
        if (mProxyManager != null) {
            super.startActivity(mProxyManager.getIntent(intent));
        }
    }

    @Override
    public void ChangeSkin(int skinType) {
        super.ChangeSkin(skinType);
        if (mProxyManager != null) {
            mProxyManager.onChangeSkin(skinType);
        }
    }

    @Override
    public void onOpenDrawerComplete() {
        IntentUtil.finish(this);
    }

    @Override
    public boolean onMoveRight() {
        return false;
    }

    public static void startProxyActivity(final Context context, final String className) {
        if (isLoadApk) {
            String apk_sdk_path = Environment.getExternalStorageDirectory() + "/software/other-apk-debug.apk";
            PluginManager.getInstance().setContext(context);
            PluginManager.getInstance().loadApk(apk_sdk_path, new PluginListener() {
                @Override
                public void success(Resources pluginResources, PackageInfo pluginPackageArchiveInfo) {
                    if (PluginManager.getInstance().exists(className)) {
                        Intent intent = new Intent(context, ProxyActivity.class);
                        intent.putExtra(ProxyManager.CLASS_NAME, className);
                        IntentUtil.startOtherActivity(context, intent);
                    }
                }

                @Override
                public void fail() {

                }
            });
        } else {
            try {
                Intent intent = new Intent(context, Class.forName(className));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                IntentUtil.startOtherActivity(context, intent);
            } catch (Exception e) {

            }
        }
    }
}
