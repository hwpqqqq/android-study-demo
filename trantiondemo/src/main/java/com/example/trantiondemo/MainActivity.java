package com.example.trantiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtFrame;
    private Button mBtTween;
    private Button mBtValue;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Android 中的动画可以分为以下几类：
//        逐帧动画
//        补间动画
//        属性动画
        mContext = this;
        mBtFrame = findViewById(R.id.bt_frame);
        mBtTween = findViewById(R.id.bt_Tween);
        mBtValue = findViewById(R.id.bt_value);
        mBtFrame.setOnClickListener(this);
        mBtTween.setOnClickListener(this);
        mBtValue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_frame:
                startActivity(new Intent(mContext, FrameAnimatorActivity.class));
                break;
            case R.id.bt_Tween:
                break;
            case R.id.bt_value:
                break;
            default:
        }
    }
}
