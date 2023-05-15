package com.example.obstaclecoursegame.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.obstaclecoursegame.Interfaces.RotateCallback;

public class RotationDetector {
    private final Sensor sensor;
    private final SensorManager sensorManager;
    private final RotateCallback rotateCallback;
    private long timestamp = 0;

    private SensorEventListener sensorEventListener;

    public RotationDetector(Context context, RotateCallback rotateCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.rotateCallback = rotateCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                calculateStep(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    private void calculateStep(float x) {
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis();
            if (x > 6.0) {
                rotateCallback.rotateLeft();
            }
            else if (x < -6.0) {
                rotateCallback.rotateRight();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
