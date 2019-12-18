package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AidlService extends Service {
    public AidlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    class MyBinder extends IMyAidlInterface.Stub{
        @Override
        public UserBean getUserBean() throws RemoteException {
            return new UserBean("aidl",1);
        }
        public void sayHello(String hello){
//            looper一个可以无限的死循环,循环取消息然后调用handler去处理
            Log.d("qqq", Thread.currentThread().getName()+"thread____1");
            if(Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            Log.d("qqq", Thread.currentThread().getName()+"thread____2");

            Toast.makeText(AidlService.this,hello,Toast.LENGTH_LONG).show();
            Looper.loop();

        }
    }
}
