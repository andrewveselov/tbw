package com.veselov.andrew.tbw.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//"main":{"temp":273.15,"pressure":1000,"humidity":99,"temp_min":273.15,"temp_max":273.15}
public class MainRestModel {
    @SerializedName("temp") @Expose public float temp;
    @SerializedName("pressure") @Expose public double pressure;
    @SerializedName("humidity") @Expose public int humidity;
    @SerializedName("temp_min") @Expose public float tempMin;
    @SerializedName("temp_max") @Expose public float tempMax;

}
