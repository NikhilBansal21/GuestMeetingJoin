/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 */

package com.microsoft.office.sfb.sfbdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.office.sfb.appsdk.AnonymousSession;
import com.microsoft.office.sfb.appsdk.Application;
import com.microsoft.office.sfb.appsdk.ConfigurationManager;
import com.microsoft.office.sfb.appsdk.Conversation;
import com.microsoft.office.sfb.appsdk.Observable;
import com.microsoft.office.sfb.appsdk.SFBException;
import com.microsoft.office.sfb.appsdk.DevicesManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main Activity of the app.
 * The activity provides UI to join the meeting and navigate to the conversations view.
 */
public class MainActivity extends AppCompatActivity implements AsyncResponse  {

    Application application = null;
    ConfigurationManager configurationManager = null;
    DevicesManager devicesManager = null;
    ConversationPropertyChangeListener conversationPropertyChangeListener = null;
    Conversation anonymousConversation = null;
    AnonymousSession anonymousSession = null;
    private Intent conversationsIntent = null;
    boolean meetingJoined = false;
    String meeting1 = "https://meet.vayusfb.com/sibaji/7N0KW93T";
    //Meeting url for bot
    EditText name;
    Button join;
    String result;
    String meetingJoinedName;
    /**
     * Defining the Volley Request queue that handles the URL request concurrently
     */


    URI  meetingUri1;
    String Default = null;
    /**
     * Creating the activity initializes the SDK Application instance.
     *
     * @param savedInstanceState saved instance.
     */
    AsyncRetrieve asyncRetrieve=new AsyncRetrieve();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Async task
         */
        asyncRetrieve.delegate=this;
        asyncRetrieve.execute();

        join= (Button) findViewById(R.id.joinbtn);
        this.application = Application.getInstance(this.getApplication().getApplicationContext());
        this.devicesManager = application.getDevicesManager();
        this.configurationManager = application.getConfigurationManager();

        // This flag will enable certain features that are in preview mode.
        // E.g. Audio / Video capability OnPrem topologies.
        //  this.configurationManager.enablePreviewFeatures(true);

        // Note that the sample enable video over cellular network. This is not the default.
        this.configurationManager.setRequireWiFiForVideo(false);


//        Intent intent = getIntent();
//        String easyPuzzle = intent.getExtras().getString("BotURL");
//        Log.d("This is Bot",">>>>>>"+easyPuzzle);


        // Max video channel count needs to be set to view video for more than one participant.
        //  this.configurationManager.setMaxVideoChannelCount(5);


        // Get UI elements.

        this.conversationsIntent = new Intent(this, ConversationsActivity.class);

        this.updateUiState();






    }



    /**
     * Joining the meeting
     */
    public void onJoinMeetingButtonClick(android.view.View view) {

        // Hide keyboard
        InputMethodHelper.hideSoftKeyBoard(this.getApplication().getApplicationContext(),
                view.getWindowToken());


        SharedPreferences preferences = getSharedPreferences("MyUrl", Context.MODE_PRIVATE);
        String meet = preferences.getString("MyUrl", Default);
        Log.d("Shared worked!!!", ">>>" + meet);

//       if (meet.equals(Default)) {
//            Log .d("API nOT ACTIVE!!!", ">>>" + meet);
//        }

        if (meetingJoined) {
            // Leave the meeting.
            try {
                this.anonymousConversation.leave();
                 this.meetingJoined=false;

            } catch (SFBException e) {
                e.printStackTrace();
            }
        } else {
            name= (EditText) findViewById(R.id.Nameedt);

            meetingJoinedName=name.getText().toString();

            URI meetingUri = URI.create(meet);
            Log.d("This is MURI", ">>>>" + meetingUri);


            // Join meeting and monitor conversation state to determine meeting join completion.
            try {

                this.anonymousSession = this.application.joinMeetingAnonymously(
                        meetingJoinedName, meetingUri);

                this.anonymousConversation = this.anonymousSession.getConversation();
                SFBDemoApplication application = (SFBDemoApplication) getApplication();
                application.setAnonymousConversation(this.anonymousConversation);

                // Conversation begins in Idle state. It will move from Idle->Establishing->InLobby/Established
                // depending on meeting configuration.
                // We will monitor property change notifications for State property.
                // Once the conversation is Established, we will move to the next activity.
                this.conversationPropertyChangeListener = new ConversationPropertyChangeListener();
                this.anonymousConversation.addOnPropertyChangedCallback(this.conversationPropertyChangeListener);
            } catch (SFBException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * onStart
     */
    @Override
    public void onStart() {

        super.onStart();


    }

    @Override
    public void onResume() {


        super.onResume();
    }


    @Override
    protected void onDestroy() {
        this.configurationManager = null;
        this.application = null;

        super.onDestroy();
    }

    /**
     * Navigate to the conversations list view.
     * Note that, the conversations list view is provided only for demonstration purposes.
     * For anonymous meeting join it will always have a single conversation after meeting join is
     * successful.
     *
     * @param view View
     */
    public void onConversationsButtonClick(android.view.View view) {
        this.navigateToConversationsActivity();
    }


    /**
     * Update the UI state.
     */
    public void updateUiState() {
        if (meetingJoined) {

            //  Toast.makeText(this, "Meeting Left", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "Meeting Join", Toast.LENGTH_SHORT).show();
            //conversationStateTextView.setText("");
        }
    }

    /**
     * Navigate to the Conversations activity.
     */
    public void navigateToConversationsActivity() {
        startActivity(this.conversationsIntent);
    }

    /**
     * Determines meeting join state based on conversations state.
     */
    public void updateConversationState() {
        Conversation.State state = this.anonymousConversation.getState();
        // conversationStateTextView.setText(state.toString());
        switch (state) {
            case ESTABLISHED:
                this.meetingJoined = true;
                break;
            case IDLE:
                //  conversationStateTextView.setText("");
                this.meetingJoined = false;
                if (this.anonymousConversation != null) {
                    this.anonymousConversation.removeOnPropertyChangedCallback(this.conversationPropertyChangeListener);
                    this.anonymousConversation = null;
                }
                break;
            default:
        }

        // Refresh the UI
        this.updateUiState();

        if (meetingJoined) {
            this.navigateToConversationsActivity();
        }
    }

    @Override
    public void ProcessFinish(String output) {
        result = output;
        Log.d("This is Bot", ">>>>" + result);
        SharedPreferences preferences = getSharedPreferences("MyUrl", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MyUrl", result);
        editor.commit();
    }

//    @Override
//    public void processFinish(String output) {
//
//
//
//
//    }


    /**
     * Callback implementation for listening for conversation property changes.
     */
    class ConversationPropertyChangeListener extends Observable.OnPropertyChangedCallback {
        /**
         * onProperty changed will be called by the Observable instance on a property change.
         *
         * @param sender     Observable instance.
         * @param propertyId property that has changed.
         */
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (propertyId == Conversation.STATE_PROPERTY_ID) {
                updateConversationState();

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }


}

