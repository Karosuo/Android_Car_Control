package com.example.karosuo.gyrocontrol;

/**
 * Created by karosuo on 30/07/16.
 */
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by karosuo on 28/07/16.
 */
public class MainListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //MainActivity myBaseActivity = ((MainActivity) this.getActivity()); //Get the container activity
        Activity myBaseActivity = this.getActivity();
        MyDBAccess myDB = new MyDBAccess(myBaseActivity); //Instantiate the DB Handler

        /** Test Trip Objects, to put them on to DB */
        //Trip myThirdTrip = new Trip("Third Trip", null, "Trayectoria", 36); //Test Trip object
        //Trip mySecondTrip = new Trip("Second Trip", null, "Trayectoria", 25); //Test Trip object
        //myDB.addTrip(mySecondTrip);
        //myDB.addTrip(myThirdTrip);

        ArrayList<Trip> myTripList;// = new ArrayList<>();

        /** Test the DB filter reads */
        String today = new SimpleDateFormat("yyyy-MM-DD HH:mm").format(new Date()); //Ref date, today
        //myTripList = myDB.getTripByDate(today); //specific date
        //myTripList = myDB.getTripByDate("0000-00-00 00:00",today); //range
        //myTripList = myDB.getTripByLength(30); //specific length
        myTripList =  myDB.getAllTrips();

        /** Adjust the adapter */
        TripListAdapter ListAdapter = new TripListAdapter(myBaseActivity, R.layout.main_list_element, myTripList);
        setListAdapter(ListAdapter);
    }
}
