package com.example.xavier.skypetesting;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by xavier on 27/7/2017.
 */

public class MultiDexApplication extends Application {
    public MultiDexApplication() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}