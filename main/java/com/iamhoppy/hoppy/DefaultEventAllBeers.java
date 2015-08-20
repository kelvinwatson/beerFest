package com.iamhoppy.hoppy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultEventAllBeers extends AppCompatActivity {
    public String jsonResponse;
    private List<Beer> beers = new ArrayList<Beer>();
    private List<Event> events = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_event_all_beer);
        Bundle bundle = getIntent().getExtras();
        String defaultEventBeerData = bundle.getString("DefaultEventBeerData");
        try {
            JSONObject eventBeersJSONObj = new JSONObject(defaultEventBeerData);

            JSONArray beersJSONArr = eventBeersJSONObj.getJSONArray("beers");
            for(int i=0, len=beersJSONArr.length(); i<len; i++){
                JSONObject beerObj = beersJSONArr.getJSONObject(i);
                Beer beer = new Beer();
                beer.setName(beerObj.getString("beerName"));
                beer.setBrewery(beerObj.getString("breweryName"));
                beer.setBreweryLogoURL(beerObj.getString("logoUrl"));
                beer.setType(beerObj.getString("beerType"));
                beer.setAbv(beerObj.getDouble("beerABV"));
                beer.setIbu(beerObj.getDouble("beerIBU"));
                beer.setDescription(beerObj.getString("beerDescription"));
                beers.add(beer);
            }

            JSONArray eventsJSONArr = eventBeersJSONObj.getJSONArray("events");
            System.out.println("eventsJSONArr="+eventsJSONArr.toString());
            for(int i=0, len=eventsJSONArr.length(); i<len; i++){
                JSONObject eventObj = eventsJSONArr.getJSONObject(i);
                System.out.println("eventObj=" + eventObj.toString());
                Event event = new Event();
                event.setName(eventObj.getString("eventName"));
                event.setDate(eventObj.getString("eventDate"));
                event.setLogoURL(eventObj.getString("logoUrl"));
                System.out.println("event=" + event.toString());
                events.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bundle==null) return;
        String facebookPassedString = bundle.getString("facebookCredential");
        //final TextView facebookText = (TextView)findViewById(R.id.facebookData);
        //facebookText.setText(facebookPassedString);

        //Create spinner
        SpinnerAdapter eventAdapter = new EventSpinnerAdapter(this, events);
        Spinner eventSpinner = (Spinner)findViewById(R.id.eventSpinner);
        eventSpinner.setAdapter(eventAdapter);

        //adapter converts array to list items
        ListAdapter beerAdapter = new BeerRowAdapter(this, beers);
        ListView beerList = (ListView)findViewById(R.id.beerList); //get reference to listview
        beerList.setAdapter(beerAdapter);
        beerList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedBeer = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(DefaultEventAllBeers.this, "Rated!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
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
