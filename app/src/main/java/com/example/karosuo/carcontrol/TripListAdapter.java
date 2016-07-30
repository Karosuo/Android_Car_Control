package com.example.karosuo.carcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Modified by karosuo on 29/07/16.
 * Original on https://devtut.wordpress.com/2011/06/09/custom-arrayadapter-for-a-listview-android/
 */
public class TripListAdapter extends ArrayAdapter<Trip> {

    /** The List of all the trips */
    private ArrayList<Trip> myTripList;

    public TripListAdapter(Context context, int textViewResourceId, ArrayList<Trip> list){
        super(context, textViewResourceId, list);
        this.setMyTripList(list);
    }

    public ArrayList<Trip> getMyTripList() {
        return myTripList;
    }

    public void setMyTripList(ArrayList<Trip> myTripList) {
        this.myTripList = myTripList;
    }


    // first check to see if the view is null. if so, we have to inflate it.
    // to inflate it basically means to render, or show, the view.
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_element, null);
        }

        /*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Trip i = this.myTripList.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView trip_title = (TextView) convertView.findViewById(R.id.element_title);
            TextView trip_date = (TextView) convertView.findViewById(R.id.element_date);

            for (Trip temp : this.myTripList){
                trip_title.setText(temp.getName());
                trip_date.setText(temp.getFecha());
            }
        }

        return convertView;
    }
}
