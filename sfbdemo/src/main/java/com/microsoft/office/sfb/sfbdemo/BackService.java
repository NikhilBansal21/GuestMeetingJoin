package com.microsoft.office.sfb.sfbdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by nikhilbansal on 07/11/16.
 */

public class BackService extends Service {



   private static String TAG=BackService.class.getSimpleName();
    private MyThread myThread;
    public boolean isRunning=false;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        myThread=new MyThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!isRunning){
            myThread.interrupt();
            myThread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if(!isRunning){
            myThread.start();
            isRunning = true;
    }
}
public void readWebPage(){
    HttpClient client =new DefaultHttpClient();
    HttpGet request=new HttpGet(UrlConfig.URL3);

    //getting the response

    ResponseHandler<String> responseHandler=new BasicResponseHandler();
    String response_str=null;
    try {
        response_str=client.execute(request,responseHandler);
        if(!response_str.equalsIgnoreCase("")){
            Intent intent=new Intent("intentKey");
            intent.putExtra("String Value",response_str);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
          Log.d(TAG, "Got Response------>>>" + response_str);


        }


    } catch (IOException e) {
        e.printStackTrace();
    }

}

    class MyThread extends Thread {
        static final long DELAY = 3000;

        @Override
        public void run() {
           while(isRunning){
               Log.d(TAG, "Running");


               try {
                   readWebPage();
                   Thread.sleep(DELAY);
               } catch (InterruptedException e) {
                   isRunning=false;
                   e.printStackTrace();
               }


           }

        }
    }
}
