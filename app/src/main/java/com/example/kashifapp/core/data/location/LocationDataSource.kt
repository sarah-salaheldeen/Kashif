package com.example.kashifapp.core.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import com.example.kashifapp.core.domain.model.UserLocation
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): Result<UserLocation, DataError.Location> {
        // Check if location services are enabled before anything else
        if (!isLocationEnabled()) {
            return Result.Error(DataError.Location.DISABLED)
        }

        val hasPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return Result.Error(DataError.Location.PERMISSION_DENIED)
        }
        return try {
            // Try last known location first — fast and battery-friendly
            val lastLocation = fusedClient.lastLocation.await()
            if (lastLocation != null) {
                Result.Success(
                    UserLocation(lastLocation.latitude, lastLocation.longitude)
                )
            } else {
                // No last location — request a fresh one
                requestFreshLocation()
            }
        } catch (e: Exception) {
            Result.Error(DataError.Location.UNAVAILABLE)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    private suspend fun requestFreshLocation(): Result<UserLocation, DataError.Location> {
        return try {
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMaxUpdateAgeMillis(60_000L)
                .build()

            val location = fusedClient.getCurrentLocation(request, null).await()
                ?: return Result.Error(DataError.Location.UNAVAILABLE)

            Result.Success(UserLocation(location.latitude, location.longitude))
        } catch (e: Exception) {
            Result.Error(DataError.Location.UNAVAILABLE)
        }
    }
}