package com.example.servicedemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class MessengerService extends Service {

    private Messenger messenger;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new ProHandler());
        return messenger.getBinder();
    }

    public void sayHelloToMain(){

        Toast.makeText(MessengerService.this,"helloboy",Toast.LENGTH_LONG).show();
    }

    private class ProHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("qqq","接收到messenger发送的信息");
            if (msg.what==100) {
                Log.d("qqq", Thread.currentThread().getName());
                Bundle data = msg.getData();
                String msg1 = data.getString("msg");
                Context context = getApplicationContext();
                Toast.makeText(MessengerService.this,msg1,Toast.LENGTH_LONG).show();
                // 接收到的还是在主线程中
                Log.d("qqq", Thread.currentThread().getName()+"----service");
                // 获取从activity发送的messenger里的msg携带的replyTo信息，通过msg.replyTo获取messenger对象，然后调用messenger.send发送消息给activity
                final Messenger replyTo = msg.replyTo;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Log.d("qqq", Thread.currentThread().getName()+"----timerTask");
                                Message msg = Message.obtain(null, 1, 0, 0);
                                Bundle bundle = new Bundle();
                                bundle.putString("service_reply","hello i'm service");
                                msg.setData(bundle);
                                replyTo.send(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    },3000);
            }
        }
    }
}
