package com.atar.interfaces;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * ****************************************************************************************************************************************************************************
 *
 * @author:Atar
 * @createTime: 2018/8/24 下午1:40
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public interface ClassLoaderListener {

    void onCreate(Bundle savedInstanceState);

    void onRestart();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void initControl(View view);

    void initValue();

    void bindEvent();

    void onChangeSkin(int skinType);
}
