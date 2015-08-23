package com.iamhoppy.hoppy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DefaultEventAllBeers extends AppCompatActivity {
    public String jsonResponse;
    private List<Beer> beers = new ArrayList<Beer>();
    private List<Beer> favoriteBeers = new ArrayList<Beer>();
    private List<Event> events = new ArrayList<Event>();
    private User user = new User();

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_event_all_beer);
        //Receiver for UpdateFavorites Service
        IntentFilter filter = new IntentFilter("com.iamhoppy.hoppy.favoriteDone");
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, filter);
        //Get default event & beer data, parse, and save data
        final Bundle bundle = getIntent().getExtras();
        String defaultEventBeerData = bundle.getString("DefaultEventBeerData");
        try {
            JSONObject startUpDataJSONObj = new JSONObject(defaultEventBeerData);

            beers = parseBeers(startUpDataJSONObj, "beers");
            favoriteBeers = parseBeers(startUpDataJSONObj, "favorites");

            JSONArray eventsJSONArr = startUpDataJSONObj.getJSONArray("events");
            System.out.println("eventsJSONArr="+eventsJSONArr.toString());
            for(int i=0, len=eventsJSONArr.length(); i<len; i++){
                JSONObject eventObj = eventsJSONArr.getJSONObject(i);
                Event event = new Event();
                event.setName(eventObj.getString("eventName"));
                event.setDate(eventObj.getString("eventDate"));
                event.setLogoURL(eventObj.getString("logoUrl"));
                events.add(event);
            }
            JSONObject userJSONObj = startUpDataJSONObj.getJSONObject("user");
            user.setFacebookCredential(userJSONObj.getString("facebook_credential"));
            user.setFirstName(userJSONObj.getString("first_name"));
            user.setLastName(userJSONObj.getString("last_name"));
            user.setId(userJSONObj.getInt("id"));
            System.out.println("UserOBJ: " + userJSONObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bundle==null) return;
        String facebookPassedString = bundle.getString("facebookCredential");
        //final TextView facebookText = (TextView)findViewById(R.id.facebookData);
        //facebookText.setText(facebookPassedString);

        //Create event spinner
        SpinnerAdapter eventAdapter = new EventSpinnerAdapter(this, events);
        Spinner eventSpinner = (Spinner)findViewById(R.id.eventSpinner);
        eventSpinner.setAdapter(eventAdapter);
        System.out.println("eventSpinnerAdapter set");

        //Create list of beers
        System.out.println("UserID: " + user.getId());
        ListAdapter beerAdapter = new BeerRowAdapter(this, beers, user);
        final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
        beerList.setAdapter(beerAdapter);
        beerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String selectedBeer = String.valueOf(parent.getItemAtPosition(position));
                        Beer selectedBeer = (Beer) (beerList.getItemAtPosition(position));
                        //System.out.println("beer id=" + selectedBeer.getId());
                        System.out.println("user selected=" + selectedBeer.getName());
                        Toast.makeText(DefaultEventAllBeers.this, "loading...", Toast.LENGTH_SHORT).show();
                        Intent viewBeerProfile = new Intent(DefaultEventAllBeers.this, BeerProfile.class);
                        viewBeerProfile.putExtra("beer", selectedBeer);
                        viewBeerProfile.putExtra("user", user);
                        startActivity(viewBeerProfile);
                        //really should be intent.putExtra("BeerID",selectedBeer.getId());
                    }
                }
        );

        Button allBeersButton = (Button)findViewById(R.id.allBeersButton);
        allBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListAdapter beerAdapter = new BeerRowAdapter(getApplicationContext(), beers, user);
                final ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
                beerList.setAdapter(beerAdapter);
            }
        });

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

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // broadcast is detected
            ((Button)findViewById(R.id.favoriteBeersButton)).setClickable(false);
            ((Button)findViewById(R.id.allBeersButton)).setClickable(false);
            int userId = intent.getIntExtra("userID", 0);
            int beerId = intent.getIntExtra("beerID", 0);
            boolean success = intent.getBooleanExtra("success", false);
            boolean added = intent.getBooleanExtra("added", false);
            if(success) {
                if(added) {
                    //Copy into favorited list
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

    private List<Beer> parseBeers(JSONObject startUpDataJSONObj, String param) throws JSONException {
        JSONArray beersJSONArr = startUpDataJSONObj.getJSONArray(param);
        List<Beer> tempBeers = new ArrayList<Beer>();
        for(int i=0, len=beersJSONArr.length(); i<len; i++){
            JSONObject beerObj = beersJSONArr.getJSONObject(i);
            Beer beer = new Beer();
            beer.setId(beerObj.getInt("beerID"));
            beer.setName(beerObj.getString("beerName"));
            beer.setBrewery(beerObj.getString("breweryName"));
            beer.setBreweryLogoURL(beerObj.getString("logoUrl"));
            beer.setType(beerObj.getString("beerType"));
            beer.setAbv(beerObj.getString("beerABV"));
            beer.setIbu(beerObj.getString("beerIBU"));
            beer.setDescription(beerObj.getString("beerDescription"));
            if(!beerObj.isNull("favorited")) {
                beer.setFavorited(beerObj.getBoolean("favorited"));
            }
            tempBeers.add(beer);
        }
        return tempBeers;
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

    public static void retrieveData(String JSONdata){
        System.out.println("printing JSONdata:="+JSONdata);
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
}
