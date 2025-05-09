package com.example.weatherapp.model;
import com.google.gson.annotations.SerializedName;
public class Sys {
    @SerializedName("type")
    private Integer type; // Optional
    @SerializedName("id")
    private Integer id; // Optional
    @SerializedName("country")
    private String country; // Mã quốc gia
    @SerializedName("sunrise")
    private long sunrise;
    @SerializedName("sunset")
    private long sunset;

    public Integer getType() { return type; }
    public Integer getId() { return id; }
    public String getCountry() { return country; }
    public long getSunrise() { return sunrise; }
    public long getSunset() { return sunset; }
}
