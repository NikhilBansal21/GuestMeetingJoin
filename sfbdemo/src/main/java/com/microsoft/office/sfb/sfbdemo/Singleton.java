package com.microsoft.office.sfb.sfbdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by nikhilbansal on 19/12/16.
 */
public class Singleton  {
    private static String TAG = "NetworkManager";
    private static Singleton singleton = null;
    /**
     * This string will hold the result
     */
    String meeting = "";
    /**
     * Url for the api to parse
     */

    String JsonUrl = "http://192.168.0.107:8082/api/v1/webchatservices/meetconference";

    //for volley api
    public RequestQueue requestQueue;



    public Singleton() {

    }



//    public static  void Parse(Context context,String JsonUrl,final VolleyListener listener){
//
//      /**
//       * Creating a JsonObject class for passing the parameters
//       */
//        requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (url, null, new Response.Listener<JSONObject>()
//  }
//

}