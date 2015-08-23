package com.iamhoppy.hoppy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class EventSpinnerAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "com.iamhoppy.hoppy";
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
        Event singleEventItem = getItem(position);

        ImageView eventLogo = (ImageView)customView.findViewById(R.id.eventLogo);
        Picasso.with(context)
                .load(singleEventItem.getLogoURL())
                .fit()
                .centerInside()
                .into(eventLogo);

        TextView eventName = (TextView)customView.findViewById(R.id.eventName);
        TextView eventDetails = (TextView)customView.findViewById(R.id.eventDetails);

        eventName.setText(singleEventItem.getName());
        eventDetails.setText(singleEventItem.getDate());
        Log.i(TAG, "inGetView" + singleEventItem.toString());
        System.out.println("inGetView"+singleEventItem.getName());

        return customView;
    }
}
