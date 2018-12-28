package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 2
// Homework 22-Dec-2018
// Andrew Veselov
//
// 1. Добавить датчики температуры и влажности (TYPE_AMBIENT_TEMPERATURE, TYPE_ABSOLUTE_HUMIDITY).
//    Показывать текущую температуру, влажность в погодном приложении (если такой датчик присутствует
//    в аппарате).
//
// 2. * Создать свой элемент View и использовать его в погодном приложении.
//

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veselov.andrew.tbw.R;


public class TempAndHum extends Fragment {
    private TextView textTemp, textHum;
    private SensorManager sensorManager;
    private Sensor sensorTemp, sensorHum;

    public TempAndHum() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_temp_and_hum, container, false);
        textHum = root.findViewById(R.id.sensor_hum);
        textTemp = root.findViewById(R.id.sensor_temp);
        // Define sensor manager
        sensorManager = (SensorManager) getActivity().getSystemService(getContext().SENSOR_SERVICE);

        // Check temperature and humidity sensors
        sensorHum = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        // sensorHum = null;

        if (sensorHum != null) sensorManager.registerListener(listenerHum, sensorHum,
                SensorManager.SENSOR_DELAY_NORMAL);
        else textHum.setText(getString(R.string.sensor_hum_not_connected));

        if (sensorTemp != null) sensorManager.registerListener(listenerTemp, sensorTemp,
                SensorManager.SENSOR_DELAY_NORMAL);
        else textTemp.setText(getString(R.string.sensor_temp_not_connected));

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Save energy when minimized application
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemp, sensorTemp);
        sensorManager.unregisterListener(listenerHum, sensorHum);
    }

    // Show temperature and humidity sensor values
    private void showTempSensors(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.sensor_temp_value)).append(event.values[0])
                .append(getString(R.string.sensor_temp_unit));
        textTemp.setText(stringBuilder);
    }

    private void showHumSensors(SensorEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.sensor_hum_value)).append(event.values[0])
                .append(getString(R.string.sensor_hum_unit));
        textHum.setText(stringBuilder);
    }


    // Temperature sensor listener
    SensorEventListener listenerTemp = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showTempSensors(event);
        }
    };

    // Humidity sensor listener
    SensorEventListener listenerHum = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            showHumSensors(event);
        }
    };

}
