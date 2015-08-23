package com.iamhoppy.hoppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BeerProfile extends AppCompatActivity {
    private Beer beer = new Beer();
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_profile);
        final Bundle bundle = getIntent().getExtras();
        beer = (Beer)bundle.getSerializable("beer");
        user = (User)bundle.getSerializable("user");
        populateBeer();
    }

    private void populateBeer() {
        //image
        ImageView breweryLogo = (ImageView)findViewById(R.id.breweryLogo);
        Picasso.with(getApplicationContext())
                .load(beer.getBreweryLogoURL())
                .fit()
                .centerInside()// here you resize your image to whatever width and height you like
                .into(breweryLogo);
        ((TextView)findViewById(R.id.beerName)).setText(beer.getName());
        ((TextView)findViewById(R.id.breweryName)).setText(beer.getBrewery());
        if(beer.getAbv() != null && beer.getIbu() != null) {
            ((TextView) findViewById(R.id.beerABVIBU)).setText("ABV " + beer.getAbv() + ", IBU " + beer.getIbu());
        }
        ((TextView)findViewById(R.id.beerDescription)).setText(beer.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_profile, menu);
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
