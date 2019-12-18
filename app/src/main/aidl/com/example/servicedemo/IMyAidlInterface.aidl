// IMyAidlInterface.aidl
package com.example.servicedemo;
import com.example.servicedemo.UserBean;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    UserBean getUserBean();
}
