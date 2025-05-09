package com.example.weatherapp.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    TextView dateText;
    ImageView iconImage;
    TextView tempText;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        dateText = itemView.findViewById(R.id.forecastDateText); // Cần tạo ID này trong layout item
        iconImage = itemView.findViewById(R.id.forecastIcon); // Cần tạo ID này trong layout item
        tempText = itemView.findViewById(R.id.forecastTempText); // Cần tạo ID này trong layout item
    }

}
