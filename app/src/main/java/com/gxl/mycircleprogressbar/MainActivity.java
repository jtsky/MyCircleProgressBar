package com.gxl.mycircleprogressbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.gxl.circleprogress.library.view.MyProgressView;


public class MainActivity extends ActionBarActivity {
    private MyProgressView mProgressView,mProgressView1,mProgressView2;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressView = (MyProgressView) findViewById(R.id.mProgressView);
        mProgressView.setmNumber(200);
        mProgressView.setmNumberColor(Color.GREEN);
        mProgressView.setmNumberSize(80);
        mProgressView.setmStrokeWidth(15);
        mProgressView1 = (MyProgressView) findViewById(R.id.mProgressView1);
        mProgressView1.setmNumber(250);
        mProgressView2 = (MyProgressView) findViewById(R.id.mProgressView2);
        mProgressView2.setmNumber(410);
        mProgressView2.setTextVisible(false);
        mButton = (Button) findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressView.startAnim();
                mProgressView1.startAnim();
                mProgressView2.startAnim();

            }
        });

    }




}
