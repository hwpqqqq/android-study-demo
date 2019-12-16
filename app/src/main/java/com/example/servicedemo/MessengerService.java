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
                Bundle data = msg.getData();
                String msg1 = data.getString("msg");
                Context context = getApplicationContext();
                Toast.makeText(MessengerService.this,msg1,Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("qqq","messenger能创建子线程吗?");
                        Message msg = new Message();
                        Messenger messenger = msg.replyTo;
                        try {
                            messenger.send(Message.obtain(null, 1, 0, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
            }
        }
    }
}
