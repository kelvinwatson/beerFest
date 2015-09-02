package com.iamhoppy.hoppy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.facebook.appevents.AppEventsLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* Displays Beer List for Default Event */
public class DefaultEventAllBeers extends AppCompatActivity {
    private static final String TAG = "com.iamhoppy.hoppy";
    private List<Beer> beers = new ArrayList<Beer>();
    private List<Beer> favoriteBeers = new ArrayList<Beer>();
    private List<Event> events = new ArrayList<Event>();
    private User user = new User();
    private FavoriteReceiver favoriteReceiver;
    private ReviewReceiver reviewReceiver;
    private String defaultEventBeerData;
    private boolean newlyCreated = true;

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "DefaultEventAllBeers: onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_event_all_beer);

        /* Button to view all beers */
        Button allBeersButton = (Button)findViewById(R.id.allBeersButton);
        allBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListAdapter beerAdapter = new BeerRowAdapter(getApplicationContext(), beers, user);
                final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
                beerList.setAdapter(beerAdapter);
            }
        });

        /* Button to view favorite beers */
        Button favoriteBeersButton = (Button)findViewById(R.id.favoriteBeersButton);
        favoriteBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListAdapter beerAdapter = new BeerRowAdapter(getApplicationContext(), favoriteBeers, user);
                final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
                beerList.setAdapter(beerAdapter);
            }
        });
    }

    /* Broadcast receiver for reviews */
    public class ReviewReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int userId = intent.getIntExtra("userID", 0);
            int beerId = intent.getIntExtra("beerID", 0);
            double rating = intent.getDoubleExtra("rating", 0.0);
            String comment = intent.getStringExtra("comment");
            boolean success = intent.getBooleanExtra("success", false);
            if(success) {
                for(Beer beer : beers) {
                    if(beer.getId() == beerId) {
                        beer.setRating(rating);
                        beer.setMyComment(comment);
                    }
                }
                for(Beer beer : favoriteBeers) {
                    if(beer.getId() == beerId) {
                        beer.setRating(rating);
                        beer.setMyComment(comment);
                    }
                }
            }
        }
    }

    /* Broadcast receiver for favorites */
    public class FavoriteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((Button)findViewById(R.id.favoriteBeersButton)).setClickable(false);
            ((Button)findViewById(R.id.allBeersButton)).setClickable(false);
            int userId = intent.getIntExtra("userID", 0);
            int beerId = intent.getIntExtra("beerID", 0);
            boolean success = intent.getBooleanExtra("success", false);
            boolean added = intent.getBooleanExtra("added", false);
            if(success) {
                if(added) {
                    for(Beer b : beers) {
                        if(b.getId() == beerId) {
                            b.setFavorited(true);
                            favoriteBeers.add(b);
                            break;
                        }
                    }
                } else {
                    for(Beer b : beers) {
                        if(b.getId() == beerId) {
                            b.setFavorited(false);
                            favoriteBeers.remove(b);
                            break;
                        }
                    }
                }
                Collections.sort(favoriteBeers);
                Collections.sort(beers);
            }
            ((Button)findViewById(R.id.favoriteBeersButton)).setClickable(true);
            ((Button)findViewById(R.id.allBeersButton)).setClickable(true);
        }
    }

    /* Parses JSON object and saves to User class */
    private User parseUser(JSONObject startUpDataJSONObj, String param) throws JSONException {
        JSONObject userJSONObj = startUpDataJSONObj.getJSONObject(param);
        User tempUser = new User();
        tempUser.setFacebookCredential(userJSONObj.getString("facebook_credential"));
        tempUser.setFirstName(userJSONObj.getString("first_name"));
        tempUser.setLastName(userJSONObj.getString("last_name"));
        tempUser.setId(userJSONObj.getInt("id"));
        return tempUser;
    }

    /* Parses JSON object and saves to Event class */
    private List<Event> parseEvents(JSONObject startUpDataJSONObj, String param) throws JSONException{
        JSONArray eventsJSONArr = startUpDataJSONObj.getJSONArray(param);
        List<Event> tempEvents = new ArrayList<Event>();
        for(int i=0, len=eventsJSONArr.length(); i<len; i++){
            JSONObject eventObj = eventsJSONArr.getJSONObject(i);
            Event event = new Event();
            if(eventObj.has("id") && !eventObj.isNull("id")) event.setId(eventObj.getInt("id"));
            if(eventObj.has("eventName") && !eventObj.isNull("eventName")) event.setName(eventObj.getString("eventName"));
            if(eventObj.has("eventDate") && !eventObj.isNull("eventDate")) event.setDate(eventObj.getString("eventDate"));
            if(eventObj.has("logoUrl") && !eventObj.isNull("logoUrl")) event.setLogoURL(eventObj.getString("logoUrl"));
            if(eventObj.has("beerCount") && !eventObj.isNull("beerCount")) event.setBeerCount(eventObj.getInt("beerCount"));
            tempEvents.add(event);
        }
        return tempEvents;
    }

    /* Parses JSON object and saves to Beer class */
    private List<Beer> parseBeers(JSONObject startUpDataJSONObj, String param) throws JSONException {
        JSONArray beersJSONArr = startUpDataJSONObj.getJSONArray(param);
        //System.out.println("beersJSONArr="+beersJSONArr.toString());
        List<Beer> tempBeers = new ArrayList<Beer>();
        for(int i=0, len=beersJSONArr.length(); i<len; i++) {
            JSONObject beerObj = beersJSONArr.getJSONObject(i);
            Beer beer = new Beer();
            if (beerObj.has("beerID") && !beerObj.isNull("beerID")) {
                beer.setId(beerObj.getInt("beerID"));
            }
            if (beerObj.has("beerName") && !beerObj.isNull("beerName")) {
                beer.setName(beerObj.getString("beerName"));
            }
            if (beerObj.has("beerType") && !beerObj.isNull("beerType")){
                beer.setType(beerObj.getString("beerType"));
            }
            if (beerObj.has("averageRating") && !beerObj.isNull("averageRating")){
                beer.setAverageRating(beerObj.getDouble("averageRating"));
            }
            if(beerObj.has("beerIBU") && !beerObj.isNull("beerIBU")) {
                beer.setIbu(beerObj.getString("beerIBU"));
            }
            if(beerObj.has("beerABV") && !beerObj.isNull("beerABV")) {
                beer.setAbv(beerObj.getString("beerABV"));
            }
            if(beerObj.has("myRating")){
                beer.setRating(beerObj.getDouble("myRating"));
            }
            if(beerObj.has("breweryName") && !beerObj.isNull("breweryName")) {
                beer.setBrewery(beerObj.getString("breweryName"));
            }
            if(beerObj.has("logoUrl") && !beerObj.isNull("logoUrl")) {
                beer.setBreweryLogoURL(beerObj.getString("logoUrl"));
            }
            if(beerObj.has("beerDescription") && !beerObj.isNull("beerDescription")) {
                beer.setDescription(beerObj.getString("beerDescription"));
            }
            if(beerObj.has("myComment") && !beerObj.getString("myComment").equals("NULL")){
                beer.setMyComment(beerObj.getString("myComment"));
            } //else leave as default null value
            if(beerObj.has("favorited") && !beerObj.isNull("favorited")) {
                beer.setFavorited(beerObj.getBoolean("favorited"));
            } //else leave as default false
            if(beerObj.has("comments") && !beerObj.isNull("comments")){
                JSONArray arr = beerObj.getJSONArray("comments"); //This line causes exception
                List<String> tempComments = new ArrayList<String>();
                for(int j=0, arrLen=arr.length(); j<arrLen; j++){
                    tempComments.add(arr.getString(j));
                }
                beer.setComments(tempComments);
            }
            System.out.println("Test printing the beer object"+beer.toString());
            tempBeers.add(beer);
        }
        return tempBeers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default_event_all_beer, menu);
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

    /* Broadcast receiver for beer data */
    public class BeerDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // broadcast is detected from FetchDefaultEventAllBeers class
            defaultEventBeerData = intent.getStringExtra("DefaultEventBeerData");
             /* Parse data */
            if(defaultEventBeerData != null && !defaultEventBeerData.equals("NULL")) {
                try {
                    JSONObject startUpDataJSONObj = new JSONObject(defaultEventBeerData);
                    beers = parseBeers(startUpDataJSONObj, "beers");
                    favoriteBeers = parseBeers(startUpDataJSONObj, "favorites");
                    events = parseEvents(startUpDataJSONObj, "events");
                    user = parseUser(startUpDataJSONObj, "user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(defaultEventBeerData==null) return;

            /* ReCreate list of beers */
            ListAdapter beerAdapter = new BeerRowAdapter(context, beers, user);
            final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
            beerList.setAdapter(beerAdapter);
        }
    }

    /* Fetch beer data */
    private void getBeerData() {
        BeerDataReceiver beerDataReceiver = new BeerDataReceiver();
        registerReceiver(beerDataReceiver,new IntentFilter("com.iamhoppy.hoppy.beers"));

        /* Start the service to get all beers */
        Intent fetchIntent = new Intent(this, FetchDefaultEventAllBeers.class);
        try {
            if(fetchIntent != null) {
                fetchIntent.putExtra("firstName", user.getFirstName());
                fetchIntent.putExtra("lastName", user.getLastName());
                fetchIntent.putExtra("facebookCredential", user.getFacebookCredential());
                fetchIntent.putExtra("isRefresh", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startService(fetchIntent);
    }


    @Override
    protected void onRestart() { //called when: user switches back from recent apps or home screen launger icon; on back button from second activity
        super.onRestart();
        Log.i(TAG, "DefaultEventAllBeers: onRestart called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "DefaultEventAllBeers: onStart called");
        /* Receiver for UpdateFavorites Service */
        favoriteReceiver = new FavoriteReceiver();
        registerReceiver(favoriteReceiver, new IntentFilter("com.iamhoppy.hoppy.favoriteDone"));
        /* Receiver for UpdateReview Service */
        reviewReceiver = new ReviewReceiver();
        registerReceiver(reviewReceiver, new IntentFilter("com.iamhoppy.hoppy.reviewDone"));
        /* Get default event & beer data from MainActivity, parse, and save data */
        final Bundle bundle = getIntent().getExtras();
        String defaultEventBeerData = bundle.getString("DefaultEventBeerData");
        try {
            JSONObject startUpDataJSONObj = new JSONObject(defaultEventBeerData);
            beers = parseBeers(startUpDataJSONObj, "beers");
            favoriteBeers = parseBeers(startUpDataJSONObj, "favorites");
            events = parseEvents(startUpDataJSONObj, "events");
            user = parseUser(startUpDataJSONObj, "user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bundle==null) return;

        /* Create event spinner */
        SpinnerAdapter eventAdapter = new EventSpinnerAdapter(this, events);
        final Spinner eventSpinner = (Spinner)findViewById(R.id.eventSpinner);
        eventSpinner.setAdapter(eventAdapter);
        System.out.println("eventSpinnerAdapter set");
        eventSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //System.out.println("eventSpinner.getPositionForView(view)=" + eventSpinner.getPositionForView(view));
                        //System.out.println("eventSpinner.getSelectedItemPosition()"+eventSpinner.getSelectedItemPosition());
                        if(position != eventSpinner.getSelectedItemPosition()) { //only fetch if selected position different from current position
                            Toast.makeText(DefaultEventAllBeers.this, "Loading...", Toast.LENGTH_SHORT).show();
                            //call API to fetch all beers
                            Event selectedEvent = (Event) (eventSpinner.getItemAtPosition(position));
                            System.out.println("in listener: selectedEvent.getBeerCount()=" + selectedEvent.getBeerCount());
                            System.out.println("in listener: selectedEvent.getBeerCount()=" + selectedEvent.getBeerCount());
                            Intent fetchIntent = new Intent(DefaultEventAllBeers.this, FetchDefaultEventAllBeers.class);
                            try {
                                if (fetchIntent != null) {
                                    fetchIntent.putExtra("firstName", user.getFirstName());
                                    fetchIntent.putExtra("lastName", user.getLastName());
                                    fetchIntent.putExtra("facebookCredential", user.getFacebookCredential());
                                    fetchIntent.putExtra("eventId", selectedEvent.getId());
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            startService(fetchIntent);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        /* Create list of beers */
        System.out.println("UserID: " + user.getId());
        ListAdapter beerAdapter = new BeerRowAdapter(this, beers, user);
        final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
        beerList.setAdapter(beerAdapter);
        beerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setEnabled(false);
                        Beer selectedBeer = (Beer)(beerList.getItemAtPosition(position));
                        //System.out.println("User selected=" + selectedBeer.getName());
                        Toast.makeText(DefaultEventAllBeers.this, "Loading...", Toast.LENGTH_SHORT).show();
                        Intent viewBeerProfile = new Intent(DefaultEventAllBeers.this, BeerProfile.class);
                        viewBeerProfile.putExtra("beer", selectedBeer);
                        viewBeerProfile.putExtra("user", user);
                        startActivity(viewBeerProfile);
                        view.setEnabled(true);
                    }
                }
        );

        if(!newlyCreated) {
            System.out.println("in onRestart");
            /* Call the fetch service */
            getBeerData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "DefaultEventAllBeers: onPause called");
        newlyCreated = false;
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() { //called when user switches between recent apps, or starts new activity
        super.onStop();
        Log.i(TAG, "DefaultEventAllBeers: onStop called");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "DefaultEventAllBeers: onDestroy called");
        try{
            unregisterReceiver(favoriteReceiver);
        } catch(Exception e){
            e.printStackTrace(); //ignore exception
        }
        try{
            unregisterReceiver(reviewReceiver);
        } catch(Exception e){
            e.printStackTrace(); //ignore exception
        }
    }
}