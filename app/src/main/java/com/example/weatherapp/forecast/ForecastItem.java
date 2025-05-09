package com.example.weatherapp.forecast;
import com.example.weatherapp.weatherDataModel.Clouds;
import com.example.weatherapp.weatherDataModel.Main;
import com.example.weatherapp.weatherDataModel.Weather;
import com.example.weatherapp.weatherDataModel.Wind;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class ForecastItem {
    @SerializedName("dt")
    private long dt; // Thời gian dự báo (Unix timestamp)
    @SerializedName("main")
    private Main main; // Sử dụng lại Main data class từ CurrentWeatherResponse
    @SerializedName("weather")
    private List<Weather> weather; // Sử dụng lại Weather data class
    @SerializedName("clouds")
    private Clouds clouds; // Sử dụng lại Clouds data class
    @SerializedName("wind")
    private Wind wind; // Sử dụng lại Wind data class
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("pop")
    private double pop; // Probability of precipitation
    @SerializedName("sys")
    private ForecastSys sys;
    @SerializedName("dt_txt")
    private String dtTxt; // Thời gian dự báo dưới dạng chuỗi

    public long getDt() { return dt; }
    public Main getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Clouds getClouds() { return clouds; }
    public Wind getWind() { return wind; }
    public int getVisibility() { return visibility; }
    public double getPop() { return pop; }
    public ForecastSys getSys() { return sys; }
    public String getDtTxt() { return dtTxt; }
}
