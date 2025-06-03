package com.example.weatherapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.R;
import com.example.weatherapp.api.RetrofitClient;
import com.example.weatherapp.model.CurrentWeatherResponse;
import com.example.weatherapp.model.ForecastResponse;
import com.example.weatherapp.ui.fragment.ClothingSuggestionFragment;
import com.example.weatherapp.ui.fragment.WeatherForecastFragment;
import com.example.weatherapp.utils.LocationHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private LocationHelper locationHelper;
    private final String OPENWEATHER_API_KEY = "1acfe1051af8a21a693760b0291b9ac4";
    private ActivityResultLauncher<String[]> requestPermissionLaunche;
    private WeatherForecastFragment weatherForecastFragment;
    private ClothingSuggestionFragment clothingSuggestionFragment;
    private CurrentWeatherResponse currentWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo các fragment
        weatherForecastFragment = new WeatherForecastFragment();
        clothingSuggestionFragment = new ClothingSuggestionFragment();

        // Thiết lập ViewPager2
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? weatherForecastFragment : clothingSuggestionFragment;
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        // Thiết lập TabLayout
        tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Dự báo thời tiết" : "Đề xuất trang phục");
        }).attach();

        //Lưu trữ dữ liệu thời tiết hiện tại 
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Khi chuyển tab, cập nhật lại dữ liệu cho fragment được chọn
                if (currentWeatherData != null) {
                    if (position == 0) {
                        weatherForecastFragment.updateWeatherData(currentWeatherData);
                    } else {
                        clothingSuggestionFragment.updateWeatherData(currentWeatherData);
                    }
                }
            }
        });


        // Khởi tạo LocationHelper
        locationHelper = new LocationHelper(this);

        // Khởi tạo request launcher cho quyền
        requestPermissionLaunche = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    Boolean fineLocationGranted = permissions.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (fineLocationGranted != null && fineLocationGranted || coarseLocationGranted != null && coarseLocationGranted) {
                        getLocationAndFetchWeather();
                    } else {
                        Toast.makeText(this, "Cần quyền truy cập vị trí để hiển thị thời tiết.", Toast.LENGTH_SHORT).show();
                        fetchWeatherForDefaultLocation();
                    }
                });

        // Kiểm tra và yêu cầu quyền truy cập vị trí
        checkLocationPermission();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkLocationPermission() {
        if (locationHelper.checkLocationPermission()) {
            getLocationAndFetchWeather();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Ứng dụng cần quyền truy cập vị trí để hiển thị thời tiết chính xác.", Toast.LENGTH_LONG).show();
            requestPermissionLaunche.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );
        } else {
            requestPermissionLaunche.launch(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }
            );
        }
    }

    private void getLocationAndFetchWeather() {
        locationHelper.getCurrentLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Log.d(TAG, "Vị trí hiện tại: " + location.getLatitude() + ", " + location.getLongitude());
                fetchWeatherData(location.getLatitude(), location.getLongitude());
            } else {
                Log.w(TAG, "Không thể lấy vị trí hiện tại, thử lấy vị trí cuối cùng biết được.");
                locationHelper.getLastLocation().addOnSuccessListener(MainActivity.this, lastKnownLocation -> {
                    if (lastKnownLocation != null) {
                        Log.d(TAG, "Vị trí cuối cùng biết được: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
                        fetchWeatherData(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    } else {
                        Log.e(TAG, "Không thể lấy bất kỳ vị trí nào.");
                        Toast.makeText(MainActivity.this, "Không thể lấy vị trí của bạn.", Toast.LENGTH_SHORT).show();
                        fetchWeatherForDefaultLocation();
                    }
                }).addOnFailureListener(MainActivity.this, e -> {
                    Log.e(TAG, "Lỗi khi lấy vị trí cuối cùng: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    fetchWeatherForDefaultLocation();
                });
            }
        }).addOnFailureListener(this, e -> {
            Log.e(TAG, "Lỗi khi lấy vị trí hiện tại: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            fetchWeatherForDefaultLocation();
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
                            updateWeatherUI(weatherData);
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
        RetrofitClient.getInstance().getOpenWeatherApiService().getForecast(lat, lon, OPENWEATHER_API_KEY, "metric", "vi")
                .enqueue(new Callback<ForecastResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ForecastResponse> call, @NonNull Response<ForecastResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ForecastResponse forecastData = response.body();
                            Log.d(TAG, "Dữ liệu dự báo: " + forecastData.getList().size() + " mục");
                            weatherForecastFragment.updateForecastData(forecastData);
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

    private void updateWeatherUI(CurrentWeatherResponse weatherData) {
        if (weatherData != null) {
            // Lưu trữ dữ liệu thời tiết hiện tại
            currentWeatherData = weatherData;
            //Cập nhật UI cho fragment hiện tại
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition == 0) {
                weatherForecastFragment.updateWeatherData(weatherData);
            } else {
                clothingSuggestionFragment.updateWeatherData(weatherData);
            }
        }
    }

    private void fetchWeatherForDefaultLocation() {
        double defaultLat = 21.0285;
        double defaultLon = 105.8542;
        Toast.makeText(this, "Hiển thị thời tiết mặc định cho Hà Nội.", Toast.LENGTH_SHORT).show();
        fetchWeatherData(defaultLat, defaultLon);
    }
}
