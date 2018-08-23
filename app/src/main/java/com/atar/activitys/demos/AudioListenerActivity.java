package com.atar.activitys.demos;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.utils.ShowLog;
import android.view.View;
import android.widget.Button;
import android.widget.CommonToast;
import android.widget.EditText;

import com.atar.activitys.AtarRefreshScrollViewActivity;
import com.atar.activitys.R;
import com.atar.utils.JsonParser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AudioListenerActivity extends AtarRefreshScrollViewActivity implements
        InitListener, RecognizerDialogListener {

    private String TAG = AudioListenerActivity.class.getSimpleName();

    private EditText edt_text;
    private Button btn_recognize, btn_stop, btn_cancel;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private boolean mTranslateEnable = false;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addScrollView(R.layout.activity_audio_listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    @Override
    protected void initScrollControl() {
        edt_text = (EditText) findViewById(R.id.edt_text);
        btn_recognize = (Button) findViewById(R.id.btn_recognize);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    @Override
    protected void initScrollBindEvent() {
        super.initScrollBindEvent();
        btn_recognize.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    protected void initScrollInitValue() {
        super.initScrollInitValue();
        setActivityTitle("语音听写");
        setRefreshMode(PullToRefreshBase.Mode.DISABLED);

        mIat = SpeechRecognizer.createRecognizer(this, this);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_recognize:
                edt_text.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
//                boolean isShowDialog = mSharedPreferences.getBoolean(
//                        getString(R.string.pref_key_iat_show), true);
//                if (isShowDialog) {
                // 显示听写对话框
                mIatDialog.setListener(this);
                mIatDialog.show();
                CommonToast.show("请开始说话");
//                showTip(getString(R.string.text_begin));
//                } else {
//                    // 不显示听写对话框
//                    ret = mIat.startListening(mRecognizerListener);
//                    if (ret != ErrorCode.SUCCESS) {
//                        showTip("听写失败,错误码：" + ret);
//                    } else {
//                        showTip(getString(R.string.text_begin));
//                    }
//                }
                break;
            case R.id.btn_stop:
                mIat.stopListening();
                CommonToast.show("停止听写");
                break;
            case R.id.btn_cancel:
                mIat.cancel();
                CommonToast.show("取消听写");
                break;
        }
    }

    @Override
    public void onInit(int code) {
        ShowLog.i(TAG, "SpeechRecognizer init() code = " + code);
        if (code != ErrorCode.SUCCESS) {
            CommonToast.show("初始化失败，错误码：" + code);
        }
    }

    @Override
    public void onResult(RecognizerResult results, boolean b) {
        if (mTranslateEnable) {
            printTransResult(results);
        } else {
            printResult(results);
        }
    }

    @Override
    public void onError(SpeechError error) {
        if (mTranslateEnable && error.getErrorCode() == 14002) {
            CommonToast.show(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能");
        } else {
            CommonToast.show(error.getPlainDescription(true));
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        this.mTranslateEnable = mSharedPreferences.getBoolean(this.getString(R.string
//                .pref_key_translate), false);
        if (mTranslateEnable) {
            Log.i(TAG, "translate enable");
            mIat.setParameter(SpeechConstant.ASR_SCH, "1");
            mIat.setParameter(SpeechConstant.ADD_CAP, "translate");
            mIat.setParameter(SpeechConstant.TRS_SRC, "its");
        }

        String lag = "";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "en");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "cn");
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if (mTranslateEnable) {
                mIat.setParameter(SpeechConstant.ORI_LANG, "cn");
                mIat.setParameter(SpeechConstant.TRANS_LANG, "en");
            }
        }
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory
                () + "/msc/iat.wav");
    }

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            CommonToast.show("解析结果失败，请确认是否已开通翻译功能。");
        } else {
            edt_text.setText("原始语言:\n" + oris + "\n目标语言:\n" + trans);
        }
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        edt_text.setText(resultBuffer.toString());
        edt_text.setSelection(edt_text.length());
    }
}
