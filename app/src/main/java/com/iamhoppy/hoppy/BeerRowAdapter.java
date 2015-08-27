package com.iamhoppy.hoppy;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.graphics.Bitmap;
import android.widget.ToggleButton;


import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

class BeerRowAdapter extends ArrayAdapter<Beer> {
    private static final String TAG = "com.iamhoppy.hoppy";
    Context context;
    private HttpURLConnection urlConnection;
    URL url;
    private User user;

    BeerRowAdapter(Context context, List beers, User user) {
        super(context, R.layout.custom_beer_row, beers);
        this.context = context;
        this.user = user;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.custom_beer_row, parent, false);
        final Beer singleBeerItem = getItem(position);
        //Reference all views and perform event handling for each
        ImageView breweryLogo = (ImageView)customView.findViewById(R.id.breweryLogo);
        Picasso.with(context)
                .load(singleBeerItem.getBreweryLogoURL())
                .fit()
                .centerInside()
                .into(breweryLogo);

        TextView beerName = (TextView)customView.findViewById(R.id.beerName);
        TextView breweryName = (TextView)customView.findViewById(R.id.breweryName);
        TextView beerType = (TextView)customView.findViewById(R.id.beerType);
        TextView beerABVIBU = (TextView)customView.findViewById(R.id.beerABVIBU);
        TextView averageRating = (TextView)customView.findViewById(R.id.score);

        System.out.println("position=" + position + " singleBeerItem=" + singleBeerItem.getName());

        ToggleButton favoriteToggle = (ToggleButton)customView.findViewById(R.id.favoriteToggle);
        if(singleBeerItem.isFavorited()) {
            favoriteToggle.setChecked(true);
        }
        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final boolean isCheckedFinal = isChecked;
                Intent updateIntent = new Intent(context, UpdateFavorites.class);
                try {
                    updateIntent.putExtra("userID", user.getId());
                    updateIntent.putExtra("beerID", singleBeerItem.getId());
                    updateIntent.putExtra("checkedFinal", isCheckedFinal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getContext().getApplicationContext().startService(updateIntent);
            }
        });

        breweryLogo.setImageResource(R.drawable.alameda);
        beerName.setText(singleBeerItem.getName());
        breweryName.setText(singleBeerItem.getBrewery());
        beerType.setText(singleBeerItem.getType());
        averageRating.setText(String.format("%.1f", singleBeerItem.getAverageRating()));
        if(singleBeerItem.getAbv() != null && singleBeerItem.getIbu() != null) {
            beerABVIBU.setText("ABV " + singleBeerItem.getAbv() + ", IBU " + singleBeerItem.getIbu());
        }

        return customView;
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
