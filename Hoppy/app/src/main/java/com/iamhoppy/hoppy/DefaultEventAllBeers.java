package com.iamhoppy.hoppy;

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

import java.util.ArrayList;
import java.util.List;

public class DefaultEventAllBeers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_event_all_beer);
        Bundle facebookData = getIntent().getExtras();
        if(facebookData==null) return;
        String facebookPassedString = facebookData.getString("facebookCredential");
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

        //Create list
        List<Beer> beers = new ArrayList<Beer>();
        //TODO: Make API Call Get Beers Fill Beer List
        for(int i=0; i<10; i++){
            Beer beer = new Beer();
            beer.setAbv(1.1);
            beer.setBrewery("Test Brewery");
            beer.setType("Test Beer Type");
            beer.setIbu(2.2);
            beer.setImgUrl("http://www.fakeimg.com");
            beer.setName("Fake Beer" + i);
            beer.setRating(9.0);
            beers.add(beer);
        }
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
