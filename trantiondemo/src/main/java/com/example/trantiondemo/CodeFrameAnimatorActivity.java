package com.example.trantiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CodeFrameAnimatorActivity extends AppCompatActivity {
    private ImageView mIv;
    private AnimationDrawable animationDrawable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_frame_animator);


        mIv = findViewById(R.id.iv);

        //创建一个AnimationDrawable
        animationDrawable1 = new AnimationDrawable();
        //准备好资源图片
        int[] ids = {R.drawable.goods_one,R.drawable.googs_two,R.drawable.goods_three,R.drawable.goods_four};
        //通过for循环添加每一帧动画
        for(int i = 0 ; i < 4 ; i ++){
            Drawable drawable = getResources().getDrawable(ids[i]);
            //设定时长
            animationDrawable1.addFrame(drawable,200);
        }
        animationDrawable1.setOneShot(false);
        //将动画设置到背景上
        mIv.setBackground(animationDrawable1);
        //开启帧动画
    }

    public void start(View view) {
        animationDrawable1.start();

    }

    public void stop(View view) {
        animationDrawable1.stop();
    }

}
