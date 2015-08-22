package com.iamhoppy.hoppy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    private TextView info;
    private String defaultEventBeerData;
    public String userID;
    public String userToken;
    private ProfileTracker profileTracker;

    Handler handler = new Handler(){ //updates interface because threads can't
        @Override
        public void handleMessage(Message msg){
            TextView loadingText = (TextView)findViewById(R.id.loadingText);
            loadingText.setText("Loading...");
        }
    };

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Implement code here to be performed when
            // broadcast is detected
            defaultEventBeerData = intent.getStringExtra("DefaultEventBeerData");
            //Parse Objects
            //Enable login button
            ((Button)findViewById(R.id.login_button)).setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.getApplicationSignature(getApplicationContext());

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        String kh = FacebookSdk.getApplicationSignature(getApplicationContext());
        System.out.println("kh=" + kh);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("com.iamhoppy.hoppy.beers");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
        //start the service to get all beers for default event
        Intent fetchIntent = new Intent(this, FetchDefaultEventAllBeers.class);
        startService(fetchIntent);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "user_likes", "user_friends");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //callback registration
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) { //redirect?
                                userID = loginResult.getAccessToken().getUserId();//
                                userToken = loginResult.getAccessToken().getToken();
                                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends"));
                                Log.e("-->", Arrays.asList("public_profile", "user_friends").toString());
                                Toast.makeText(getApplication(), "Login Successful", Toast.LENGTH_SHORT).show();
                                profileTracker = new ProfileTracker() {
                                    @Override
                                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                        Log.v("facebook - profile", profile2.getFirstName());
                                        profileTracker.stopTracking();
                                    }
                                };
                                profileTracker.startTracking();
                            }
                            @Override
                            public void onCancel() {
                                // App code
                                Toast.makeText(getApplication(), "fail", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Toast.makeText(getApplication(), "error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent loginIntent = new Intent(this, DefaultEventAllBeers.class);
            loginIntent.putExtra("DefaultEventBeerData", defaultEventBeerData);
            startActivity(loginIntent);
        }
    }
}