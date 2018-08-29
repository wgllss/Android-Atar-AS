package com.atar.activitys.demos;

import android.activity.CommonActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.atar.plugin.ProxyManager;
import com.atar.utils.IntentUtil;

public class ProxyActivity extends CommonActivity {

    public static boolean isLoadApk = true;
    private ProxyManager mProxyManager = ProxyManager.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public static void startProxyActivity(Context context, String className) {
        if (isLoadApk) {
            Intent intent = new Intent(context, ProxyActivity.class);
            intent.putExtra(ProxyManager.CLASS_NAME, className);
            IntentUtil.startOtherActivity(context, intent);
        } else {
            try {
                Intent intent = new Intent(context, Class.forName(className).getClass());
                IntentUtil.startOtherActivity(context, intent);
            } catch (Exception e) {

            }
        }
    }
}
