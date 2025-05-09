package com.example.weatherapp.model;
import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private double temp; // Nhiệt độ hiện tại
    @SerializedName("feels_like")
    private double feelsLike;
    @SerializedName("temp_min")
    private double tempMin;
    @SerializedName("temp_max")
    private double tempMax;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("humidity")
    private int humidity; // Độ ẩm
    @SerializedName("sea_level")
    private Integer seaLevel; // Optional
    @SerializedName("grnd_level")
    private Integer grndLevel; // Optional

    public double getTemp() { return temp; }
    public double getFeelsLike() { return feelsLike; }
    public double getTempMin() { return tempMin; }
    public double getTempMax() { return tempMax; }
    public int getPressure() { return pressure; }
    public int getHumidity() { return humidity; }
    public Integer getSeaLevel() { return seaLevel; }
    public Integer getGrndLevel() { return grndLevel; }
}
