package com.example.karosuo.gyrocontrol;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Menu myMenuBar;
    private MainListFragment mListFrag;
    private EditText userInputDialogEditText; //Trip name for search
    private DatePicker userInputDialogDate; //Trip(s)'s date for search
    private int lenth_for_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Setup Toolbar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** Actions for the FAB*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(view.getContext(), AddTripActivity.class);
                startActivity(intent);
            }
        });

        /** Find Main List fragment to be able to update listView*/
        FragmentManager fm = getFragmentManager();
        mListFrag = (MainListFragment) fm.findFragmentById(R.id.main_list_frag);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        //inflater.inflate(R.menu.menu_main, menu);
        this.myMenuBar = menu;
        menu.findItem(R.id.wifi_status).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.wifi_disab_icon, null));

        /** Setup the connection settings, IP and PORT*/
        setIP_PORT("127.0.0.1:12345");

        /** Check if GyroCar is already setup*/
        checkGyroCarConnection();
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.wifi_status) {
            checkGyroCarConnection();
        }else if (id == R.id.search_by_name){
            setupInputDialog(this);
        }else if(id == R.id.search_by_date){
            setupDateDialog(this);
        }else if(id == R.id.order_by_length){
            orderBySize();
        }else if(id == R.id.show_all_mainItem){
            mListFrag.showAllTrips();
        }else if(id == R.id.wifi_config_actionbar){
            setupWifiDialog(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupWifiDialog(Context c){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_wifi_config_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        userInputDialogEditText = (EditText) mView.findViewById(R.id.user_wifi_config_edit);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.done_button_tag, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        setIP_PORT(userInputDialogEditText.getText().toString());
                    }

                })

                .setNegativeButton(R.string.cancel_button_tag,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void setIP_PORT(String ip_port){
        String ip_port_pattern = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):\\d{3,5}";
        Pattern pattern_object = Pattern.compile(ip_port_pattern);
        Matcher result = pattern_object.matcher(ip_port);

        if (result.find()){
            String[] ip_port_array = ip_port.split(":");
            UDPConnection.setup(ip_port_array[0], Integer.valueOf(ip_port_array[1]));
            Toast toast = Toast.makeText(MainActivity.this, String.format("Connection Configured %s", ip_port), Toast.LENGTH_LONG);
            toast.show();
        }else{
            Toast toast = Toast.makeText(MainActivity.this, "Not a correct IP:PORT", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void checkGyroCarConnection(){
        String messageToGyro = String.format("Are you ready?");
        String ACK_fromGyro = String.format("GyroCar Ready!");

        String ipAddress = String.format("192.168.0.3");
        int port = 12345;

        //UDPConnection.setup(ipAddress, port);
        UDPConnection.askACK(messageToGyro, ACK_fromGyro);
        boolean GyroResponse = UDPConnection.getACKStatus();

        if (GyroResponse){
            myMenuBar.findItem(R.id.wifi_status).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.wifi_enab_icon, null));
            Toast toast = Toast.makeText(this, ACK_fromGyro, Toast.LENGTH_LONG);
            toast.show();
        }else{
            if (UDPConnection.getUDPlog().equals("Blocked")){
                Toast toast = Toast.makeText(this, "Connection in use by another device", Toast.LENGTH_LONG);
                toast.show();
            }else{
                Toast toast = Toast.makeText(this, "GyroCar isn't ready", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void setupDateDialog(Context activityContext){
        /** Get current date */
        final Calendar myCal = Calendar.getInstance();
        int mYear = myCal.get(Calendar.YEAR);
        int mMont = myCal.get(Calendar.MONTH);
        int mDay = myCal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        /** Te user changed the date */
                        String dateToSearch = String.format("%04d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
                        ArrayList<Trip> myList = mListFrag.getMyDB().getTripByDate(dateToSearch);

                        if(!myList.isEmpty()){
                            mListFrag.updateTripList(myList);
                        }
                    }
                }, mYear, mMont, mDay);
        datePickerDialog.show();
    }

    private void orderBySize(){
        ArrayList<Trip> myList = mListFrag.getMyDB().orderBySizeDESC();

        if(!myList.isEmpty()){
            mListFrag.updateTripList(myList);
        }
    }

    private void setupInputDialog(Context c){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_text_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        userInputDialogEditText = (EditText) mView.findViewById(R.id.user_input_text_edit);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.search_button_tag, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String nameToSearch = userInputDialogEditText.getText().toString();
                        ArrayList<Trip> myList = mListFrag.getMyDB().getTripByName(nameToSearch);

                        if(!myList.isEmpty()){
                            mListFrag.updateTripList(myList);
                        }
                    }

                })

                .setNegativeButton(R.string.cancel_button_tag,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public void onResume(){
        super.onResume();
        mListFrag.showAllTrips();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        mListFrag.showAllTrips();
    }
}
