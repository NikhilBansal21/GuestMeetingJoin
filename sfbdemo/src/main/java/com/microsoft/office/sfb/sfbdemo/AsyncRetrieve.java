package com.microsoft.office.sfb.sfbdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nikhilbansal on 21/12/16.
 */
class AsyncRetrieve extends AsyncTask<Void, Void, String> {
    String botUrl;
    String meeting;
    private Context context;

public AsyncResponse delegate=null;

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;


        try {
            URL url = new URL("http://192.168.0.107:8082/api/v1/webchatservices/meetconference");
            //open connection before running otherwise it will give NullPointer Exception
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line + "/n");
            if (buffer.length() == 0)
                return null;
            inputStream.close();
            return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                JSONObject jsonResponse = json.getJSONObject("ConferenceDetail");
                meeting = jsonResponse.getString("MettingUrl");


                delegate.ProcessFinish(meeting);
                Log.d("Result", ">>>" + meeting);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}