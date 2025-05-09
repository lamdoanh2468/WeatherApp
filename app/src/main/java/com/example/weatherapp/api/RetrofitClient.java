package com.example.weatherapp.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static RetrofitClient instance;
    private OpenWeatherApiService openWeatherApiService;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherApiService = retrofit.create(OpenWeatherApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public OpenWeatherApiService getOpenWeatherApiService() {
        return openWeatherApiService;
    }
}
