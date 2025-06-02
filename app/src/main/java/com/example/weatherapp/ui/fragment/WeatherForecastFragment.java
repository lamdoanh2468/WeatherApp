package com.example.weatherapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R;
import com.example.weatherapp.model.CurrentWeatherResponse;
import com.example.weatherapp.model.ForecastResponse;
import com.example.weatherapp.ui.adapter.ForecastAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherForecastFragment extends Fragment {
    private TextView locationText;
    private TextView dateText;
    private ImageView weatherIcon;
    private TextView temperatureText;
    private TextView descriptionText;
    private RecyclerView forecastRecView;
    private ForecastAdapter forecastAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Ánh xạ các view
        locationText = view.findViewById(R.id.locationText);
        dateText = view.findViewById(R.id.dateText);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        temperatureText = view.findViewById(R.id.temperatureText);
        descriptionText = view.findViewById(R.id.descriptionText);
        forecastRecView = view.findViewById(R.id.forecastRecView);

        // Cấu hình RecyclerView
        forecastAdapter = new ForecastAdapter(new ArrayList<>());
        forecastRecView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        forecastRecView.setAdapter(forecastAdapter);

        // Cập nhật ngày hiện tại
        updateCurrentDate();
    }

    private void updateCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d/M", new Locale("vi"));
        Date currentDate = new Date();
        dateText.setText(dateFormat.format(currentDate));
    }

    public void updateWeatherData(CurrentWeatherResponse weatherData) {
        if (weatherData != null) {
            locationText.setText(weatherData.getName());
            temperatureText.setText(String.format(Locale.getDefault(), "%.0f°C", weatherData.getMain().getTemp()));
            descriptionText.setText(weatherData.getWeather().get(0).getDescription());

            // Tải icon thời tiết
            String iconCode = weatherData.getWeather().get(0).getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
            Glide.with(this)
                    .load(iconUrl)
                    .into(weatherIcon);
        }
    }

    public void updateForecastData(ForecastResponse forecastData) {
        if (forecastData != null) {
            forecastAdapter.updateData(forecastData.getList());
        }
    }
} 