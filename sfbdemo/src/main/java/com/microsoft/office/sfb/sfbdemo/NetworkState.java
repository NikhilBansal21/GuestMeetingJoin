package com.microsoft.office.sfb.sfbdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nikhilbansal on 21/11/16.
 */
public class NetworkState {

    public static int TYPE_WIFI=1;
    public static int TYPE_MOBILE=2;
    public static int TYPE_NOT_CONNECTED=0;


    public static int getConnectivityStatus(Context context){

        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        if(activeNetwork !=null){

            if(activeNetwork.getType()== ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType()== ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;

        }
        return TYPE_NOT_CONNECTED;

    }


    public static String getConnectivityStatusString(Context context){
        int conn=getConnectivityStatus(context);

        String status=null;

        if(conn==TYPE_WIFI){
            status="Wifi enabled";


        }else if(conn==TYPE_MOBILE){
            status="Mobile Data enabled";


        }else if(conn==TYPE_NOT_CONNECTED){
            status="Not Connected to the Internet";
        }

        return status;
    }

}
