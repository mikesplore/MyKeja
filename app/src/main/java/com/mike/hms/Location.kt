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

    // Request both coarse and fine location permissions
    fun requestLocationPermission(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Check if both location permissions are granted
    fun isLocationPermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Function to get precise location
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

    // Request precise location updates with high accuracy
    private fun requestLocationUpdates(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(10000)
            .setWaitForAccurateLocation(true)
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

    // Enhanced location fetching with more detailed address information
    private fun fetchLocationInBackground(activity: Activity, latitude: Double, longitude: Double, onLocationReceived: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val geocoder = Geocoder(activity, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)

                val result = if (addresses?.isNotEmpty() == true) {
                    val address = addresses[0]
                    buildString {
                        address.locality?.let { append(it) }  // City
                        address.adminArea?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)  // State
                        }
                        address.countryName?.let {
                            if (isNotEmpty()) append(", ")
                            append(it)  // Country
                        }
                    }.takeIf { it.isNotEmpty() }
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