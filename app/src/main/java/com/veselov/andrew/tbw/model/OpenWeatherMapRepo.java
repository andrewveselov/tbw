package com.veselov.andrew.tbw.model;

import com.veselov.andrew.tbw.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherMapRepo {
    private OpenWeatherMapAPI API;

    private static OpenWeatherMapRepo instance = null;

    private OpenWeatherMapRepo() {
        API = createAdapter();
    }

    public static OpenWeatherMapRepo getInstance() {
        if(instance == null) {
            instance = new OpenWeatherMapRepo();
        }
        return instance;
    }

    public OpenWeatherMapAPI getAPI() {
        return API;
    }

    private OpenWeatherMapAPI createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                //Базовая часть адреса
                .baseUrl(Constants.OPEN_WEATHER_MAP_API_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON в объекты
                .build();
        //Создаем объект, при помощи которого будем выполнять запросы
        return adapter.create(OpenWeatherMapAPI.class);
    }
}

