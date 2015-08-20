package com.iamhoppy.hoppy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import java.util.List;

class BeerRowAdapter extends ArrayAdapter<Beer> {
    private static final String TAG = "com.iamhoppy.hoppy";


    BeerRowAdapter(Context context, List beers) {
        super(context, R.layout.custom_beer_row, beers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.custom_beer_row, parent, false);
        Beer singleBeerItem = getItem(position);
        ImageView breweryLogo = (ImageView)customView.findViewById(R.id.breweryLogo);

        TextView beerName = (TextView)customView.findViewById(R.id.beerName);
        TextView breweryName = (TextView)customView.findViewById(R.id.breweryName);
        TextView beerType = (TextView)customView.findViewById(R.id.beerType);
        TextView beerABVIBU = (TextView)customView.findViewById(R.id.beerABVIBU);
        TextView score = (TextView)customView.findViewById(R.id.score);
        TextView favorited = (TextView)customView.findViewById(R.id.favorited);


        breweryLogo.setImageResource(R.drawable.alameda);
        beerName.setText(singleBeerItem.getName());
        breweryName.setText(singleBeerItem.getBrewery());
        beerType.setText(singleBeerItem.getType());
        beerABVIBU.setText("ABV "+singleBeerItem.getAbv() + ", IBU " +singleBeerItem.getIbu());
        //beerABVIBU.setText(Double.toString(singleBeerItem.getAbv())+ ", " +Double.toString(singleBeerItem.getIbu()));

        Log.i(TAG, singleBeerItem.toString());

        return customView;
    }
}
