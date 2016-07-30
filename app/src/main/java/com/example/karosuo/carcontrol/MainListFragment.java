package com.example.karosuo.carcontrol;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by karosuo on 28/07/16.
 */
public class MainListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        MainActivity myBaseActivity = ((MainActivity) this.getActivity()); //Get the container activity
        MyDBAccess myDB = new MyDBAccess(myBaseActivity); //Instantiate the DB Handler

        /** Test Trip Objects, to put them on to DB */
        //Trip myFirstTrip = new Trip("First Trip", null, "Trayectoria", 25); //Test Trip object
        //Trip mySecondTrip = new Trip("Second Trip", null, "Trayectoria", 25); //Test Trip object
        //myDB.addTrip(myFirstTrip);

        /** Test the DB filter reads */
        //String today = new SimpleDateFormat("yyyy-MM-DD HH:mm").format(new Date()); //Ref date, today
        //myTripList = myDB.getTripByDate(today); //specific date
        //myTripList = myDB.getTripByDate("0000-00-00 00:00",today); //range
        //myTripList = myDB.getTripByLength(30); //specific length

        ArrayList<Trip> myTripList = new ArrayList<>();
        myTripList =  myDB.getAllTrips();

        /** Adjust the adapter */
        TripListAdapter ListAdapter = new TripListAdapter(myBaseActivity, R.layout.main_list_element, myTripList);
        setListAdapter(ListAdapter);
    }
}
