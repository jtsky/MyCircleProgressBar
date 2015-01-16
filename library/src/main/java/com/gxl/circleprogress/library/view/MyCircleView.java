package com.gxl.circleprogress.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.gxl.circleprogress.library.R;


public class MyCircleView extends View {
	private float mRadius;
	private float CenterX, CenterY;
	private float mWWidth;
	private int mStrokeWidthSmall;

    public int getmStrokeWidthSmall() {
        return mStrokeWidthSmall;
    }

    public void setmStrokeWidthSmall(int mStrokeWidthSmall) {
        this.mStrokeWidthSmall = mStrokeWidthSmall;
    }

    private int mStrokeWidthBig;
    private int mDistance;
	private int mEdge;
	private static final String TAG = "MyCircleView";

	public MyCircleView(Context context) {
		super(context);
		this.setWillNotDraw(false);// 必须

	}

	public MyCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setWillNotDraw(false);// 必须

	}

	public MyCircleView(Context context, AttributeSet attrs) {

		super(context, attrs);
		this.setWillNotDraw(false);// 必须
        this.setBackgroundColor(Color.TRANSPARENT);

	}

	public void setCircleView(float centerX, float centerY, float radius,
			int Wwidth) {
		this.CenterX = centerX;
		this.CenterY = centerY;
		this.mRadius = radius;
		this.mWWidth = Wwidth;

		// 45度角交点
		mEdge = (int) Math.sqrt(mRadius * mRadius / 2);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mWWidth > 0 && mWWidth <= 480) {
            if(mStrokeWidthSmall == 0)
			mStrokeWidthSmall = 4;
			mStrokeWidthBig = 2;
			mDistance = 10;
		} else if (mWWidth > 480 && mWWidth <= 720) {
            if(mStrokeWidthSmall == 0)
			mStrokeWidthSmall = 6;
			mStrokeWidthBig = 4;
			mDistance = 15;
		} else if (mWWidth > 720 && mWWidth <= 1080) {
            if(mStrokeWidthSmall == 0)
			mStrokeWidthSmall = 8;
			mStrokeWidthBig = 6;
			mDistance = 20;
		}

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setDither(true);
		// Shader mShader = new RadialGradient(0, 0, radius, colors, positions,
		// tile);
		int color1 = Color.parseColor("#6eaf0a");
		int color2 = Color.parseColor("#f89023");
		int color3 = Color.parseColor("#df043e");
		int color4 = Color.parseColor("#9d136b");
		int color5 = Color.parseColor("#2c0946");

		// // 一个材质,打造出一个线性梯度沿著一条线。
		// 采用梯度渐变的效果
		Shader mShader = new SweepGradient(CenterX, CenterY - 5
                , new int[] {
				color1, color2, color3, color4, color5 }, null);
		Matrix matrix = new Matrix();
		matrix.setRotate(135, CenterX, CenterY);
		mShader.setLocalMatrix(matrix);
		paint.setStrokeCap(Paint.Cap.ROUND);//设置圆弧末端形状
		paint.setShader(mShader);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(mStrokeWidthSmall);
		paint.setAntiAlias(true);
		RectF oval = new RectF(CenterX - mRadius, CenterY - mRadius, CenterX
				+ mRadius, CenterY + mRadius);
		
		//画小圆弧
		canvas.drawArc(oval, 135, 270, false, paint);

		// 圆心
		// canvas.drawCircle(CenterX, CenterY, 2, paint);
		paint.setColor(getResources().getColor(R.color.white2));
		paint.setStrokeWidth(mStrokeWidthBig);
		paint.setShader(null);
		// canvas.drawCircle(CenterX, CenterY, mRadius + 10, paint);
		oval.set(CenterX - mRadius - mStrokeWidthBig - mDistance, CenterY - mRadius
				- mStrokeWidthBig - mDistance, CenterX + mRadius + mStrokeWidthBig + mDistance,
				CenterY + mRadius + mStrokeWidthBig + mDistance);
		// 画大圆弧
		canvas.drawArc(oval, 135, 270, false, paint);

		drawPointAndText(CenterX, CenterY, mRadius, canvas);

	}

	private void drawPointAndText(float CenterX, float CenterY, float mRadius,
			Canvas canvas) {
		int textSize = 0;
		int sRadius = 0;
		if (mWWidth > 0 && mWWidth <= 480) {
			textSize = 15;
			sRadius = 4;
		} else if (mWWidth > 480 && mWWidth <= 720) {
			textSize = 20;
			sRadius = 6;
		} else if (mWWidth > 720 && mWWidth <= 1080) {
			textSize = 35;
			sRadius = 8;
		}

		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.white1));
		paint.setAntiAlias(true);
		paint.setTextSize(textSize);
		// 圆弧与X轴交点
		/*canvas.drawCircle(CenterX - mRadius - mStrokeWidthBig / 2, CenterY,
				sRadius, paint);*/
		canvas.drawText("50", CenterX - mRadius - mStrokeWidthBig - textSize
				* 2, CenterY + 10, paint);
		/*canvas.drawCircle(CenterX + mRadius + mStrokeWidthBig / 2, CenterY,
				sRadius, paint);*/
		canvas.drawText("300", CenterX + mRadius + mStrokeWidthBig + textSize,
				CenterY + 10, paint);
		// 圆弧与Y轴交点
		/*canvas.drawCircle(CenterX, CenterY - mRadius - mStrokeWidthBig / 2,
				sRadius, paint);*/
		canvas.drawText("150", CenterX - textSize, CenterY - mRadius
				- mStrokeWidthBig - textSize, paint);
		// canvas.drawCircle(CenterX, CenterY + mRadius + 10, 6, paint);
		// canvas.drawText("270度", CenterX - 20, CenterY + mRadius + 50, paint);

		// 左垂直X轴交点
		/*canvas.drawCircle(CenterX - mEdge - mStrokeWidthBig / 2, CenterY
				- mEdge - 10, sRadius, paint);*/
		canvas.drawText("100",
				CenterX - mEdge - mStrokeWidthBig - textSize * 2 - 10, CenterY
						- mEdge - 10, paint);
		/*canvas.drawCircle(CenterX - mEdge - mStrokeWidthBig / 2, CenterY
				+ mEdge + 10, sRadius, paint);*/
		canvas.drawText("0", CenterX - mEdge - mStrokeWidthBig - textSize * 2,
				CenterY + mEdge + 20, paint);
		// 右垂直X轴交点
		/*canvas.drawCircle(CenterX + mEdge + mStrokeWidthBig / 2, CenterY
				- mEdge - 10, sRadius, paint);*/
		canvas.drawText("200", CenterX + mEdge + mStrokeWidthBig + textSize,
				CenterY - mEdge - 10, paint);
		/*canvas.drawCircle(CenterX + mEdge + mStrokeWidthBig / 2, CenterY
				+ mEdge + 10, sRadius, paint);*/
		canvas.drawText("500", CenterX + mEdge + mStrokeWidthBig + textSize,
				CenterY + mEdge + 20, paint);
	}

}
