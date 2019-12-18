package com.example.trantiondemo;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FrameAnimatorActivity extends AppCompatActivity {

    private ImageView mIv;
    private AnimationDrawable mBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animator);

        initView();
    }

    public void start(View view) {
        mBackground.start();
    }

    public void stop(View view) {
        mBackground.stop();
    }

    private void initView() {
        mIv = findViewById(R.id.iv);
        mBackground = (AnimationDrawable)mIv.getBackground();
    }

    public void frame_animator_by_code(View view) {
        startActivity(new Intent(FrameAnimatorActivity.this,CodeFrameAnimatorActivity.class));
    }
}
