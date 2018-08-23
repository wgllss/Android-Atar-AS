package com.atar.activitys.demos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.SpeechRecognizer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.utils.ShowLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.atar.activitys.AtarRefreshScrollViewActivity;
import com.atar.activitys.R;
import com.atar.common.AudioRecognizerDialog;
import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.aip.asrwakeup3.core.params.CommonRecogParams;
import com.baidu.aip.asrwakeup3.core.params.OfflineRecogParams;
import com.baidu.aip.asrwakeup3.core.params.OnlineRecogParams;
import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.recog.MyRecognizer;
import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
import com.baidu.aip.asrwakeup3.core.recog.listener.IRecogListener;

import java.util.Map;

public class BaiduAudioListenerActivity extends AtarRefreshScrollViewActivity implements
        IRecogListener, IStatus {

    private String TAG = BaiduAudioListenerActivity.class.getSimpleName();

    private EditText edt_text;
    private Button btn_start;

//    /**
//     * 对话框界面的输入参数
//     */
//    private DigitalDialogInput input;
//    private ChainRecogListener chainRecogListener;

    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    /*
   * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
   */
    protected boolean enableOffline;
    /**
     * 控制UI按钮的状态
     */
    protected int status;

    private AudioRecognizerDialog mAudioRecognizerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addScrollView(R.layout.activity_baidu_audio_listener);
    }

    @Override
    protected void initScrollControl() {
        edt_text = (EditText) findViewById(R.id.edt_text);
        btn_start = (Button) findViewById(R.id.btn_start);
    }

    @Override
    protected void initScrollBindEvent() {
        super.initScrollBindEvent();
        btn_start.setOnClickListener(this);
    }

    @Override
    protected void initScrollInitValue() {
        super.initScrollInitValue();
        setActivityTitle("百度语音识别");

        // DEMO集成步骤 1.1 新建一个回调类，识别引擎会回调这个类告知重要状态和识别结果
//        IRecogListener listener = new MessageStatusRecogListener(handler);


        // DEMO集成步骤 1.2 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例
//        myRecognizer = new MyRecognizer(this, listener);
//        myRecognizer.setEventListener();

        myRecognizer = new MyRecognizer(this, this);
//        myRecognizer.setEventListener(this);


        if (enableOffline) {
            // 集成步骤 1.3（可选）加载离线资源。offlineParams是固定值，复制到您的代码里即可
            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
            myRecognizer.loadOfflineEngine(offlineParams);
        }
        status = STATUS_NONE;

        mAudioRecognizerDialog = new AudioRecognizerDialog(this);
        mAudioRecognizerDialog.setOnClickCancleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioRecognizerDialog.dismiss();
                stopRecognizer();
            }
        }).setOnClickCompleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioRecognizerDialog.startRecognizingAnimation();
                mAudioRecognizerDialog.dismiss();
                if (status == STATUS_SPEAKING) {
                    stopRecognizer();
//                    speakFinish();
//                    onEndOfSpeech();
                } else {
                    cancel();
                    onAsrFinishError(SpeechRecognizer.ERROR_NO_MATCH, 0, null, null);
                }

            }
        }).setOnClickTryAgainListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioRecognizerDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start:
                switch (status) {
                    case STATUS_NONE: // 初始状态
                        start();
                        status = STATUS_WAITING_READY;
//                        updateBtnTextByStatus();
//                        txtLog.setText("");
//                        txtResult.setText("");
                        break;
                    case STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
                    case STATUS_READY: // 引擎准备完毕。
                    case STATUS_SPEAKING: // 用户开始讲话
                    case STATUS_FINISHED: // 一句话识别语音结束
                    case STATUS_RECOGNITION: // 识别中
                        stopRecognizer();
                        status = STATUS_STOPPED; // 引擎识别中
//                        updateBtnTextByStatus();
                        break;
                    case STATUS_LONG_SPEECH_FINISHED: // 长语音识别结束
                    case STATUS_STOPPED: // 引擎识别中
                        cancel();
                        status = STATUS_NONE; // 识别结束，回到初始状态
//                        updateBtnTextByStatus();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
//                case STATUS_FINISHED:
//                    if (msg.arg2 == 1) {
//                        edt_text.setText(msg.obj.toString());
//                    }
//                    status = msg.what;
////                    updateBtnTextByStatus();
//                    break;
//                case STATUS_NONE:
//                case STATUS_READY:
//                case STATUS_SPEAKING:
//                case STATUS_RECOGNITION:
//                    status = msg.what;
////                    updateBtnTextByStatus();
//                    break;
//                default:
//                    break;
//
//            }
//        }
//    };

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = fetchParams();
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印

        // 复制此段可以自动检测常规错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck
                        // .obtainAllMessage();
//                        txtLog.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, enableOffline)).checkAsr(params);
        mAudioRecognizerDialog.startInitializingAnimation();
        mAudioRecognizerDialog.show();
        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }

    protected Map<String, Object> fetchParams() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //  上面的获取是为了生成下面的Map， 自己集成时可以忽略
        Map<String, Object> params = apiParams.fetch(sp);
        //  集成时不需要上面的代码，只需要params参数。
        return params;
    }

    /*
   * Api的参数类，仅仅用于生成调用START的json字符串，本身与SDK的调用无关
   */
    private final CommonRecogParams apiParams = new OnlineRecogParams(this);


    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stopRecognizer() {
        // DEMO集成步骤4 (可选） 停止录音
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancel() {
        status = STATUS_NONE;
        // DEMO集成步骤5 (可选） 取消本次识别
        myRecognizer.cancel();
    }

    /**
     * 销毁时需要释放识别资源。
     */
    @Override
    protected void onDestroy() {
        // DEMO集成步骤3 释放资源
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        myRecognizer.release();
//        Log.i(TAG, "onDestory");
        super.onDestroy();
    }


    @Override
    public void onAsrReady() {
        status = STATUS_READY;
        mAudioRecognizerDialog.startPreparingAnimation();
        ShowLog.i(TAG, "onAsrReady");
    }

    @Override
    public void onAsrBegin() {
        status = STATUS_SPEAKING;
        mAudioRecognizerDialog.startRecordingAnimation();
        ShowLog.i(TAG, "onAsrBegin");
    }

    @Override
    public void onAsrEnd() {
        status = STATUS_RECOGNITION;
        mAudioRecognizerDialog.startRecognizingAnimation();
        ShowLog.i(TAG, "onAsrEnd");
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        ShowLog.i(TAG, "onAsrPartialResult");
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        ShowLog.i(TAG, "onAsrOnlineNluResult");
    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        status = STATUS_NONE;
        mAudioRecognizerDialog.stopRecognizingAnimation();
        String message = "识别结束，结果是”" + results[0] + "”";
        edt_text.setText(results[0]);
        ShowLog.i(TAG, message);
        String msg = recogResult.getOrigalJson();
        ShowLog.i(TAG, "原始json：" + recogResult.getOrigalJson());

        ShowLog.i(TAG, "onAsrFinalResult");

    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        ShowLog.i(TAG, "onAsrFinish");
    }

    protected static final int ERROR_NONE = 0;
    private static final int ERROR_NETWORK_UNUSABLE = 0x90000;

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult
            recogResult) {
        ShowLog.i(TAG, "onAsrFinishError errorCode:" + errorCode + "  subErrorCode:" +
                subErrorCode);
        if (errorCode != ERROR_NONE) {
            boolean showHelpBtn = false;
            String mErrorRes = "";
            switch (errorCode) {
                case SpeechRecognizer.ERROR_NO_MATCH:
                    mErrorRes = "没有匹配的识别结果";
                    break;
                case SpeechRecognizer.ERROR_AUDIO:
                    mErrorRes = "启动录音失败";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    mErrorRes = "未检测到语音， 请重试";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    SpannableString spanString = new SpannableString("网络超时，再试一次");
                    URLSpan span = new URLSpan("#") {
                        @Override
                        public void onClick(View widget) {
                            mAudioRecognizerDialog.startInitializingAnimation();
                        }
                    };
                    spanString.setSpan(span, 5, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mErrorRes = "网络超时，再试一次";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    if (errorCode == ERROR_NETWORK_UNUSABLE) {
                        mErrorRes = "网络不可用， 请检查后重试";
                    } else {
                        mErrorRes = "网络不稳定， 请稍后重试";
                    }
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    mErrorRes = "客户端错误";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    mErrorRes = "权限不足，请检查设置";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    mErrorRes = "引擎忙";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    mErrorRes = "未能识别， 请重试";
                    break;
                default:
                    mErrorRes = "未能识别， 请重试";
                    break;
            }
        }
    }

    @Override
    public void onAsrLongFinish() {
        ShowLog.i(TAG, "onAsrLongFinish");
    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {
        ShowLog.i(TAG, "onAsrVolume-volumePercent--->" + volumePercent + "--volume-->" + volume);
        mAudioRecognizerDialog.setCurrentDBLevelMeter(volumePercent);
    }

    @Override
    public void onAsrAudio(byte[] data, int offset, int length) {
        ShowLog.i(TAG, "onAsrAudio");
    }

    @Override
    public void onAsrExit() {
        ShowLog.i(TAG, "onAsrExit");
    }

    @Override
    public void onOfflineLoaded() {
        ShowLog.i(TAG, "onOfflineLoaded");
    }

    @Override
    public void onOfflineUnLoaded() {
        ShowLog.i(TAG, "onOfflineUnLoaded");
    }
}
