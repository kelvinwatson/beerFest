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
import java.util.List;

public class DefaultEventAllBeers extends AppCompatActivity {
    public String jsonResponse;
    private List<Beer> beers = new ArrayList<Beer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_event_all_beer);
        Bundle bundle = getIntent().getExtras();
        String beersString = bundle.getString("Beers");
        try {
            JSONArray beersJSON = new JSONArray(beersString);
            for(int i=0; i<beersJSON.length(); i++){
                JSONObject beerObject = beersJSON.getJSONObject(i);
                Beer beer = new Beer();
                beer.setName(beerObject.getString("beerName"));
                //beer.setBrewery(beerObject.getString("brewName"));
                beer.setType(beerObject.getString("beerType"));
                beer.setAbv(beerObject.getDouble("beerABV"));
                beer.setIbu(beerObject.getDouble("beerIBU"));
                beers.add(beer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bundle==null) return;
        String facebookPassedString = bundle.getString("facebookCredential");
        //final TextView facebookText = (TextView)findViewById(R.id.facebookData);
        //facebookText.setText(facebookPassedString);

        //Create spinner
        //http://stackoverflow.com/questions/3609231/how-is-it-possible-to-create-a-spinner-with-images-instead-of-text
        //http://mrbool.com/how-to-customize-spinner-in-android/28286
        //String[] events = {"Fruit Beer Fest", "Oregon Brewers Festival", "Oktoberfest"};
        //String[] eventDates = {"2015-06-12","2015-07-22","2015-09-12"};
        //int eventLogos[] = { R.drawable.eventLogo, R.drawable.eventLogo, R.drawable.eventLogo};

        List<Event> events = new ArrayList<Event>();
        //TODO: Make API Call Get Beers Fill Beer List
        for(int i=0; i<3; i++){
            Event event = new Event();
            event.setLogoURL("www.example.com");
            event.setName("Fake Event" + i);
            event.setDate("Fake Date" + i);
            events.add(event);
        }
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
