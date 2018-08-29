package com.atar.plugin;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * ****************************************************************************************************************************************************************************
 * 插件代理接口
 *
 * @author:Atar
 * @createTime: 2018/8/27 上午11:05
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public interface PluginInterface {

    void attachContext(FragmentActivity context);

    void onCreate(Bundle savedInstanceState);

    void onRestart();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    Resources getResources();

//    void startActivity(Intent intent);

//    void initControl(View view);
//
//    void initValue();
//
//    void bindEvent();

    void onChangeSkin(int skinType);
}
