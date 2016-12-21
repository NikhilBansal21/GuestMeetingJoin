package com.microsoft.office.sfb.sfbdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by nikhilbansal on 21/11/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

   EventBus eventBus= EventBus.getDefault();


    @Override

    public void onReceive(Context context, Intent intent) {
    String status= NetworkState.getConnectivityStatusString(context);
        String eventData=" "+status;

        eventBus.post(eventData);
    }
}
