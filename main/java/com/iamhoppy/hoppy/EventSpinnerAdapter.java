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

class EventSpinnerAdapter extends ArrayAdapter<Event> {
    private static final String TAG = "com.iamhoppy.hoppy";

    EventSpinnerAdapter(Context context, List events) {
        super(context, R.layout.custom_event_row, events);
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
        TextView eventName = (TextView)customView.findViewById(R.id.eventName);
        TextView eventDetails = (TextView)customView.findViewById(R.id.eventDetails);

        eventLogo.setImageResource(R.drawable.eventlogo);
        eventName.setText(singleEventItem.getName());
        Log.i(TAG, "inGetView"+singleEventItem.toString());

        return customView;
    }
}
