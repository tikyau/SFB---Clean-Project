package com.example.xavier.skypetesting;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.microsoft.office.sfb.appsdk.Conversation;

/**
 * Created by xavier on 27/7/2017.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * Saving the anonymous conversation to be shared across activities.
     */
    private Conversation anonymousConversation = null;

    /**
     * Save the anonymous conversation.
     *
     * @param conversation
     */
    public void setAnonymousConversation(Conversation conversation) {
        anonymousConversation = conversation;
    }

    /**
     * Get the anonymous conversation.
     *
     * @return Conversation conversation.
     */
    public Conversation getAnonymousConversation() {
        return anonymousConversation;
    }
}
