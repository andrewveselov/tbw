package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 4
// Homework 30-Dec-2018
// Andrew Veselov
//
// 1. В погодном приложении сделать сохранение и загрузку настроек (например, выбранный домашний город).
//
// 2. * Сделать текстовый мини-браузер с применением WebView, OkHttp и полем ввода страницы.
//

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.utils.Constants;
import com.veselov.andrew.tbw.utils.OpenWeatherMapGetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class TempAndHum extends Fragment {
    private TextView textTemp, textHum;
    private SensorManager sensorManager;
    private Sensor sensorTemp, sensorHum;

    private final Handler handler = new Handler();

    private Typeface weatherFont;
    private TextView cityTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIconTextView;

    public TempAndHum() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_temp_and_hum, container, false);

        init(root);
        checkSavedCity();
        setHasOptionsMenu(true);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.weather_city_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showInputDialog();
        return true;
    }
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.select_city);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.change_city, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();
                updateWeatherData(city);
                SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.WEATHER_CITY, city);
                editor.apply();
            }
        });
        builder.show();
    }
    // Save energy when minimized application
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listenerTemp, sensorTemp);
        sensorManager.unregisterListener(listenerHum, sensorHum);
    }

    // Init
    private void init(View view){
        textHum = view.findViewById(R.id.sensor_hum);
        textTemp = view.findViewById(R.id.sensor_temp);
        cityTextView = view.findViewById(R.id.weather_city);
        currentTemperatureTextView = view.findViewById(R.id.weather_current_temp);
        weatherIconTextView = view.findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        weatherIconTextView.setTypeface(weatherFont);
    }

    private void checkSavedCity() {
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_WORLD_WRITEABLE);
        String city = preferences.getString(Constants.WEATHER_CITY, getString(R.string.default_city));
        updateWeatherData(city);
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = OpenWeatherMapGetter.getJSONData(getActivity().getApplicationContext(), city);
                if(jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.city_not_found, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            setPlaceName(jsonObject);
            setCurrentTemp(main);
            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";

        if(actualId == 800) {
            long currentTime = new Date().getTime();
            if(currentTime >= sunrise && currentTime < sunset) {
                icon = getString(R.string.weather_sunny);
            } else {
                icon = getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2: {
                    icon = getString(R.string.weather_thunder);
                    break;
                }
                case 3: {
                    icon = getString(R.string.weather_drizzle);
                    break;
                }
                case 5: {
                    icon = getString(R.string.weather_rainy);
                    break;
                }
                case 6: {
                    icon = getString(R.string.weather_snowy);
                    break;
                }
                case 7: {
                    icon = getString(R.string.weather_foggy);
                    break;
                }
                case 8: {
                    icon = getString(R.string.weather_cloudy);
                    break;
                }
            }
        }
        weatherIconTextView.setText(icon);
    }


    private void setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format("%.2f", main.getDouble("temp")) + "\u2103";
        currentTemperatureTextView.setText(currentTextText);
    }


    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        cityTextView.setText(cityText);
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
