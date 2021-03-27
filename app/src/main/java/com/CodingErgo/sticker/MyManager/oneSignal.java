package com.CodingErgo.sticker.MyManager;

import android.content.Context;

import com.bumptech.glide.load.engine.Initializable;
import com.onesignal.OneSignal;

public class oneSignal {
    private static final String ONESIGNAL_APP_ID = "63460680-58eb-4fe3-81a6-1d560e8e11a7";
    public static void  SignalInitializable(Context context) {

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(context);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

     }
}
