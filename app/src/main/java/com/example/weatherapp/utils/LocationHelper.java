package com.example.weatherapp.utils;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
public class LocationHelper {
    private final Context context;
    private final com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    public LocationHelper(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }
    public boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    // Lấy vị trí cuối cùng
    @SuppressLint("MissingPermission")
    public Task<Location> getLastLocation() {
        if (!checkLocationPermission()) {
            // Trả về Task bị lỗi nếu không có quyền
            // Tạo một Task bị lỗi để xử lý ở nơi gọi
            com.google.android.gms.tasks.TaskCompletionSource<Location> taskCompletionSource = new com.google.android.gms.tasks.TaskCompletionSource<>();
            taskCompletionSource.setException(new SecurityException("Location permission not granted"));
            return taskCompletionSource.getTask();
        }
        return fusedLocationClient.getLastLocation();
    }
    // Lấy vị trí hiện tại
    @SuppressLint("MissingPermission") // Permission được kiểm tra trước khi gọi hàm này
    public Task<Location> getCurrentLocation() {
        if (!checkLocationPermission()) {
            // Trả về Task bị lỗi nếu không có quyền
            com.google.android.gms.tasks.TaskCompletionSource<Location> taskCompletionSource = new com.google.android.gms.tasks.TaskCompletionSource<>();
            taskCompletionSource.setException(new SecurityException("Location permission not granted"));
            return taskCompletionSource.getTask();
        }
        return fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null);
    }

}
