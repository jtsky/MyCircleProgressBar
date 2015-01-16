package com.gxl.circleprogress.library.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.gxl.circleprogress.library.R;
import com.gxl.circleprogress.library.util.UnitUtils;

/**
 * Created by Administrator on 2015/1/15.
 */
public class MyProgressView extends FrameLayout {
    private static final String TAG = "MyProgressView";
    private Context mContext;
    private  MyCircleView mCircleView;
    private  MySurfaceView mSurfaceView;
    private  RiseNumberTextView mRiseNumberTextView;
    //MyProgressView 本身的宽度和高度
    private int mFrameLayoutWidth,mFrameLayoutHeight;

    public boolean isTextVisible() {
        return isTextVisible;
    }

    public void setTextVisible(boolean isTextVisible) {
        this.isTextVisible = isTextVisible;
    }

    private  boolean isTextVisible = true;


    public int getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = UnitUtils.px2dip(mContext, mStrokeWidth);
    }

    //设置圆弧描边的大小
    private  int mStrokeWidth;

    // 屏幕分辨率
    private int mWWidth;

    public int getmNumber() {
        return mNumber;
    }

    public void setmNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    //设定的进度
    private int mNumber;

    public int getmNumberSize() {
        return mNumberSize;
    }

    public void setmNumberSize(int mNumberSize) {
        this.mNumberSize = mNumberSize;
    }
    //设置数字大小
    private int mNumberSize;

    public int getmNumberColor() {
        return mNumberColor;
    }

    public void setmNumberColor(int mNumberColor) {
        this.mNumberColor = mNumberColor;
    }

    //设置数字颜色
    private int mNumberColor;
    //根据进度换算圆弧的角度
    private int mAngle;
    //根据进度换算圆弧的弧度
    private int mRadian;
    // 指针停留X坐标
    private int stopX;
    // 指针停留Y坐标
    private int stopY;

    public Bitmap getmPointer() {
        return mPointer;
    }

    public void setmPointer(Bitmap mPointer) {
        this.mPointer = mPointer;
    }

    private Bitmap mPointer;

    public MyProgressView(Context context) {
        super(context);
    }

    public MyProgressView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
       // this.setBackgroundColor(Color.TRANSPARENT);
        // 获取屏幕宽度
        mWWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        mRiseNumberTextView = new RiseNumberTextView(context,attrs);
        mRiseNumberTextView.setGravity(Gravity.CENTER);
        mCircleView = new MyCircleView(context,attrs);
        mSurfaceView = new MySurfaceView(context,attrs);

        addView(mRiseNumberTextView);
        addView(mCircleView);
        addView(mSurfaceView);
        Log.v(TAG,"==========>MyProgressView");

        // 界面开始绘制之前调用 直接获取宽度和高度则为0 因为控件还没有开始进行绘制
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
               getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                mFrameLayoutWidth = getWidth();
                mFrameLayoutHeight = mFrameLayoutWidth;
                initCircleView();
                if(isTextVisible){
                    mRiseNumberTextView.setVisibility(View.VISIBLE);
                    if(mNumberSize == 0)
                        mNumberSize = 60;

                    mRiseNumberTextView.setTextSize(UnitUtils.px2sp(context,mNumberSize));
                    if(mNumberColor == 0)
                        mNumberColor = Color.BLUE;

                    mRiseNumberTextView.setTextColor(mNumberColor);
                    mRiseNumberTextView.withNumber(mNumber).start();
                    mRiseNumberTextView.setOnEnd(new RiseNumberTextView.EndListener() {
                        @Override
                        public void onEndFinish() {
                            mRiseNumberTextView.setText("" + mNumber);
                        }
                    });
                }else {
                    mRiseNumberTextView.setVisibility(View.INVISIBLE);
                }

            }
        });



    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设定FrameLayout的高度为相对宽度的值
        super.onMeasure(widthMeasureSpec, widthMeasureSpec - 80);
    }

    // 初始化中间圆环
    public void initCircleView() {
        int centerX = mFrameLayoutWidth / 2;
        int centerY = mFrameLayoutHeight / 2;
        //圆弧半径
        int Radius = 0;
        if(mWWidth >0 && mWWidth <= 350){
            Radius = centerY - 35;
            centerY = centerY - 10;
        }else if (mWWidth > 350 && mWWidth <= 650) {
            Radius = centerY - 65;
            centerY = centerY - 20;
        } else if (mWWidth > 650 && mWWidth <= 830) {
            Radius = centerY - 80;
            centerY = centerY - 20;
        } else if (mWWidth > 830 && mWWidth <= 1080) {
            Radius = centerY - 125;
            centerY = centerY - 25;
        }

		/*  等分的情况下的角度计算公式
		 * mRadian = 27 * mNumber / 50;
		 * 0-200pm25之间的角度计算公式
		 * mRadian = 9 * mNumber / 10;
		 * 200-300pm25之间的角度计算公式
		 * mRadian = 9 * mNumber / 20;
		 * //300-500Pm25之间的角度计算公式
		 * mRadian = 9 * mNumber / 40;
		 */

        if (mNumber >= 0 && mNumber <= 200) {
            mRadian = 9 * mNumber / 10; // 0-200pm25之间的角度计算公式
            if (mRadian >= 0 && mRadian <= 45) {
                mAngle = 45 - mRadian;
                stopX = (int) (centerX - Math.cos(Math.toRadians(mAngle))
                        * Radius);
                stopY = (int) (centerY + Math.sin(Math.toRadians(mAngle))
                        * Radius);

            } else if (mRadian > 45 && mRadian <= 135) {
                mAngle = mRadian - 45;
                stopX = (int) (centerX - Math.cos(Math.toRadians(mAngle))
                        * Radius);
                stopY = (int) (centerY - Math.sin(Math.toRadians(mAngle))
                        * Radius);

            } else if (mRadian > 135 && mRadian <= 225) {
                mAngle = 225 - mRadian;
                stopX = (int) (centerX + Math.cos(Math.toRadians(mAngle))
                        * Radius);
                stopY = (int) (centerY - Math.sin(Math.toRadians(mAngle))
                        * Radius);

            }
        } else if (mNumber > 200 && mNumber <= 300) {
            mRadian = 9 * (mNumber - 200) / 20;
            mAngle = 45 - mRadian;
            stopX = (int) (centerX + Math.cos(Math.toRadians(mAngle)) * Radius);
            stopY = (int) (centerY - Math.sin(Math.toRadians(mAngle)) * Radius);

        } else if (mNumber > 300 && mNumber <= 500) {
            mRadian = 9 * (mNumber - 300) / 40;
            mAngle = mRadian;
            stopX = (int) (centerX + Math.cos(Math.toRadians(mAngle)) * Radius);
            stopY = (int) (centerY + Math.sin(Math.toRadians(mAngle)) * Radius);
        } else if (mNumber > 500) {
            mRadian = 9 * (500 - 300) / 40;
            mAngle = mRadian;
            stopX = (int) (centerX + Math.cos(Math.toRadians(mAngle)) * Radius);
            stopY = (int) (centerY + Math.sin(Math.toRadians(mAngle)) * Radius);

        }

        mCircleView.setCircleView(centerX, centerY, Radius, mWWidth);
        mCircleView.setmStrokeWidthSmall(mStrokeWidth);
        mSurfaceView.setParms(centerX, centerY, Radius, stopX, stopY);
        if(mPointer == null){
            mSurfaceView.setIcon(BitmapFactory.decodeResource(getResources(),
                    R.drawable.ring1));
        }else {
            mSurfaceView.setIcon(mPointer);
        }

        mSurfaceView.showMovie();
    }

    public  void startAnim(){
        mSurfaceView.showMovie();
        mRiseNumberTextView.withNumber(mNumber).start();
    }


}
