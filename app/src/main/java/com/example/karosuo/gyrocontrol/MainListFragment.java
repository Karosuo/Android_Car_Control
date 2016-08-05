package com.example.karosuo.gyrocontrol;

/**
 * Created by karosuo on 30/07/16.
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by karosuo on 28/07/16.
 */
public class MainListFragment extends Fragment {// ListFragment {

    private ArrayList<Trip> myTripList;
    private ListView mainListView;
    private TripListAdapter ListAdapter;
    private MyDBAccess myDB;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //MainActivity myBaseActivity = ((MainActivity) this.getActivity()); //Get the container activity
        Activity myBaseActivity = this.getActivity();
        myDB = new MyDBAccess(myBaseActivity); //Instantiate the DB Handler

        /** Test Trip Objects, to put them on to DB */
        //Trip myThirdTrip = new Trip("Hour and something", null, "Trayectoria", 3800); //Test Trip object
        //Trip mySecondTrip = new Trip("Just seconds", null, "Trayectoria", 25); //Test Trip object
        //myDB.addTrip(mySecondTrip);
        //myDB.addTrip(myThirdTrip);



        /** Test the DB filter reads */
        //String today = new SimpleDateFormat("yyyy-MM-DD HH:mm").format(new Date()); //Ref date, today
        //myTripList = myDB.getTripByDate(today); //specific date
        //myTripList = myDB.getTripByDate("0000-00-00 00:00",today); //range
        //myTripList = myDB.getTripByLength(30); //specific length
        //myTripList = myDB.getTripByName("First Trip");
        myTripList =  myDB.getAllTrips();

        /** Adjust the adapter */
        mainListView = (ListView) this.getActivity().findViewById(R.id.main_list_listview);
        ListAdapter = new TripListAdapter(myBaseActivity, R.layout.main_list_element, myTripList);
        mainListView.setAdapter(ListAdapter);
    }

    public void updateTripList(ArrayList<Trip> newList){
        this.myTripList.clear();
        for (Trip temp: newList){
            /*if(!this.myTripList.contains(temp)){
                this.myTripList.add(temp);
            }*/
            this.myTripList.add(temp);
        }
        this.ListAdapter.notifyDataSetChanged();
    }

    public TripListAdapter getListAdapter() {
        return ListAdapter;
    }

    public MyDBAccess getMyDB() {
        return myDB;
    }

    public ListView getMainListView() {
        return mainListView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.main_list_layout, container, false);
        return myView;
    }

}
