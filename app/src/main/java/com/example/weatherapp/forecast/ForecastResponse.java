package com.example.weatherapp.forecast;
import com.example.weatherapp.weatherDataModel.City;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {
    @SerializedName("cod")
    private String cod;
    @SerializedName("message")
    private int message;
    @SerializedName("cnt")
    private int cnt;
    @SerializedName("list")
    private List<ForecastItem> list; // Danh sách các mục dự báo
    @SerializedName("city")
    private City city;

    public String getCod() { return cod; }
    public int getMessage() { return message; }
    public int getCnt() { return cnt; }
    public List<ForecastItem> getList() { return list; }
    public City getCity() { return city; }
}
