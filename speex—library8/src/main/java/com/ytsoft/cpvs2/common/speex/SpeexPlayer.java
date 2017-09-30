/**
 * 
 */
package com.ytsoft.cpvs2.common.speex;

import java.io.File;

/**
 * @author Gauss
 * 
 */
public class SpeexPlayer {
	private String fileName = null;
	private SpeexDecoder speexdec = null;
	private SpeexFinishListener l;

	private static SpeexPlayer splayer;
	private static boolean isPlaying;

	public SpeexPlayer(String fileName) {
		this.fileName = fileName;
		System.out.println(this.fileName);
		try {
			speexdec = new SpeexDecoder(new File(this.fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void startPlay() {
		RecordPlayThread rpt = new RecordPlayThread();
		Thread th = new Thread(rpt);
		th.start();
	}

	public void stopPlay() {
		speexdec.setPaused(true);
	}

	public boolean isPlay = false;

	class RecordPlayThread extends Thread {
		public void run() {
			try {
				if (speexdec != null)
					speexdec.decode();
				l.finishPlayListener();
			} catch (Exception t) {
				t.printStackTrace();
			}
		}
	};

	public void setOnSpeexFinishListener(SpeexFinishListener l) {
		this.l = l;
	}

	public interface SpeexFinishListener {
		public void finishPlayListener();
	}

	public static void paly(String localPath) {
		String pathTemp = "";
		if (!isPlaying) {
			isPlaying = true;
			splayer = new SpeexPlayer(localPath);
			pathTemp = localPath;
			splayer.setOnSpeexFinishListener(new SpeexFinishListener() {
				@Override
				public void finishPlayListener() {
					splayer.stopPlay();
					isPlaying = false;
				}
			});
			splayer.startPlay();
		} else {
			if (!pathTemp.equals(localPath)) {
				splayer.stopPlay();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isPlaying = true;
				splayer = new SpeexPlayer(localPath);

				splayer.setOnSpeexFinishListener(new SpeexFinishListener() {
					@Override
					public void finishPlayListener() {
						isPlaying = false;
					}
				});
				splayer.startPlay();
			}
		}
	}
}
