package com.iamhoppy.hoppy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BeerProfile extends AppCompatActivity {
    private Beer beer = new Beer();
    private User user = new User();
    private List<ImageView> ratingImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_profile);
        final Bundle bundle = getIntent().getExtras();
        beer = (Beer)bundle.getSerializable("beer");
        user = (User)bundle.getSerializable("user");
        populateBeer();

        //Reference images and add to arrays
        ratingImages = new ArrayList<ImageView>();
        ratingImages.add((ImageView)findViewById(R.id.ratingImg1));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg2));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg3));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg4));
        ratingImages.add((ImageView)findViewById(R.id.ratingImg5));

        if(beer.getRating() >= 0) {
            ratingClicked(beer.getRating(), false);
        }
        ratingImages.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(0,true);
            }
        });
        ratingImages.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(1, true);
            }
        });
        ratingImages.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(2, true);
            }
        });
        ratingImages.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(3, true);
            }
        });
        ratingImages.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingClicked(4, true);
            }
        });
    }

    private void ratingClicked(double rating, boolean makeCall) {
        int intRating = (int)rating;
        switch (intRating) {
            case 0: {
                ratingImages.get(0).setClickable(false);
                ratingImages.get(0).setImageResource(R.drawable.rate1);
                ratingImages.get(1).setClickable(true);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setClickable(true);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setClickable(true);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setClickable(true);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 1: {
                ratingImages.get(1).setClickable(false);
                ratingImages.get(1).setImageResource(R.drawable.rate2);
                ratingImages.get(0).setClickable(true);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(2).setClickable(true);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setClickable(true);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setClickable(true);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 2: {
                ratingImages.get(2).setClickable(false);
                ratingImages.get(2).setImageResource(R.drawable.rate3);
                ratingImages.get(0).setClickable(true);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setClickable(true);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(3).setClickable(true);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                ratingImages.get(4).setClickable(true);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 3: {
                ratingImages.get(3).setClickable(false);
                ratingImages.get(3).setImageResource(R.drawable.rate4);
                ratingImages.get(0).setClickable(true);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setClickable(true);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setClickable(true);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(4).setClickable(true);
                ratingImages.get(4).setImageResource(R.drawable.rate5dark);
                break;
            }
            case 4: {
                ratingImages.get(4).setClickable(false);
                ratingImages.get(4).setImageResource(R.drawable.rate5);
                ratingImages.get(0).setClickable(true);
                ratingImages.get(0).setImageResource(R.drawable.rate1dark);
                ratingImages.get(1).setClickable(true);
                ratingImages.get(1).setImageResource(R.drawable.rate2dark);
                ratingImages.get(2).setClickable(true);
                ratingImages.get(2).setImageResource(R.drawable.rate3dark);
                ratingImages.get(3).setClickable(true);
                ratingImages.get(3).setImageResource(R.drawable.rate4dark);
                break;
            }
            default: { //rate null
                //no action
            }
        }
        if(makeCall) {
            Intent updateIntent = new Intent(getApplicationContext(), UpdateReview.class);
            try {
                updateIntent.putExtra("userID", user.getId());
                updateIntent.putExtra("beerID", beer.getId());
                updateIntent.putExtra("rating", rating);
                updateIntent.putExtra("comment", "NULL");
            } catch (Exception e) {
                e.printStackTrace();
            }
            getApplicationContext().startService(updateIntent);
        }

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
