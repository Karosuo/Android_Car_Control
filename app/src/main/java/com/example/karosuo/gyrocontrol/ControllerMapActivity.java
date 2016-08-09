package com.example.karosuo.gyrocontrol;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControllerMapActivity extends AppCompatActivity implements MySensorListener{

    private DrawingPanelView control_drawing_panel;
    private boolean paused = false;
    private int current_trip_id;
    private Date start_time;
    private Menu myMenuBar;
    //private float[] rollPitch; //this is for painting, not initialized

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_map);

        /** Get intent info from AddTripActivity*/
        Intent addedTripIntent = getIntent();
        current_trip_id = addedTripIntent.getIntExtra(AddTripFragment.CURRENT_TRIP_ID,0);
        Trip tmpTrip = new Trip();
        tmpTrip.setId(current_trip_id);

        MyDBAccess myDB = new MyDBAccess(this);
        tmpTrip = myDB.getTripById(current_trip_id);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            start_time = formatter.parse(tmpTrip.getFecha());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /** Config of the drawing panel*/
        /*
        this.control_drawing_panel = new DrawingPanelView(this);
        this.control_drawing_panel.setBackgroundColor(Color.WHITE);
        setContentView(control_drawing_panel);
        */


        /** Sensor fragment config and attach to ControllerMapActivity*/
        FragmentManager fm = getFragmentManager();

        // Check to see if we have retained the worker fragment.
        SensorsFragment mSensFrag = (SensorsFragment)fm.findFragmentByTag("SensorsFrag");

        // If not retained (or first time running), we need to create it.
        if (mSensFrag == null) {
            mSensFrag = new SensorsFragment();   //create instance of NON UI Fragment
            // Tell it who it is working with.
            fm.beginTransaction().add(mSensFrag, "SensorsFrag").commit();  //NON UI Fragment
        }
        /** Add listener to get the sensors fragment data*/
        mSensFrag.registerListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        MyDBAccess myDB = new MyDBAccess(this);
        Trip tmpTrip = myDB.getTripById(current_trip_id);
        ImageView bkImage = (ImageView) findViewById(R.id.controller_background_imageView);
        bkImage.setImageBitmap(BitmapHelper.decodeSampledBitmapFromUri(Uri.parse(tmpTrip.getImgUri()),0, 0));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Date end_time = new Date();
        MyDBAccess myDB = new MyDBAccess(this);
        Trip tmpTrip = myDB.getTripById(current_trip_id);
        tmpTrip.setDuracion((int) (end_time.getTime() - start_time.getTime()) / 1000);
        myDB.updateTrip(tmpTrip);
    }

    private float[] getXYreadings(SensorsFragment mSensFrag){
        float[] sensorReadings;
        float[] xyReadings = new float[2];

        sensorReadings = mSensFrag.getReadings();
        int i=0;
        for(i=0; i<xyReadings.length; i++){
            xyReadings[i] = sensorReadings[i];
        }

        return xyReadings;
    }

    @Override
    public void getDataFromSensors(float[] sensorData) {
        if (!paused){

            String message = String.format("%.3f,%.3f",sensorData[0],sensorData[1]);
            /*
            String ipAdress = String.format("192.168.0.3");
            int port = 12345;
            UDPConnection.setup(ipAdress, port);
            */
            UDPConnection.sendString(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.stop_control, menu);
        myMenuBar = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.stop_trip:
                this.finish();
                break;
            case R.id.pause_trip:
                switchPauseState();
                switchPauseIcon();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchPauseIcon(){
        if (paused)
            myMenuBar.findItem(R.id.pause_trip).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.play_icon2, null));
        else
            myMenuBar.findItem(R.id.pause_trip).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.pause_icon2, null));
    }

    private void switchPauseState(){
        if (paused)
            paused = false;
        else
            paused = true;
    }
}
