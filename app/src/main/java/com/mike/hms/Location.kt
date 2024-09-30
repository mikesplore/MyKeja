package com.mike.hms

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val LOCATION_PERMISSION_REQUEST_CODE = 1000

object LocationUtils {

    // Request both coarse and fine location permission
    fun requestLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Check if fine location permission is granted
    fun isLocationPermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Function to get the city/state name using Geocoder (run in background for compatibility)
    fun getLocation(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use Geocoder to convert lat/lng into a human-readable location asynchronously
                    fetchLocationInBackground(activity, location.latitude, location.longitude, onLocationReceived)
                } else {
                    requestLocationUpdates(activity, fusedLocationClient, onLocationReceived)
                }
            }
    }

    // Request location updates if lastLocation is null (using updated LocationRequest API)
    private fun requestLocationUpdates(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    fetchLocationInBackground(activity, location.latitude, location.longitude, onLocationReceived)
                    fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting the location
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // Function to handle Geocoding on a background thread
    private fun fetchLocationInBackground(activity: Activity, latitude: Double, longitude: Double, onLocationReceived: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1) // Synchronous call
                val result = if (addresses?.isNotEmpty() == true) {
                    val cityName = addresses[0].locality // City
                    val stateName = addresses[0].adminArea // State
                    cityName ?: stateName // Use city name if available, else state
                } else {
                    null
                }
                onLocationReceived(result)
            } catch (e: Exception) {
                e.printStackTrace()
                onLocationReceived(null)
            }
        }
    }
}
