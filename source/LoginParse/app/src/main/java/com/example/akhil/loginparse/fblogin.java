package com.example.akhil.loginparse;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.facebook.AppEventsLogger;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class fblogin extends Activity {

    private Button loginButton;
    private Dialog progressDialog;

    public void onCreateParse() {
       // Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "0wcbdjeYZE09yoDti4z3G1KGSXbrCmsso0N36lZ3", "ujP59dnSbYwvBtHX0QHdCFjLX9kGIkqtoLN7GwEN");
        ParseFacebookUtils.initialize(getString(R.string.app_id));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getActionBar();
        assert (ab != null);
            ab.hide();

        setContentView(R.layout.activity_fblogin);
        onCreateParse();
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.akhil.loginparse", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.v("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            proceed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void onLoginButtonClicked() {
        fblogin.this.progressDialog = ProgressDialog.show(
                fblogin.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("public_profile","user_friends");

       // List<String> permissions = Arrays.asList("public_profile", "user_friends", "user_about_me",
         //       "user_relationships", "user_birthday", "user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                fblogin.this.progressDialog.dismiss();
                if (user == null) {
                    Log.d("MyApp",
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp",
                            "User signed up and logged in through Facebook!");
                    proceed();
                } else {
                    Log.d("MyApp",
                            "User logged in through Facebook!");
                    proceed();
                }
            }
        });
    }

    private void proceed() {
        Intent intent = new Intent(this, action.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
