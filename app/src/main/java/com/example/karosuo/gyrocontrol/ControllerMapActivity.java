package com.example.karosuo.gyrocontrol;

import android.app.FragmentManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ControllerMapActivity extends AppCompatActivity implements MySensorListener{

    private DrawingPanelView control_drawing_panel;
    private boolean paused = false;
    //private float[] rollPitch; //this is for painting, not initialized

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_map);

        /** Config of the drawing panel*/
        this.control_drawing_panel = new DrawingPanelView(this);
        this.control_drawing_panel.setBackgroundColor(Color.WHITE);
        setContentView(control_drawing_panel);


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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.stop_trip:
                break;
            case R.id.pause_trip:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
