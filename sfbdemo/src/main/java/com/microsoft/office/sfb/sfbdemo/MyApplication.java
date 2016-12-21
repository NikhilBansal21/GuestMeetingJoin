package com.microsoft.office.sfb.sfbdemo;

import android.app.Application;

/**
 * Created by nikhil on 11/18/2016.
 */
public class MyApplication extends Application {


    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
    mInstance=this;
    }

    public static synchronized  MyApplication getInstance(){

        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReciever.ConnectivityReceiverListener listener){
        ConnectivityReciever.connectivityReceiverListener= (ConnectivityReciever.ConnectivityReceiverListener) listener;

    }
}
