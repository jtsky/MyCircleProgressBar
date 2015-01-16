package com.gxl.circleprogress.library.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.gxl.circleprogress.library.util.DrawThread;
import com.gxl.circleprogress.library.util.TrackTool;


public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = "MySurfaceView";
	/** 每30ms刷一帧。 */
	private static final long SLEEP_DURATION = 1;
	/** 动画图标。 */
	private Bitmap bitmap;

	// SurfaceView句柄 用来控制SurfaceView
	private SurfaceHolder mSurfaceHolder;
	private TrackTool mTrackTool;
	private DrawThread thread;

	/** 默认未创建，相当于Destory。 */
	private boolean surfaceDestoryed = true;

	public MySurfaceView(Context context) {
		super(context);
		Log.v(TAG, "MySurfaceView1");
		this.setWillNotDraw(false);// 必须
		init();
	}

	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.v(TAG, "MySurfaceView2");
		this.setWillNotDraw(false);// 必须
		init();
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.v(TAG, "MySurfaceView3");
		this.setWillNotDraw(false);// 必须
        this.setBackgroundColor(Color.TRANSPARENT);
		init();

	}

	private void init() {
		Log.v(TAG, "init==================>");
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		// 设置surfaceView背景透明
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		setZOrderOnTop(true);
		mTrackTool = new TrackTool();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(TAG, "-----------------surfaceCreated------------");
		surfaceDestoryed = false;
		// 当返回首页的时候调用
		/*
		 * mTrackTool.setChange(false); mTrackTool.setX(mTrackTool.getStartX());
		 * mTrackTool.setY(mTrackTool.getStartY());
		 */
		showMovie();
		// drawCircle(holder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		Log.v(TAG, "-----------------surfaceChanged------------");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(TAG, "-----------------surfaceDestroyed------------");
		surfaceDestoryed = true;
	}
	

	public void drawTrack() {
		Canvas canvas = null;
		Paint pTmp = new Paint();
		pTmp.setAntiAlias(true);
		pTmp.setColor(Color.RED);
		Paint paint = new Paint();
		// 设置抗锯齿
		paint.setAntiAlias(true);
		paint.setColor(Color.CYAN);
		mTrackTool.start();
		while (mTrackTool.doing()) {
			try {

				// 先计算测量
				mTrackTool.compute();

				// 绘上新图区域
				int x = mTrackTool.getX();
				// float y = (float) physicalTool.getY();
				int y = mTrackTool.getY();

				canvas = mSurfaceHolder.lockCanvas();
				// 设置画布的背景为透明。
				canvas.drawColor(Color.TRANSPARENT,
						android.graphics.PorterDuff.Mode.CLEAR);

				canvas.drawRect(x, y, x, y, pTmp);
				canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2,
						y - bitmap.getHeight() / 2, paint);
				mSurfaceHolder.unlockCanvasAndPost(canvas);
				// Thread.sleep(SLEEP_DURATION);

				if (mTrackTool.isChange()) {
					mTrackTool.setX(x + TrackTool.RUN_STEP);
				} else {
					mTrackTool.setX(x - TrackTool.RUN_STEP);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 清除屏幕内容
		// 直接按"Home"回桌面，SurfaceView被销毁了，lockCanvas返回为null。
		if (surfaceDestoryed == true && canvas != null) {
			canvas = mSurfaceHolder.lockCanvas();
			canvas.drawColor(Color.TRANSPARENT,
					android.graphics.PorterDuff.Mode.CLEAR);
			mSurfaceHolder.unlockCanvasAndPost(canvas);
		}

		// thread.setRunning(false);

	}

	public void setIcon(Bitmap bit) {
		bitmap = bit;
	}

	public void setParms(float CenterX, float CenterY, float r, int stopX,
			int stopY) {
		mTrackTool.setParams(CenterX, CenterY, r, stopX, stopY);
	}

	public void showMovie() {
		mTrackTool.setChange(false);
		mTrackTool.setX(mTrackTool.getStartX());
		mTrackTool.setY(mTrackTool.getStartY());
		if (thread == null) {
			thread = new DrawThread(this);
		} else if (thread.getState() == Thread.State.TERMINATED) {
			thread.setRunning(false);
			thread = new DrawThread(this);
		}
		if (thread.getState() == Thread.State.NEW) {
			thread.start();
		}
	}

}
