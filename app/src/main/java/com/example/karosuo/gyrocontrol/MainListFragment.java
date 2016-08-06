package com.example.karosuo.gyrocontrol;

/**
 * Created by karosuo on 30/07/16.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
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
    private String name_to_eliminate;

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
        //myTripList = myDB.getTripByDate("2016-08-05 23:59:00");

        //Toast toast = Toast.makeText(this.getActivity(), myTripList.get(0).getFecha() , Toast.LENGTH_LONG);
        //toast.show();

        /** Adjust the adapter */
        mainListView = (ListView) this.getActivity().findViewById(R.id.main_list_listview);
        ListAdapter = new TripListAdapter(myBaseActivity, R.layout.main_list_element, myTripList);
        mainListView.setAdapter(ListAdapter);


        /** Config the long click for the list view*/
        this.mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                name_to_eliminate = myTripList.get(position).getName();

                /** Config the confirm deletition*/
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        switch (choice) {
                            case DialogInterface.BUTTON_POSITIVE:

                                getMyDB().deleteTrip(myTripList.get(position));
                                showAllTrips();
                                Toast toast = Toast.makeText(MainListFragment.this.getActivity(), String.format("%s eliminado", name_to_eliminate), Toast.LENGTH_LONG);
                                toast.show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String message = String.format("%s\n%s","Sure to delete?", name_to_eliminate);
                builder.setMessage(message)
                        .setPositiveButton(R.string.yes_dialog_tag, dialogClickListener)
                        .setNegativeButton(R.string.no_dialog_tag, dialogClickListener).show();

                return false;
            }
        });
    }

    public void updateTripList(ArrayList<Trip> newList){
        if(!newList.isEmpty()){
            this.myTripList.clear();
            for (Trip temp: newList){
                this.myTripList.add(temp);
            }
            this.ListAdapter.notifyDataSetChanged();
        }
    }

    public void showAllTrips(){
        ArrayList<Trip> myList = this.getMyDB().getAllTrips();
        this.updateTripList(myList);
    }

    public MyDBAccess getMyDB() {
        return myDB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.main_list_layout, container, false);
        return myView;
    }

}
