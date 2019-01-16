package com.veselov.andrew.tbw.model;

import com.veselov.andrew.tbw.model.rest_models.WeatherRequestRestModel;
import com.veselov.andrew.tbw.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapAPI {
    @GET(Constants.OPEN_WEATHER_MAP_API_ENDPOINT_URL)
    Call<WeatherRequestRestModel> loadWeatherLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String keyApi, @Query("units") String units);
    @GET(Constants.OPEN_WEATHER_MAP_API_ENDPOINT_URL)
    Call<WeatherRequestRestModel> loadWeatherCity(@Query("q") String cityCountry, @Query("appid") String keyApi, @Query("units") String units);
}

