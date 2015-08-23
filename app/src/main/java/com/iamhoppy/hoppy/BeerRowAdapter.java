package com.iamhoppy.hoppy;

import android.content.Context;
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

    BeerRowAdapter(Context context, List beers) {
        super(context, R.layout.custom_beer_row, beers);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.custom_beer_row, parent, false);
        Beer singleBeerItem = getItem(position);
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
        TextView score = (TextView)customView.findViewById(R.id.score);

        System.out.println("position=" + position + " singleBeerItem=" + singleBeerItem.getName());

        ToggleButton favoriteToggle = (ToggleButton)customView.findViewById(R.id.favoriteToggle);
        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {  // Toggle clicked
                    try {
                        Log.i(TAG, "Toggle to favorite!");
                        url = new URL("http://45.58.38.34/toggleFavorite");
                        //need to pass in user ID and beer ID
                        //check if the beer was favorited for that individual
                        urlConnection = (HttpURLConnection) url.openConnection();
                        Log.i(TAG, "Connnection established!");
                    } catch (IOException e) {
                        Log.i(TAG, "URL Error");
                    } finally {
                        urlConnection.disconnect();
                    }
                } else {            // Toggle not clicked, no action?
                }
            }
        });
//        Rect delegateArea = new Rect();
//        favoriteToggle.getHitRect(delegateArea);
//        delegateArea.right += 100;
//        delegateArea.left += 100;
//        delegateArea.top += 50;
//        delegateArea.bottom += 50;
//        TouchDelegate touchDelegate = new TouchDelegate(delegateArea, favoriteToggle);
//
//        ((View) favoriteToggle.getParent()).setTouchDelegate(touchDelegate);

        //deal with image view


        /*
        try{
            URL logoURL = new URL(singleBeerItem.getBreweryLogoURL());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            InputStream is = logoURL.openConnection().getInputStream();
            Bitmap preview_bitmap = BitmapFactory.decodeStream(is, null, options);
            //i.setImageDrawable(d);
        }catch(MalformedURLException e){
            System.out.println("111HAHA ERROR Error!111");
            e.printStackTrace();
        }catch(IOException e){
            System.out.println("222HAHA ERROR Error!222");
            e.printStackTrace();
        }*/
        breweryLogo.setImageResource(R.drawable.alameda);
        beerName.setText(singleBeerItem.getName());
        breweryName.setText(singleBeerItem.getBrewery());
        beerType.setText(singleBeerItem.getType());
        if(singleBeerItem.getAbv() != null && singleBeerItem.getIbu() != null) {
            beerABVIBU.setText("ABV " + singleBeerItem.getAbv() + ", IBU " + singleBeerItem.getIbu());
        }
        Log.i(TAG, singleBeerItem.toString());

        return customView;
    }
}
