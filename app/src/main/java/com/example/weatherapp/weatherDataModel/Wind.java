package com.example.weatherapp.weatherDataModel;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    private double speed; // Tốc độ gió
    @SerializedName("deg")
    private int deg;
    @SerializedName("gust")
    private Double gust; // Optional

    public double getSpeed() { return speed; }
    public int getDeg() { return deg; }
    public Double getGust() { return gust; }
}
