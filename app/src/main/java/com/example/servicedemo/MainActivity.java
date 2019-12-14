package com.example.servicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        1, 新建一个继承自Service的类MyService，然后在AndroidManifest.xml里注册这个Service
//        2, Activity里面使用bindService方式启动MyService，也就是绑定了MyService,（到这里实现了绑定，Activity与Service通信的话继续下面的步骤）
//        3, 新建一个继承自Binder的类MyBinder
//        4, 在MyService里实例化一个MyBinder对象mBinder，并在onBind回调方法里面返回这个mBinder对象
//        5, 第2步bindService方法需要一个ServiceConnection类型的参数，在ServiceConnection里可以取到一个IBinder对象，就是第4步onBinder返回的mBinder对象（也就是在Activity里面拿到了Service里面的mBinder对象）
//        在Activity里面拿到mBinder之后就可以调用这个binder里面的方法了（也就是可以给Service发消息了），需要什么方法在MyBinder类里面定义实现就行了。如果需要Service给Activity发消息的话，通过这个binder注册一个自定义回调即可。
//
//
        bindService(new Intent(this, MyService.class), this,0);
        MyService myService = new MyService();

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        boolean binderAlive = iBinder.isBinderAlive();
        Log.d("qqq",binderAlive+"service是否存活");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
