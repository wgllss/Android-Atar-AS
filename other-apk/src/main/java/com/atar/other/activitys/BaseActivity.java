package com.atar.other.activitys;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.atar.plugin.PluginInterface;

public class BaseActivity extends FragmentActivity implements PluginInterface {
    private boolean isPlugin = false;

    protected FragmentActivity thisContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!isPlugin) {
            thisContext = this;
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void attachContext(FragmentActivity context) {
        isPlugin = true;
        thisContext = context;
    }

    @Override
    public void onRestart() {
        if (!isPlugin) {
            super.onRestart();
        }
    }

    @Override
    public void onStart() {
        if (!isPlugin) {
            super.onStart();
        }
    }

    @Override
    public void onResume() {
        if (!isPlugin) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (!isPlugin) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (!isPlugin) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (!isPlugin) {
            super.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!isPlugin) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!isPlugin) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onChangeSkin(int skinType) {
    }

    @Override
    public void setContentView(int layoutResID) {
        if (isPlugin) {
            thisContext.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (isPlugin) {
            thisContext.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (isPlugin) {
            thisContext.setContentView(view, params);
        } else {
            super.setContentView(view, params);
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if (isPlugin) {
            return thisContext.getLayoutInflater();
        } else {
            return super.getLayoutInflater();
        }
    }

    @Override
    public Window getWindow() {
        if (isPlugin) {
            return thisContext.getWindow();
        } else {
            return super.getWindow();
        }
    }

    @Override
    public View findViewById(int id) {
        if (isPlugin) {
            return thisContext.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if (isPlugin) {
            return thisContext.getClassLoader();
        } else {
            return super.getClassLoader();
        }
    }

    @Override
    public WindowManager getWindowManager() {
        if (isPlugin) {
            return thisContext.getWindowManager();
        } else {
            return super.getWindowManager();
        }
    }


    @Override
    public ApplicationInfo getApplicationInfo() {
        if (isPlugin) {
            return thisContext.getApplicationInfo();
        } else {
            return super.getApplicationInfo();
        }
    }

    @Override
    public void finish() {
        if (isPlugin) {
            thisContext.finish();
        } else {
            super.finish();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isPlugin) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void onBackPressed() {
        if (isPlugin) {
            thisContext.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (isPlugin) {
            thisContext.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }
}
