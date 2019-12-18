package com.example.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtBind;
    private Button mBtMessenger;
    private Button mBtAidl;
    private Context mContext;
    private Messenger mMessenger;
    private Messenger mReceiveMessenger;

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
        mReceiveMessenger = new Messenger(new receiveHandler());
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

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bind:
                // 通过startService开启的服务开启后，activity和service是无法通讯的，所以要让两者通讯只能使用bind方式
                // 同一进程中使用bind方式进行服务绑定，可以实现activity与service的通讯
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
//               使用binder messenger方式开启的服务，service和activity可以跨进程通讯，但是只适用于单线程
                mContext.bindService(new Intent(mContext, MessengerService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mMessenger = new Messenger(service);
                        Log.d("qqq", Thread.currentThread().getName());
                        Message message = new Message();
                        message.what = 100;
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", "hello i'am activity");
                        message.setData(bundle);
                        // replyto指定回复的接收messenger
                        message.replyTo = mReceiveMessenger;
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
                // aidlservice居然没有注册也能行。。。。
                bindService(new Intent(mContext,AidlService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        UserBean userBean = null;
                        try {
                            final AidlService.MyBinder service1 = (AidlService.MyBinder) service;
                            userBean = (service1).getUserBean();
                            Toast.makeText(mContext,userBean.getName()+userBean.getAge(),Toast.LENGTH_LONG).show();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    service1.sayHello("hello aidl,i am activity");
                                }
                            },3000);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);
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
            if (msg.what == 1) {
                Bundle data = msg.getData();
                String service_reply = data.getString("service_reply");
                Toast.makeText(mContext, service_reply, Toast.LENGTH_LONG).show();
            }
        }
    }
}
