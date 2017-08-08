package com.example.xavier.skypetesting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.microsoft.office.sfb.appsdk.AnonymousSession;
import com.microsoft.office.sfb.appsdk.Application;
import com.microsoft.office.sfb.appsdk.ConfigurationManager;
import com.microsoft.office.sfb.appsdk.Conversation;
import com.microsoft.office.sfb.appsdk.Observable;
import com.microsoft.office.sfb.appsdk.SFBException;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    /**
     * Skype related
     */
    ConfigurationManager sfbConfigManager;
    Application sfbApplication;
    AnonymousSession anonymousSession = null;
    Conversation anonymousConversation;
    ConversationPropertyChangeListener conversationPropertyChangeListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (anonymousConversation != null && anonymousConversation.canLeave()) {
            try {
                anonymousConversation.leave();
            } catch (SFBException e) {
                e.printStackTrace();
            }
        }

        try {
            sfbApplication = Application.getInstance(this);
            sfbConfigManager = sfbApplication.getConfigurationManager();

            //set 4G can video chat too
            sfbConfigManager.setRequireWiFiForVideo(false);

            //set max can show how many video channel
            sfbConfigManager.setMaxVideoChannelCount(5);

            sfbConfigManager.enablePreviewFeatures(false);

            sfbConfigManager.setEndUserAcceptedVideoLicense();

            joinMeeting();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (anonymousConversation != null && anonymousConversation.canLeave())
                anonymousConversation.leave();
        } catch (SFBException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void joinMeeting() {
        //Join the meeting.
        Log.v(TAG, "joinMeeting");
        URI meetingUri = URI.create("https://meet.lync.com/popsquare.io/xavier.law/27GZ2D4G");
        try {
            // Set the default device to Speaker
            //this.devicesManager.setActiveEndpoint(DevicesManager.Endpoint.LOUDSPEAKER);

            anonymousSession = sfbApplication.joinMeetingAnonymously("kiosk", meetingUri);
            anonymousConversation = anonymousSession.getConversation();
            MyApplication application = (MyApplication) getApplication();
            application.setAnonymousConversation(anonymousConversation);


            // Conversation begins in Idle state.
            // It will move from Idle->Establishing->InLobby/Established
            // depending on meeting configuration.
            // We will monitor property change notifications for State property.
            // Once the conversation is Established, we will move to the next activity.
            conversationPropertyChangeListener = new ConversationPropertyChangeListener();
            anonymousConversation.addOnPropertyChangedCallback(conversationPropertyChangeListener);
        } catch (SFBException e) {
            e.printStackTrace();
        }
    }

    private class ConversationPropertyChangeListener extends Observable.OnPropertyChangedCallback {

        @Override
        public void onPropertyChanged(Observable observable, int propertyId) {
            Log.v(TAG, "onPropertyChanged");
            if (propertyId == Conversation.STATE_PROPERTY_ID) {
                updateConversationState();
            }
        }
    }

    private void updateConversationState() {
        Log.v(TAG, "updateConversationState");
        Conversation.State state = anonymousConversation.getState();
        switch (state) {
            case ESTABLISHED:
                break;
            case IDLE:
                if (anonymousConversation != null) {
                    anonymousConversation.removeOnPropertyChangedCallback(conversationPropertyChangeListener);
                    anonymousConversation = null;
                }
                break;
            default:
        }
    }
}
