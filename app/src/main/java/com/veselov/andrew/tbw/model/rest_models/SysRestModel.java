package com.veselov.andrew.tbw.model.rest_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SysRestModel {
    @SerializedName("type") @Expose public int type;
    @SerializedName("id") @Expose public int id;
    @SerializedName("message") @Expose public float message;
    @SerializedName("country") @Expose public String country;
    @SerializedName("sunrise") @Expose public long sunrise;
    @SerializedName("sunset") @Expose public long sunset;
}
