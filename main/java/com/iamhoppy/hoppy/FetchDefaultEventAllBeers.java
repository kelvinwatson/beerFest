package com.iamhoppy.hoppy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Service runs in bg, no UI, user unaware

public class FetchDefaultEventAllBeers extends Service {
    private static final String TAG = "com.iamhoppy.hoppy";
    private HttpURLConnection urlConnection;
    public FetchDefaultEventAllBeers(){
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG,"onStartCommand method called");
        Runnable r = new Runnable() {   //MUST place service code in thread(req'd for Service class)
            @Override
            public void run() {
                //TODO: retrieve all beers for default event from database
                //http://developer.android.com/reference/java/net/HttpURLConnection.html
                //http://stackoverflow.com/questions/8376072/whats-the-readstream-method-i-just-can-not-find-it-anywhere
                try {
                    Log.i(TAG, "in run function!");
                    URL url = new URL("http://45.58.38.34/getDefaultBeers");
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    Log.i(TAG, "URL Error");
                }
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String beerData = readStream(in);
                    Intent intent = new Intent();
                    intent.setAction("com.iamhoppy.hoppy.beers");
                    intent.putExtra("Beers", beerData);
                    sendBroadcast(intent);

                }catch(IOException e){
                    Log.i(TAG, "Read error");
                }finally{
                    urlConnection.disconnect();
                }
            }
        };
        Thread getDefaultEventAllBeersThread = new Thread(r);
        getDefaultEventAllBeersThread.start();
        return Service.START_STICKY;
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            System.out.println(TAG+"bo=:"+bo);
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
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
