package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("qqq", "onstartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("qqq", "oncreate");

    }

    public void print(String msg){
        Log.d("qqq",msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("qqq", "onDestroy");

    }

  public  class MyBinder extends Binder {
        public MyService getService(){
            try {
                MyService service = MyService.this;
                return service;
            }catch (Exception ex){
                Log.d("qqq",ex.toString());
                return null;
            }


        }
    }

}
