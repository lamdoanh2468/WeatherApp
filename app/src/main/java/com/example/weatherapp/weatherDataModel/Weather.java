package com.example.weatherapp.weatherDataModel;
import com.google.gson.annotations.SerializedName;
public class Weather {
    @SerializedName("id")
    private int id;
    @SerializedName("main")
    private String main; // Ví dụ: "Clouds", "Clear"
    @SerializedName("description")
    private String description; // Ví dụ: "overcast clouds"
    @SerializedName("icon")
    private String icon; // Mã icon thời tiết

    public int getId() { return id; }
    public String getMain() { return main; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}
