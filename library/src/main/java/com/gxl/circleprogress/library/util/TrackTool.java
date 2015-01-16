package com.gxl.circleprogress.library.util;

public class TrackTool {
	private static final String TAG = "TrackTool";
    // 圆盘指针速率
    public static final int RUN_STEP = 3;
    private float centerX, cneterY, r;
	private int stopX;
	private int stopY;
	private int startX, startY;

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public float getCenterX() {
		return centerX;
	}

	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	// private boolean isFirst = true;
	private boolean isChange = false;

	public boolean isChange() {
		return isChange;
	}

	public void setChange(boolean isChange) {
		this.isChange = isChange;
	}

	/*
	 * public boolean isFirst() { return isFirst; }
	 * 
	 * public void setFirst(boolean isFirst) { this.isFirst = isFirst; }
	 */

	private boolean doing;

	public void start() {
		doing = true;
	}

	/* 圆心坐标 以及半径长度 */
	public void setParams(float centerX, float cneterY, float r, int stopX,
			int stopY) {
		this.centerX = centerX;
		this.cneterY = cneterY;
		this.r = r;
		this.stopX = stopX;
		this.stopY = stopY;
		int edge = (int) Math.sqrt(r * r / 2);
		this.x = (int) (centerX - edge);
		this.y = (int) (cneterY + edge);
		this.startX = this.x;
		this.startY = this.y;
	}

	/** 根据当前时间计算小球的X/Y坐标。 */
	public void compute() {

		float y2 = r * r - (centerX - x) * (centerX - x);
		if (y2 < 0) {
			y2 = 0;
		}
		if (isChange) {
			y = (int) (cneterY - Math.sqrt(y2));
		} else {
			y = (int) (cneterY + Math.sqrt(y2));
		}

		if (Math.abs(x - stopX) <= RUN_STEP
				&& Math.abs(y - stopY) < 20) {
			doing = false;
			return;
		}

	}

	private int x, y;

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {

		if (x >= (centerX + r)) {
			isChange = false;
			this.x = x;
		} else if (x <= (centerX - r)) {
			isChange = true;
			this.x = x;
		} else {
			this.x = x;
		}

	}

	/** 反转Y轴正方向。适应手机的真实坐标系。 */
	public float getMirrorY(int parentHeight, int bitHeight) {
		int half = parentHeight >> 1;
		float tmp = half + (half - y);
		tmp -= bitHeight;
		return tmp;
	}

	public boolean doing() {
		return doing;
	}

	public void cancel() {
		doing = false;
	}
}
