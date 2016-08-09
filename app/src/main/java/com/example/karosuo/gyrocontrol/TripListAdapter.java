package com.example.karosuo.gyrocontrol;

/**
 * Created by karosuo on 30/07/16.
 */
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Trip tempTrip = this.myTripList.get(position);

        if (tempTrip != null) {
            String[] dateComponents;

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView trip_title = (TextView) convertView.findViewById(R.id.element_title);
            TextView trip_date = (TextView) convertView.findViewById(R.id.element_date);
            TextView trip_time = (TextView) convertView.findViewById(R.id.element_time);
            TextView trip_duration = (TextView) convertView.findViewById(R.id.element_duration);
            ImageView trip_Image = (ImageView) convertView.findViewById(R.id.element_image);

            trip_title.setText(tempTrip.getName());
            dateComponents = tempTrip.getFechaDH();
            trip_date.setText(dateComponents[0]); //Set the YYYY/mm/dd
            trip_time.setText(dateComponents[1]); //Set the HH:mm
            trip_duration.setText(String.valueOf(tempTrip.getDuracion()));
            if (tempTrip.getImgUri().equals("Default")){
                trip_Image.setImageBitmap(BitmapHelper.decodeSampledBitmapFromResource(trip_Image.getContext().getResources(), R.drawable.default_map, 70, 50));
            }else{
                //trip_Image.setImageURI(Uri.parse(tempTrip.getImgUri()));
                Uri.Builder UriBuild = new Uri.Builder();
                UriBuild.appendPath(tempTrip.getImgUri());
                trip_Image.setImageBitmap(BitmapHelper.decodeSampledBitmapFromUri(Uri.parse(tempTrip.getImgUri()),50,50));
            }

        }

        return convertView;
    }
}
