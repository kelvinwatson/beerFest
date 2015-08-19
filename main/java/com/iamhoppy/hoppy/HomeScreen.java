package com.iamhoppy.hoppy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {
    private String beersString;

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
            beersString = intent.getStringExtra("Beers");
            //Parse Objects

            //Enable login button
            ((Button)findViewById(R.id.facebookButton)).setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        IntentFilter filter = new IntentFilter("com.iamhoppy.hoppy.beers");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
        //start the service to get all beers for default event
        Intent fetchIntent = new Intent(this, FetchDefaultEventAllBeers.class);
        startService(fetchIntent);
    }

    public void onClick(View view){
        Intent loginIntent = new Intent(this, DefaultEventAllBeers.class);
        String facebookCredential =  "FACEBOOK USERNAME/TOKEN";
        loginIntent.putExtra("facebookCredential",facebookCredential);
        loginIntent.putExtra("Beers", beersString);
        startActivity(loginIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
