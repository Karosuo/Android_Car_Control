package com.example.karosuo.gyrocontrol;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ControllerMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_map);

        FragmentManager fm = getFragmentManager();

        // Check to see if we have retained the worker fragment.
        SensorsFragment mSensFrag = (SensorsFragment)fm.findFragmentByTag("SensorsFrag");

        // If not retained (or first time running), we need to create it.
        if (mSensFrag == null) {
            mSensFrag = new SensorsFragment();   //create instance of NON UI Fragment
            // Tell it who it is working with.
            //mSensFrag.setTargetFragment(this, 0);
            fm.beginTransaction().add(mSensFrag, "SensorsFrag").commit();  //NON UI Fragment
        }

    }
}
