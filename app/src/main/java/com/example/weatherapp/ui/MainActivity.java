package com.example.weatherapp.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherapp.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.weatherapp.api.RetrofitClient;
import com.example.weatherapp.model.CurrentWeatherResponse;
import com.example.weatherapp.model.ForecastResponse;
import com.example.weatherapp.ui.adapter.ForecastAdapter;
import com.example.weatherapp.utils.LocationHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // Khai báo các View từ layout XML
    private TextView locationText;
    private TextView dateText;
    private ImageView weatherIcon;
    private TextView temperatureText;
    private TextView descriptionText;
    private RecyclerView forecastRecView;
    private LocationHelper locationHelper;
    private ForecastAdapter forecastAdapter;
    private final String OPENWEATHER_API_KEY = "88b25bb0c905f69e674b77d0b30efd97";
    // Request launcher cho quyền truy cập vị trí
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Ánh xạ các View từ layout
        locationText = findViewById(R.id.locationText);
        dateText = findViewById(R.id.dateText);
        weatherIcon = findViewById(R.id.weatherIcon);
        temperatureText = findViewById(R.id.temperatureText);
        descriptionText = findViewById(R.id.descriptionText);
        forecastRecView = findViewById(R.id.forecastRecView);
        locationHelper = new LocationHelper(this);

        // Cấu hình RecyclerView cho dự báo
        forecastAdapter = new ForecastAdapter(new ArrayList<>()); // Bắt đầu với danh sách trống
        forecastRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Scroll ngang
        forecastRecView.setAdapter(forecastAdapter);
        // Khởi tạo request launcher cho quyền
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    Boolean fineLocationGranted = permissions.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (fineLocationGranted != null && fineLocationGranted || coarseLocationGranted != null && coarseLocationGranted) {
                        // Quyền đã được cấp, tiến hành lấy vị trí
                        getLocationAndFetchWeather();
                    } else {
                        // Quyền bị từ chối, hiển thị thông báo cho người dùng
                        Toast.makeText(this, "Cần quyền truy cập vị trí để hiển thị thời tiết.", Toast.LENGTH_SHORT).show();
                        // Có thể hiển thị thời tiết mặc định cho một thành phố nào đó
                        fetchWeatherForDefaultLocation();
                    }
                });
        // Kiểm tra và yêu cầu quyền truy cập vị trí
        checkLocationPermission();
        // Cập nhật ngày hiện tại
        updateCurrentDate();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkLocationPermission() {
        if (locationHelper.checkLocationPermission()) {
            // Quyền đã được cấp
            getLocationAndFetchWeather();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Giải thích lý do cần quyền cho người dùng (tùy chọn)
            Toast.makeText(this, "Ứng dụng cần quyền truy cập vị trí để hiển thị thời tiết chính xác.", Toast.LENGTH_LONG).show();
            requestPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );
        } else {
            // Yêu cầu quyền lần đầu hoặc sau khi người dùng từ chối và không chọn "Don't ask again"
            requestPermissionLauncher.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );
        }
    }

    private void updateCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d/M", new Locale("vi")); // Ví dụ: "Thứ Tư, 9/5"
        Date currentDate = new Date();
        dateText.setText(dateFormat.format(currentDate));
    }

    private void getLocationAndFetchWeather() {
        // Cố gắng lấy vị trí hiện tại
        locationHelper.getCurrentLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "Vị trí hiện tại: " + location.getLatitude() + ", " + location.getLongitude());
                    fetchWeatherData(location.getLatitude(), location.getLongitude());
                } else {
                    Log.w(TAG, "Không thể lấy vị trí hiện tại, thử lấy vị trí cuối cùng biết được.");
                    // Thử lấy vị trí cuối cùng biết được
                    locationHelper.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location lastKnownLocation) {
                            if (lastKnownLocation != null) {
                                Log.d(TAG, "Vị trí cuối cùng biết được: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
                                fetchWeatherData(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                            } else {
                                Log.e(TAG, "Không thể lấy bất kỳ vị trí nào.");
                                Toast.makeText(MainActivity.this, "Không thể lấy vị trí của bạn.", Toast.LENGTH_SHORT).show();
                                // Hiển thị thời tiết mặc định nếu không lấy được vị trí
                                fetchWeatherForDefaultLocation();
                            }
                        }
                    }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Lỗi khi lấy vị trí cuối cùng: " + e.getMessage());
                            Toast.makeText(MainActivity.this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            // Hiển thị thời tiết mặc định nếu gặp lỗi khi lấy vị trí
                            fetchWeatherForDefaultLocation();
                        }
                    });
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Lỗi khi lấy vị trí hiện tại: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                // Hiển thị thời tiết mặc định nếu gặp lỗi khi lấy vị trí
                fetchWeatherForDefaultLocation();
            }
        });
    }
    private void fetchWeatherData(double lat, double lon) {
        // Gọi API thời tiết hiện tại
        RetrofitClient.getInstance().getOpenWeatherApiService().getCurrentWeather(lat, lon, OPENWEATHER_API_KEY, "metric", "vi")
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrentWeatherResponse> call, @NonNull Response<CurrentWeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CurrentWeatherResponse weatherData = response.body();
                            Log.d(TAG, "Dữ liệu thời tiết hiện tại: " + weatherData.getName());
                            updateCurrentWeatherUI(weatherData);
                        } else {
                            try {
                                Log.e(TAG, "Lỗi khi gọi API thời tiết hiện tại: " + response.errorBody().string());
                            } catch (Exception e) {
                                Log.e(TAG, "Lỗi khi đọc error body: " + e.getMessage());
                            }
                            Toast.makeText(MainActivity.this, "Không thể tải dữ liệu thời tiết hiện tại.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CurrentWeatherResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Lỗi mạng hoặc xử lý API thời tiết hiện tại: " + t.getMessage());
                        Toast.makeText(MainActivity.this, "Lỗi kết nối hoặc xử lý dữ liệu thời tiết hiện tại.", Toast.LENGTH_SHORT).show();
                    }
                });

        // Gọi API dự báo thời tiết
        Call<ForecastResponse> forecastCall = RetrofitClient.getInstance().getOpenWeatherApiService().getForecast(lat, lon, OPENWEATHER_API_KEY, "metric", "vi");
        forecastCall.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ForecastResponse forecastData = response.body();
                    Log.d(TAG, "Dữ liệu dự báo: " + forecastData.getList().size() + " mục");
                    // Cập nhật RecyclerView với dữ liệu dự báo
                    forecastAdapter.updateData(forecastData.getList());
                } else {
                    try {
                        Log.e(TAG, "Lỗi khi gọi API dự báo: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi khi đọc error body: " + e.getMessage());
                    }
                    Toast.makeText(MainActivity.this, "Không thể tải dữ liệu dự báo.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Lỗi mạng hoặc xử lý API dự báo: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối hoặc xử lý dữ liệu dự báo.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateCurrentWeatherUI(CurrentWeatherResponse weatherData) {
        locationText.setText(weatherData.getName()); // Tên thành phố từ API
        temperatureText.setText(String.format(Locale.getDefault(), "%.0f°C", weatherData.getMain().getTemp())); // Nhiệt độ làm tròn
        descriptionText.setText(weatherData.getWeather().get(0).getDescription()); // Mô tả thời tiết

        // Tải icon thời tiết bằng Glide
        String iconCode = weatherData.getWeather().get(0).getIcon();
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Glide.with(this)
                .load(iconUrl)
                .into(weatherIcon);

        // TODO: Cập nhật thêm các thông tin khác nếu có trong layout (độ ẩm, tốc độ gió...)
        // Ví dụ:
        // TextView humidityValueText = findViewById(R.id.humidityValueText);
        // if (humidityValueText != null) {
        //     humidityValueText.setText(weatherData.getMain().getHumidity() + "%");
        // }
        // TextView windSpeedValueText = findViewById(R.id.windSpeedValueText);
        // if (windSpeedValueText != null) {
        //     windSpeedValueText.setText(weatherData.getWind().getSpeed() + " m/s");
        // }
    }

    private void fetchWeatherForDefaultLocation() {
        double defaultLat = 21.0285;
        double defaultLon = 105.8542;
        Toast.makeText(this, "Hiển thị thời tiết mặc định cho Hà Nội.", Toast.LENGTH_SHORT).show();
        fetchWeatherData(defaultLat, defaultLon);
        // Cập nhật tên địa điểm trên UI cho biết đây là địa điểm mặc định
        locationText.setText("Hà Nội");

    }

}
