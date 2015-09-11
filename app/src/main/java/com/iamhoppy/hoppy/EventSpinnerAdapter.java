package com.iamhoppy.hoppy;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

class EventSpinnerAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "com.iamhoppy.hoppy";
    private Event singleEventItem;
    TextView eventName;
    TextView eventDetails;
    TextView comingSoon;

    Context context;

    EventSpinnerAdapter(Context context, List events) {
        super(context, R.layout.custom_event_row, events);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.custom_event_row, parent, false);
        singleEventItem = getItem(position);

        ImageView eventLogo = (ImageView)customView.findViewById(R.id.eventLogo);
        Picasso.with(context)
                .load(singleEventItem.getLogoURL())
                .fit()
                .centerInside()
                .into(eventLogo);

        //if no beers for that event, then display as coming soon!
        eventName = (TextView)customView.findViewById(R.id.eventName);
        eventDetails = (TextView)customView.findViewById(R.id.eventDetails);
        comingSoon = (TextView)customView.findViewById(R.id.comingSoon);
        if(singleEventItem.getBeerCount()>0){
            comingSoon.setVisibility(View.GONE);
        }
        eventName.setText(singleEventItem.getName());
        eventDetails.setText(singleEventItem.getDate());
        return customView;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItem(position).getBeerCount()>0) ? true:false;
    }
}
