package com.example.weatherapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.utils.ClothingSuggestion;
import com.example.weatherapp.R;
import com.example.weatherapp.model.CurrentWeatherResponse;

import java.util.Locale;

public class ClothingSuggestionFragment extends Fragment {
    private TextView currentTempText;
    private TextView currentWeatherText;
    private TextView humidityText;
    private TextView windSpeedText;
    private TextView clothingSuggestionText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clothing_suggestion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Ánh xạ các view
        currentTempText = view.findViewById(R.id.currentTempText);
        currentWeatherText = view.findViewById(R.id.currentWeatherText);
        humidityText = view.findViewById(R.id.humidityText);
        windSpeedText = view.findViewById(R.id.windSpeedText);
        clothingSuggestionText = view.findViewById(R.id.clothingSuggestionText);
    }

    public void updateWeatherData(CurrentWeatherResponse weatherData) {
        if (weatherData == null) {
            Log.e("ClothingSuggestion", "weatherData is null");
            return;
        }

        if (currentTempText == null) {
            Log.e("ClothingSuggestion", "currentTempText is null");
            return;
        }

        if (weatherData.getMain() == null) {
            Log.e("ClothingSuggestion", "weatherData.getMain() is null");
            return;
        }

        try {
            // Cập nhật thông tin thời tiết hiện tại
            double temp = weatherData.getMain().getTemp();
            Log.d("ClothingSuggestion", "Temperature: " + temp);
            currentTempText.setText(String.format(Locale.getDefault(), "%.0f°C", temp));
            
            if (weatherData.getWeather() != null && !weatherData.getWeather().isEmpty()) {
                currentWeatherText.setText(weatherData.getWeather().get(0).getDescription());
            }
            
            humidityText.setText(String.format(Locale.getDefault(), "Độ ẩm: %d%%", weatherData.getMain().getHumidity()));
            windSpeedText.setText(String.format(Locale.getDefault(), "Gió: %.1f m/s", weatherData.getWind().getSpeed()));

            // Cập nhật đề xuất trang phục
            String weatherCondition = weatherData.getWeather().get(0).getMain().toLowerCase();
            double temperature = weatherData.getMain().getTemp();
            double humidity = weatherData.getMain().getHumidity();
            double windSpeed = weatherData.getWind().getSpeed();

            String suggestion = ClothingSuggestion.getClothingSuggestion(
                temperature,
                weatherCondition,
                humidity,
                windSpeed
            );
            clothingSuggestionText.setText(suggestion);
        } catch (Exception e) {
            Log.e("ClothingSuggestion", "Error updating weather data: " + e.getMessage(), e);
        }
    }
} 