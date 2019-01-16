package com.veselov.andrew.tbw.ui.activities.fragments;
// Android Level 2 Lesson 6
// Homework 12-Jan-2019
// Andrew Veselov
//
// 1. Переделать запросы и обработку ответа в погодном приложении на Retrofit 2.
//
// 2. * Оживить погодное приложение картинками из Интернета.
//

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.veselov.andrew.tbw.R;
import com.veselov.andrew.tbw.model.OpenWeatherMapRepo;
import com.veselov.andrew.tbw.model.rest_models.WeatherRequestRestModel;
import com.veselov.andrew.tbw.utils.Constants;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TempAndHum extends Fragment {
    private TextView textTemp, textHum;
    private SensorManager sensorManager;
    private Sensor sensorTemp, sensorHum;

    private TextView cityTextView;
    private TextView currentTemperatureTextView;
    private TextView weatherIconTextView;
    private ImageView weatherServiceLogo;
    private LocationManager locationManager = null;
    private Location location;
    private LocationListener locationListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_temp_and_hum, container, false);

        init(root);
        showWeatherServiceLogo(Constants.OPEN_WEATHER_LOGO_URL);
        checkSavedCity();
        setHasOptionsMenu(true);
        // Define sensor manager
        sensorManager = (SensorManager) Objects.requireNonNull(getActivity()).getSystemService(getContext().SENSOR_SERVICE);

        // Check temperature and humidity sensors
        assert sensorManager != null;
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showInputDialog();
        return true;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.select_city);

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.change_city, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();
                loadWeatherData(city);
            }
        });

        builder.setNeutralButton(R.string.define_city, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                location = getLastKnownLocation();
                if (location != null) {
//                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()), Toast.LENGTH_LONG).show();
                    loadWeatherData(location.getLatitude(), location.getLongitude());
                } else
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.city_not_determined, Toast.LENGTH_LONG).show();
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

    // Getting location
    private Location getLastKnownLocation() {
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.no_permission_for_location, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        assert locationManager != null;
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30000,10, (LocationListener) this);
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    // Init
    private void init(View view) {
        textHum = view.findViewById(R.id.sensor_hum);
        textTemp = view.findViewById(R.id.sensor_temp);
        cityTextView = view.findViewById(R.id.weather_city);
        currentTemperatureTextView = view.findViewById(R.id.weather_current_temp);
        weatherIconTextView = view.findViewById(R.id.weather_icon);
        weatherServiceLogo = view.findViewById(R.id.weather_service_logo);
        Typeface weatherFont = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/weather.ttf");
        weatherIconTextView.setTypeface(weatherFont);
    }

    private void checkSavedCity() {
        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String city = preferences.getString(Constants.WEATHER_CITY, getString(R.string.default_city));
        loadWeatherData(city);
    }

    private void saveCity(String city) {
        SharedPreferences preferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.WEATHER_CITY, city);
        editor.apply();
    }

    private void loadWeatherData(String city){
        OpenWeatherMapRepo.getInstance().getAPI().loadWeatherCity(city, Constants.OPEN_WEATHER_API_KEY, Constants.OPEN_WEATHER_UNITS_METRIC)
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                            if(response.code() == Constants.CITY_NOT_FOUND)
                                errorMessage(getString(R.string.city_not_found));
                            else if (response.isSuccessful() && response.body() != null) renderWeather(response);
                                 else errorMessage(getString(R.string.error_get_data));
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, Throwable t) {
                        errorMessage(getString(R.string.error_get_data));
                    }
                });
    }

    private void loadWeatherData(double lat, double lon){
        OpenWeatherMapRepo.getInstance().getAPI().loadWeatherLocation(lat, lon, Constants.OPEN_WEATHER_API_KEY,Constants.OPEN_WEATHER_UNITS_METRIC)
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.code() == Constants.CITY_NOT_FOUND)
                            errorMessage(getString(R.string.city_not_found));
                        else if (response.isSuccessful() && response.body() != null)
                            renderWeather(response);
                        else errorMessage(getString(R.string.error_get_data));
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherRequestRestModel> call, Throwable t) {
                        errorMessage(getString(R.string.error_get_data));
                    }
                });
    }

    private void renderWeather(Response<WeatherRequestRestModel> response) {
        setPlaceName(response);
        setCurrentTemp(response);
        setWeatherIcon(response);
    }

    private void setWeatherIcon(Response<WeatherRequestRestModel> response) {
        assert response.body() != null;
        int actualId = response.body().weatherRestModel[0].id;
        long sunrise = response.body().sysRestModel.sunrise * 1000;
        long sunset = response.body().sysRestModel.sunset * 1000;
        int id = actualId / 100;
        String icon = "";

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
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

    private void setCurrentTemp(Response<WeatherRequestRestModel> response) {
        String currentTextText = String.format("%.2f", response.body().mainRestModel.temp) + "\u2103";
        currentTemperatureTextView.setText(currentTextText);
    }

    private void setPlaceName(Response<WeatherRequestRestModel> response) {
        assert response.body() != null;
        String cityText = response.body().name.toUpperCase() + ", "
                + response.body().sysRestModel.country;
        cityTextView.setText(cityText);
        saveCity(cityText);
    }

    private void showWeatherServiceLogo(String url) {
        Picasso.get()
                .load(url)
                .into(weatherServiceLogo);
    }

    private void errorMessage(String message){
        // clear fields
        cityTextView.setText("");
        currentTemperatureTextView.setText("");
        weatherIconTextView.setText("");
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
