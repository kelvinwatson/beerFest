package com.iamhoppy.hoppy;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    private TextView info;
    private String defaultEventBeerData;
    public String userID;
    public String userToken;
    private ProfileTracker profileTracker;
    private AccessToken accessTokenTracker;

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
            // broadcast is detected from FetchDefaultEventAllBeers class
            defaultEventBeerData = intent.getStringExtra("DefaultEventBeerData");
            Intent loginIntent = new Intent(MainActivity.this, DefaultEventAllBeers.class);
            loginIntent.putExtra("DefaultEventBeerData", defaultEventBeerData);
            startActivity(loginIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Initialize Facebook SDK */
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //String kh = FacebookSdk.getApplicationSignature(getApplicationContext());
        if(isLoggedIn()){
            getFacebookProfile();
        } else {
            ProgressBar progBar = (ProgressBar)findViewById(R.id.progressBar);
            progBar.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            callbackManager = CallbackManager.Factory.create();
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    Log.v("facebook - profile", profile2.getFirstName());
                    System.out.println(isLoggedIn());
                    if(isLoggedIn()) {
                        getFacebookProfile();
                    }
                    profileTracker.stopTracking();
                }
            };
            profileTracker.startTracking();

            loginButton.setReadPermissions("public_profile", "email", "user_friends");

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Button)findViewById(R.id.login_button)).setVisibility(View.INVISIBLE);
                    ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

                    /* Callback registration*/
                    LoginManager.getInstance().registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    userID = loginResult.getAccessToken().getUserId();
                                    userToken = loginResult.getAccessToken().getToken();
                                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "user_friends"));
                                    Log.e("-->", Arrays.asList("public_profile", "user_friends").toString());
                                    Toast.makeText(getApplication(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    getFacebookProfile();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(getApplication(), "fail", Toast.LENGTH_SHORT).show();
                                    ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                    ((Button)findViewById(R.id.login_button)).setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(FacebookException exception) {
                                    Toast.makeText(getApplication(), "error", Toast.LENGTH_SHORT).show();
                                    ((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                    ((Button)findViewById(R.id.login_button)).setVisibility(View.VISIBLE);
                                }
                            });
                }
            });
        }
    }

    /* Get FB credentials */
    private void getFacebookProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        getData(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /* Retrieve startUp data */
    private void getData(JSONObject object) {
        IntentFilter filter = new IntentFilter("com.iamhoppy.hoppy.beers");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
        /* Start the service to get all beers for default event */
        Intent fetchIntent = new Intent(this, FetchDefaultEventAllBeers.class);
        try {
            String[] nameParts = object.getString("name").split(" ");
            if(nameParts != null) {
                fetchIntent.putExtra("firstName", nameParts[0]);
                fetchIntent.putExtra("lastName", nameParts[1]);
                fetchIntent.putExtra("facebookCredential", object.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startService(fetchIntent);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}