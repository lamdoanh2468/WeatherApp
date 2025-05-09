package com.example.weatherapp.api;
import com.example.weatherapp.CurrentWeatherResponse;
import com.example.weatherapp.forecast.ForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface OpenWeatherApiService {
    @GET("data/2.5/weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units, // Đơn vị đo: metric (Celsius), imperial (Fahrenheit)
            @Query("lang") String lang // Ngôn ngữ: tiếng Việt
    );
    // Endpoint để lấy dự báo thời tiết 5 ngày / 3 giờ theo vĩ độ và kinh độ
    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units, // Đơn vị đo: metric (Celsius), imperial (Fahrenheit)
            @Query("lang") String lang // Ngôn ngữ: tiếng Việt
    );
}
