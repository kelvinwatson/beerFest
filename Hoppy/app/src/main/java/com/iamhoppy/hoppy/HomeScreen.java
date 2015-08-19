package com.iamhoppy.hoppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    Handler handler = new Handler(){ //updates interface because threads can't
        @Override
        public void handleMessage(Message msg){
            TextView loadingText = (TextView)findViewById(R.id.loadingText);
            loadingText.setText("Loading...");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        //start the service to get all beers for default event
        Intent fetchIntent = new Intent(this, FetchDefaultEventAllBeers.class);
        startService(fetchIntent);
    }

    public void onClick(View view){
        /*
        //Set up for thread to retrieve data from database for DefaultEventAllBeers
        //never update interface within thread
        Runnable r = new Runnable() {
            @Override
            public void run() {
                //Grab data from database for DefaultEventAllBeers
                handler.sendEmptyMessage(0);
            }
        };
        Thread defaultEventData = new Thread(r);
        defaultEventData.start();
        */

        Intent loginIntent = new Intent(this, DefaultEventAllBeers.class);
        String facebookCredential =  "FACEBOOK USERNAME/TOKEN";
        loginIntent.putExtra("facebookCredential",facebookCredential);
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
