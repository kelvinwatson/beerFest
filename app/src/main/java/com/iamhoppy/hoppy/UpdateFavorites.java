package com.iamhoppy.hoppy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateFavorites extends Service {
    private static final String TAG = "com.iamhoppy.hoppy";
    URL url;
    HttpURLConnection urlConnection;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final boolean isCheckedFinal = intent.getBooleanExtra("checkedFinal",true);
        final int userID = intent.getIntExtra("userID", 0);
        final int beerID = intent.getIntExtra("beerID", 0);
        Log.i(TAG, "IN SERVICE");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    if (isCheckedFinal) {  // Toggle clicked
                        Log.i(TAG, "Add a Favorite!");
                        url = new URL("http://45.58.38.34/addFavorites/" + userID + "/" + beerID);
                    } else {
                        Log.i(TAG, "Remove Favorite!");
                        url = new URL("http://45.58.38.34/removeFavorites/" + userID + "/" + beerID);
                    }
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String response = readStream(in);
                    JSONObject respObj = new JSONObject(response);
                    if(respObj.getBoolean("success")) {
                        Intent intent = new Intent();
                        intent.setAction("com.iamhoppy.hoppy.favoriteDone");
                        intent.putExtra("userID", userID);
                        intent.putExtra("beerID", beerID);
                        intent.putExtra("success", true);
                        intent.putExtra("added", isCheckedFinal);
                        sendBroadcast(intent);
                    }
                    Log.i(TAG, "RESPONSE: " + response);

                    Log.i(TAG, "Connnection established!  http://45.58.38.34/addFavorites/" + userID + "/" + beerID);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        thread.start();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
}
