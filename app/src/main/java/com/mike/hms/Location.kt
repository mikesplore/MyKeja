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

    // Request only coarse location permission
    fun requestLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Check if coarse location permission is granted
    fun isLocationPermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Function to get approximate location to fetch city name
    fun getLocation(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        if (!isLocationPermissionGranted(activity)) {
            requestLocationPermission(activity)
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        fetchLocationInBackground(activity, location.latitude, location.longitude, onLocationReceived)
                    } else {
                        requestLocationUpdates(activity, fusedLocationClient, onLocationReceived)
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    onLocationReceived(null)
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
            onLocationReceived(null)
        }
    }

    // Request location updates with balanced accuracy (coarse location)
    private fun requestLocationUpdates(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(10000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    fetchLocationInBackground(activity, location.latitude, location.longitude, onLocationReceived)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }

        try {
            if (isLocationPermissionGranted(activity)) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            onLocationReceived(null)
        }
    }

    // Function to fetch only the city name from the latitude and longitude
    private fun fetchLocationInBackground(activity: Activity, latitude: Double, longitude: Double, onLocationReceived: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                val cityName = if (addresses?.isNotEmpty() == true) {
                    addresses[0].locality // City name
                } else {
                    null
                }
                onLocationReceived(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                onLocationReceived(null)
            }
        }
    }
}
