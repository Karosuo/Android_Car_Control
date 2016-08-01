package com.example.karosuo.gyrocontrol;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by karosuo on 31/07/16.
 */
public class SensorsFragment extends Fragment implements SensorEventListener{

        private SensorManager mSensorManager;
        private Sensor mAccelerometer;
        private Sensor mMagnetometer;
        private final float[] mAccelerometerReading = new float[3];
        private final float[] mMagnetometerReading = new float[3];

        private final float[] mRotationMatrix = new float[9];
        private final float[] mOrientationAngles = new float[3];

    public SensorsFragment() {
    }

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
            setRetainInstance(true);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

            TextView testText = (TextView) this.getActivity().findViewById(R.id.test_text);
            testText.setText(String.format("x: %.3f, y: %.3f, z: %.3f",this.mOrientationAngles[0],this.mOrientationAngles[1],this.mOrientationAngles[2]));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return null;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
            // You must implement this callback in your code.
        }

        @Override
        public void onResume() {
            super.onResume();

            // Get updates from the accelerometer and magnetometer at a constant rate.
            // To make batch operations more efficient and reduce power consumption,
            // provide support for delaying updates to the application.
            //
            // In this example, the sensor reporting delay is small enough such that
            // the application receives an update before the system checks the sensor
            // readings again.
            mSensorManager.registerListener(this, this.mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this, this.mMagnetometer,SensorManager.SENSOR_DELAY_NORMAL);
            /*
            mSensorManager.registerListener(this, this.mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, this.mMagnetometer,SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            */
        }

        @Override
        public void onPause() {
            super.onPause();

            // Don't receive any more updates from either sensor.
            mSensorManager.unregisterListener(this);
        }

        // Get readings from accelerometer and magnetometer. To simplify calculations,
        // consider storing these readings as unit vectors.
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mAccelerometerReading,
                        0, mAccelerometerReading.length);
            }

            else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mMagnetometerReading,
                        0, mMagnetometerReading.length);
            }
            this.updateOrientationAngles();

            String str_sensors = String.format("x: %.3f, y: %.3f, z: %.3f",this.mOrientationAngles[0],this.mOrientationAngles[1],this.mOrientationAngles[2]);
            Toast toast = Toast.makeText(this.getActivity(), str_sensors, Toast.LENGTH_SHORT);
            toast.show();
        }

        // Compute the three orientation angles based on the most recent readings from
        // the device's accelerometer and magnetometer.
        public void updateOrientationAngles() {
            // Update rotation matrix, which is needed to update orientation angles.
            mSensorManager.getRotationMatrix(mRotationMatrix, null,
                    mAccelerometerReading, mMagnetometerReading);

            // "mRotationMatrix" now has up-to-date information.

            mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

            // "mOrientationAngles" now has up-to-date information.
        }
}


