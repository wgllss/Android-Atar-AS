package com.atar.activitys.demos;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.atar.activitys.AtarCommonActivity;
import com.atar.activitys.R;
import com.atar.widgets.waves.VoiceLineView;

import java.io.File;
import java.io.IOException;

public class VoiceLineActivity extends AtarCommonActivity implements Runnable {

    private VoiceLineView voiceLineView;
    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 120;
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            voiceLineView.setVolume((int) (db));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_voice_line);
    }

    @Override
    protected void initControl() {
        voiceLineView = (VoiceLineView) findViewById(R.id.voiceLineview);
    }

    @Override
    protected void initValue() {
        setActivityTitle("音量曲线");

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (isAlive) {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
