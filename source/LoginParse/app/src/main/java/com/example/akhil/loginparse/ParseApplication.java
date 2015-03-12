package com.example.akhil.loginparse;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by meha on 13/11/14.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "0wcbdjeYZE09yoDti4z3G1KGSXbrCmsso0N36lZ3", "ujP59dnSbYwvBtHX0QHdCFjLX9kGIkqtoLN7GwEN");

        PushService.setDefaultPushCallback(this, Login.class);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
        ParsePush.subscribeInBackground(" ", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push" + e, e);
                }
            }
        });
    }
}