package com.example.servicedemo;

import android.os.Binder;
import android.util.Log;

public class MyBinder extends Binder {
  public static void print(String msg){
      Log.d("qqq",msg);
  }
}
