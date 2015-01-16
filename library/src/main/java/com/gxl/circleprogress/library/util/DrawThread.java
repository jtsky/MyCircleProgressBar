package com.gxl.circleprogress.library.util;


import android.view.SurfaceView;

import com.gxl.circleprogress.library.view.MySurfaceView;


/**
 * @author Sodino E-mail:sodinoopen@hotmail.com
 * @version Time：2012-6-18 上午03:14:31
 */
public class DrawThread extends Thread {
	private SurfaceView surfaceView;
	private boolean running;

	public DrawThread(SurfaceView surfaceView) {
		this.surfaceView = surfaceView;
	}

	@Override
	public void run() {
		if (surfaceView == null) {
			return;
		}
		
		if (surfaceView instanceof MySurfaceView) {
			//先让线程睡眠500毫秒 避免侧边栏效果和指针效果冲突
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			((MySurfaceView) surfaceView).drawTrack();
		}
	}

	public void setRunning(boolean b) {
		running = b;
	}

	public boolean isRunning() {
		return running;
	}
}