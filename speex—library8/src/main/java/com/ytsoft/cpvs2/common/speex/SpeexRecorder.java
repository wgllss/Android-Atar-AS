package com.ytsoft.cpvs2.common.speex;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

public class SpeexRecorder implements Runnable {
	private static String TAG = SpeexRecorder.class.getSimpleName();
	// private Logger log = LoggerFactory.getLogger(SpeexRecorder.class);
	private volatile boolean isRecording;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int packagesize = 160;
	private String fileName = null;
	private Context context;
	private OnRecordSpeexingListener listener;
	private Handler Handler;

	public void setOnRecordSpeexingListener(OnRecordSpeexingListener listener) {
		this.listener = listener;
	}

	public SpeexRecorder(String fileName) {
		super();
		this.fileName = fileName;
	}

	public SpeexRecorder(String fileName, Context context, Handler Handler) {
		super();
		this.fileName = fileName;
		this.context = context;
		this.Handler = Handler;
	}

	public void run() {

		// ���������߳�
		Log.d("zheng", "fileName=" + fileName);
		SpeexEncoder encoder = new SpeexEncoder(this.fileName);
		Thread encodeThread = new Thread(encoder);
		encoder.setRecording(true);
		encodeThread.start();

		synchronized (mutex) {
			while (!this.isRecording) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					throw new IllegalStateException("Wait() interrupted!", e);
				}
			}
		}
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

		int bufferRead = 0;
		int bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding);

		short[] tempBuffer = new short[packagesize];

		AudioRecord recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding, bufferSize);
		if (checkAudioPermission()) {
			Log.i(TAG, "recordInstance.getState()---->" + recordInstance.getState());
			if (AudioRecord.STATE_UNINITIALIZED == recordInstance.getState()) {
				if (Handler != null) {
					Handler.sendEmptyMessage(AudioRecord.STATE_UNINITIALIZED);
				}
				return;
			}
			recordInstance.startRecording();
		} else {
			return;
		}
		while (this.isRecording) {
			// log.debug("start to recording.........");
			bufferRead = recordInstance.read(tempBuffer, 0, packagesize);
			// bufferRead = recordInstance.read(tempBuffer, 0, 320);
			if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
			} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
				throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
			}
			// log.debug("put data into encoder collector....");
			encoder.putData(tempBuffer, bufferRead);
			if (listener != null) {
				listener.onRecordSpeexing();
			}

		}
		recordInstance.stop();
		// tell encoder to stop.
		encoder.setRecording(false);
		recordInstance.release();
		// recordInstance=null;
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

	/**
	 * 正在录音
	 * @author :Atar
	 * @createTime:2014-9-29下午4:25:46
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @description:
	 */
	public interface OnRecordSpeexingListener {
		void onRecordSpeexing();
	}

	/**
	 * 检查录音权限
	 * @author :Atar
	 * @createTime:2014-12-26下午4:50:22
	 * @version:1.0.0
	 * @modifyTime:
	 * @modifyAuthor:
	 * @return
	 * @description:
	 */
	private boolean checkAudioPermission() {
		if (context != null) {
			String permName = "android.permission.RECORD_AUDIO";
			String pkgName = context.getPackageName();
			// 结果为0则表示使用了该权限，-1则表求没有使用该权限
			int reslut = context.getPackageManager().checkPermission(permName, pkgName);
			Log.i(TAG, "checkPermission---RECORD_AUDIO----reslut-->" + reslut);
			if (reslut == PackageManager.PERMISSION_GRANTED) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
