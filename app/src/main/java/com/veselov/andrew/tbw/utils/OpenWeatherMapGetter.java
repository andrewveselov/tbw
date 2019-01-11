package com.veselov.andrew.tbw.utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class OpenWeatherMapGetter {
    private static final String OPEN_WEATHER_API_KEY = "de7fa657bb311ee51c70ead50aaf49ac";
    private static final String OPEN_WEATHER_API_URL_CITY = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_API_URL_LOC = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final int ALL_GOOD = 200;

    public static JSONObject getJSONData(Context context, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API_URL_CITY, city));
            return readJSONData(url);
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONData(Context context, double lat, double lon) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API_URL_LOC, String.valueOf(lat),String.valueOf(lon)));
            return readJSONData(url);
    } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private static JSONObject readJSONData (URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, OPEN_WEATHER_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;

            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append("\n");
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if(jsonObject.getInt(RESPONSE) != ALL_GOOD) {
                return null;
            } else {
                return jsonObject;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
}
