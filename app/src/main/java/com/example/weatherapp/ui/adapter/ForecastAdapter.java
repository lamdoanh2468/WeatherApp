package com.example.weatherapp.ui.adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weatherapp.R; // Đảm bảo import đúng R file của bạn
import com.example.weatherapp.forecast.ForecastItem;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {
    private List<ForecastItem> forecastList;

    public ForecastAdapter(List<ForecastItem> forecastList) {
        this.forecastList = forecastList;
    }
    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Cần tạo một layout file riêng cho mỗi item trong RecyclerView
        // Ví dụ: res/layout/item_forecast.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastItem forecastItem = forecastList.get(position);
        // Format ngày/giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, HH:mm", new Locale("vi")); // Ví dụ: "Thứ Tư, 15:00"
        Date date = new Date(forecastItem.getDt() * 1000L); // OpenWeatherMap sử dụng Unix timestamp (giây)
        holder.dateText.setText(dateFormat.format(date));

        // Tải icon thời tiết sử dụng Glide
        String iconUrl = "https://openweathermap.org/img/wn/" + forecastItem.getWeather().get(0).getIcon() + "@2x.png";
        Glide.with(holder.itemView.getContext())
                .load(iconUrl)
                .into(holder.iconImage);

        // Hiển thị nhiệt độ
        holder.tempText.setText(String.format(Locale.getDefault(), "%.0f°C", forecastItem.getMain().getTemp()));
    }
    @Override
    public int getItemCount() {
        return forecastList.size();
    }
    public void updateData(List<ForecastItem> newList){
        this.forecastList = newList;
        notifyDataSetChanged(); // Thông báo cho RecyclerView biết dữ liệu đã thay đổi
    }

}
