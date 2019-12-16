package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtBind;
    private Button mBtMessenger;
    private Button mBtAidl;
    private Context mContext;
    private Messenger mMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
//        1, 新建一个继承自Service的类MyService，然后在AndroidManifest.xml里注册这个Service
//        2, Activity里面使用bindService方式启动MyService，也就是绑定了MyService,（到这里实现了绑定，Activity与Service通信的话继续下面的步骤）
//        3, 新建一个继承自Binder的类MyBinder
//        4, 在MyService里实例化一个MyBinder对象mBinder，并在onBind回调方法里面返回这个mBinder对象
//        5, 第2步bindService方法需要一个ServiceConnection类型的参数，在ServiceConnection里可以取到一个IBinder对象，就是第4步onBinder返回的mBinder对象（也就是在Activity里面拿到了Service里面的mBinder对象）
//        在Activity里面拿到mBinder之后就可以调用这个binder里面的方法了（也就是可以给Service发消息了），需要什么方法在MyBinder类里面定义实现就行了。如果需要Service给Activity发消息的话，通过这个binder注册一个自定义回调即可。

        initView();
        addListener();
        receiveMessenger();
    }

    private void receiveMessenger() {
        Messenger messenger = new Messenger(new receiveHandler());
    }

    private void addListener() {
        mBtBind.setOnClickListener(this);
        mBtMessenger.setOnClickListener(this);
        mBtAidl.setOnClickListener(this);
    }


    private void initView() {
        mBtBind = findViewById(R.id.bt_bind);
        mBtMessenger = findViewById(R.id.bt_messenger);
        mBtAidl = findViewById(R.id.bt_aidl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bind:
                mContext.bindService(new Intent(mContext, MyService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        //     如果是跨进程的不能通过这个binder直接获取service，会报类的类型转换错误
                        MyService.MyBinder iBinder1 = (MyService.MyBinder) service;
                        MyService service1 = iBinder1.getService();
                        if (service1 != null) {
                            service1.print("hello");
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, BIND_AUTO_CREATE);
                break;
            case R.id.bt_messenger:
//                这种调用方式适用于service是跨进程的，而且只能说单线程的情况，才能实现通讯
                mContext.bindService(new Intent(mContext, MessengerService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mMessenger = new Messenger(service);
                        Message message =new Message();
                        message.what=100;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg","messenger");
                        message.setData(bundle);
                        try {
                            mMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, BIND_AUTO_CREATE);
                break;
            case R.id.bt_aidl:
                break;
        }
    }

    /**
     * 获取Messenger方式交互，从service像activity传输的msg
     */
    private class receiveHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                Toast.makeText(mContext,"从MessengerService向这里传输的消息",Toast.LENGTH_LONG).show();
            }
        }
    }
}
