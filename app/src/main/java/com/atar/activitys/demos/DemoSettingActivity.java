package com.atar.activitys.demos;

import java.util.ArrayList;
import java.util.List;

import android.activity.ActivityManager;
import android.activity.CommonActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appconfig.AppConfigModel;
import android.appconfig.AppConfigSetting;
import android.content.Context;
import android.content.Intent;
import android.download.DownLoadFileManager;
import android.enums.SkinMode;
import android.os.Environment;
import android.os.Message;
import android.skin.SkinUtils;
import android.utils.MDPassword;
import android.view.View;
import android.widget.CommonToast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.atar.activitys.AtarRefreshListViewActivity;
import com.atar.activitys.R;
import com.atar.adapters.SkinAdapter;
import com.atar.beans.DynamicSkinBean;
import com.atar.config.AppConfigJson;
import com.atar.config.AppConfigUtils;
import com.atar.weex.utils.WeexUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zf.view.UISwitchButton;

/**
 * ****************************************************************************************************************************************************************************
 *
 * @author :Atar
 * @createTime:2017-09-30-上午10:26
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description: ****************************************************************************************************************************************************************************
 */

public class DemoSettingActivity extends AtarRefreshListViewActivity implements OnCheckedChangeListener {

    public UISwitchButton common_ui_switch_button;
    private EditText edt_ip;
    private View view_line;
    private TextView txt_save;

    /**
     * SD卡目录 下载 资源文件 皮肤资源
     */
    private String SD_PATH = Environment.getExternalStorageDirectory() + "/.Android/.cache/.";

    private List<DynamicSkinBean> list = new ArrayList<DynamicSkinBean>();
    private SkinAdapter mSkinAdapter = new SkinAdapter(list);

    @Override
    protected int getResLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initControl() {
        super.initControl();
        edt_ip = (EditText) findViewById(R.id.edt_ip);
        view_line = findViewById(R.id.view_line);
        txt_save = (TextView) findViewById(R.id.txt_save);
        common_ui_switch_button = (UISwitchButton) findViewById(R.id.common_ui_switch_button);
    }


    @Override
    protected void initValue() {
        super.initValue();
        setRefreshMode(Mode.DISABLED);
        if (getCurrentSkinType() == SkinMode.DAY_MODE) {
            common_ui_switch_button.setChecked(false);
        } else {
            common_ui_switch_button.setChecked(true);
        }
        SD_PATH = SD_PATH + MDPassword.getPassword32(getPackageName()) + "/";
        mSkinAdapter.setStrDownloadDir(SD_PATH);
        String configJson = AppConfigModel.getInstance().getString(AppConfigUtils.ANDRIOD_APP_CONFIG_HOME_KEY, AppConfigUtils.getDefaultSetting());
        Gson gson = new Gson();
        try {
            AppConfigJson mAppConfigJson = gson.fromJson(configJson, AppConfigJson.class);
            if (mAppConfigJson != null && mAppConfigJson.getAll_skins() != null && mAppConfigJson.getAll_skins().size() > 0) {
                for (int i = 0; i < mAppConfigJson.getAll_skins().size(); i++) {
                    String[] skin = mAppConfigJson.getAll_skins().get(i).split(",");
                    DynamicSkinBean mDynamicSkinBean = new DynamicSkinBean(skin[0], skin[1], skin[2], skin[3], skin[4]);
                    list.add(mDynamicSkinBean);
                    DownLoadFileManager.getInstance().initTempFilePercent(i, this, skin[0], MDPassword.getPassword32(skin[4]), SD_PATH);
                }
            }
        } catch (Exception e) {
        }
        mSkinAdapter.notifyDataSetChanged();
        setAdapter(mSkinAdapter);

        edt_ip.setText(WeexUtils.IP);
    }

    @Override
    public void onHandlerData(Message msg) {
        super.onHandlerData(msg);
        switch (msg.what) {
            case android.download.DownLoadFileBean.DOWLOAD_FLAG_SUCCESS:
                list.get(msg.arg2).setProgress(100);
                break;
            case android.download.DownLoadFileBean.DOWLOAD_FLAG_ING:
                int progress = (Integer) msg.obj;
                list.get(msg.arg2).setProgress(progress);
                break;
        }
        if (mSkinAdapter != null) {
            mSkinAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        common_ui_switch_button.setOnCheckedChangeListener(this);
        txt_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_save:
                String ip = edt_ip.getText().toString();
                if (ip == null || ip.length() == 0) {
                    CommonToast.show("请输入ip和端口");
                    return;
                }
                AppConfigModel.getInstance().putString("WEEX_IP_KEY", ip, true);
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent);
//                android.os.Process.killProcess(android.os.Process.myPid());
                ActivityManager.getActivityManager().exitApplication();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int skinType = 0;
        if (isChecked) {
            skinType = SkinMode.NIGHT_MODE;
        } else {
            skinType = SkinMode.DAY_MODE;
        }
        AppConfigSetting.getInstance().putInt(SkinMode.SKIN_MODE_KEY, skinType);
        for (Activity activity : ActivityManager.getActivityManager().getActivityStack()) {
            if (activity instanceof CommonActivity) {
                ((CommonActivity) activity).loadSkin(skinType);
            }
        }
    }

    @Override
    public void ChangeSkin(int skinType) {
        super.ChangeSkin(skinType);
        SkinUtils.setTextColor(this, R.string.txt_day_black_night_greywhite_color, skinType, (TextView) findViewById(R.id.txt_yejianmoshi));
        if (mSkinAdapter != null) {
            mSkinAdapter.setSkinType(skinType);
        }
        SkinUtils.setTextColor(this, R.string.common_activity_title_color, skinType, txt_save);
        SkinUtils.setBackgroundColor(this, R.string.common_top_title_bar_bg_color, skinType, view_line, txt_save);
    }
}
