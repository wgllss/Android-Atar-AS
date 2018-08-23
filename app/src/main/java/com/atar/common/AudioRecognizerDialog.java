package com.atar.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.atar.activitys.R;
import com.atar.utils.ColorFilterGenerator;
import com.atar.widgets.SDKAnimationView;

/**
 * ****************************************************************************************************************************************************************************
 *
 * @author:Atar
 * @createTime: 2018/8/17 上午11:01
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description :
 * **************************************************************************************************************************************************************************
 */
public class AudioRecognizerDialog extends Dialog {
    private SDKAnimationView animationview;
    private TextView txt_complete, txt_cancle, txt_try_again;

    private View.OnClickListener onClickTryAgainListener;
    private View.OnClickListener onClickCancleListener;
    private View.OnClickListener onClickCompleteListener;
    private int mTheme = 0;

    public AudioRecognizerDialog(Activity activity) {
        super(activity, R.style.MyDialog);
        setContentView(R.layout.dialog_audio_recognizer);
        txt_complete = (TextView) getWindow().findViewById(R.id.txt_complete);
        txt_cancle = (TextView) getWindow().findViewById(R.id.txt_cancle);
        txt_try_again = (TextView) getWindow().findViewById(R.id.txt_try_again);
        animationview = (SDKAnimationView) getWindow().findViewById(R.id.animationview);
        animationview.setVisibility(View.INVISIBLE);
        setDialogWindowLayoutParams(activity, this, 0.80f, 0.65f);
        adjustThemeColor();
    }

    public AudioRecognizerDialog setOnClickTryAgainListener(View.OnClickListener
                                                                    onClickTryAgainListener) {
        if (onClickTryAgainListener != null) {
            this.onClickTryAgainListener = onClickTryAgainListener;
            txt_try_again.setOnClickListener(onClickTryAgainListener);
        }
        return this;
    }

    public AudioRecognizerDialog setOnClickCancleListener(View.OnClickListener
                                                                  onClickCancleListener) {
        if (onClickCancleListener != null) {
            this.onClickCancleListener = onClickCancleListener;
            txt_cancle.setOnClickListener(onClickCancleListener);
        }

        return this;
    }

    public AudioRecognizerDialog setOnClickCompleteListener(View.OnClickListener
                                                                    onClickCompleteListener) {
        if (onClickCompleteListener != null) {
            this.onClickCompleteListener = onClickCompleteListener;
            txt_complete.setOnClickListener(onClickCompleteListener);
        }
        return this;
    }

    public void setCurrentDBLevelMeter(float volume) {
        if (animationview != null) {
            animationview.setCurrentDBLevelMeter(volume);
        }
    }

    public void stopRecognizingAnimation() {
        animationview.resetAnimation();
        animationview.setVisibility(View.INVISIBLE);
    }

    public void startRecognizingAnimation() {
        animationview.startRecognizingAnimation();
    }

    public void startPreparingAnimation() {
        animationview.startPreparingAnimation();
    }

    public void startRecordingAnimation() {
        animationview.startRecordingAnimation();
    }

    public void startInitializingAnimation() {
        animationview.setVisibility(View.VISIBLE);
        animationview.startInitializingAnimation();
    }


    public static void setDialogWindowLayoutParams(Activity activity, Dialog dialog, float width,
                                                   float height) {
        Window dialogWindow = dialog.getWindow();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * height); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    /**
     * 根据选定的主题，设置色调
     */
    private void adjustThemeColor() {
        float hue = 0;
//        switch (mTheme) {
//            case BaiduASRDialogTheme.THEME_BLUE_LIGHTBG:
//                hue = 0;
//                break;
//            case BaiduASRDialogTheme.THEME_BLUE_DEEPBG:
//                hue = 0;
//                break;
//            case BaiduASRDialogTheme.THEME_GREEN_LIGHTBG:
//                hue = -108;
//                break;
//            case BaiduASRDialogTheme.THEME_GREEN_DEEPBG:
//                hue = -109;
//                break;
//            case BaiduASRDialogTheme.THEME_RED_LIGHTBG:
//                hue = 148;
//                break;
//            case BaiduASRDialogTheme.THEME_RED_DEEPBG:
//                hue = 151;
//                break;
//            case BaiduASRDialogTheme.THEME_ORANGE_LIGHTBG:
//                hue = -178;
//                break;
//            case BaiduASRDialogTheme.THEME_ORANGE_DEEPBG:
//                hue = -178;
//                break;
//            default:
//                break;
//        }
        ColorMatrix cm = new ColorMatrix();
        ColorFilterGenerator.adjustColor(cm, 0, 0, 0, hue);
        ColorFilter filter = new ColorMatrixColorFilter(cm);
        animationview.setHsvFilter(filter);
    }
}


