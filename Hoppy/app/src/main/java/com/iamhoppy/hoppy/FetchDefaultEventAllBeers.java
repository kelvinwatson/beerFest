package com.iamhoppy.hoppy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

//Service runs in bg, no UI, user unaware

public class FetchDefaultEventAllBeers extends Service {
    private static final String TAG = "com.iamhoppy.hoppy";

    public FetchDefaultEventAllBeers(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG,"onStartCommand method called");
        Runnable r = new Runnable() {   //MUST place service code in thread(req'd for Service class)
            @Override
            public void run() {
                //TODO: retrieve all beers for default event from database
            }
        };
        Thread getDefaultEventAllBeersThread = new Thread(r);
        getDefaultEventAllBeersThread.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy method called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
