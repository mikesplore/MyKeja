package com.mike.hms

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import java.util.*

const val LOCATION_PERMISSION_REQUEST_CODE = 1000

object LocationUtils {

    // Request coarse location permission
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

    // Function to get the city/state name using Geocoder
    fun getLocation(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use Geocoder to convert lat/lng into a human-readable location
                    val geocoder = Geocoder(activity, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val cityName = addresses[0].locality // City
                        val stateName = addresses[0].adminArea // State
                        onLocationReceived(cityName ?: stateName) // Use city name if available, else state
                    } else {
                        onLocationReceived(null)
                    }
                } else {
                    requestLocationUpdates(activity, fusedLocationClient, onLocationReceived)
                }
            }
    }

    // Request location updates if lastLocation is null
    private fun requestLocationUpdates(activity: Activity, fusedLocationClient: FusedLocationProviderClient, onLocationReceived: (String?) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    // Use Geocoder to get the city or state
                    val geocoder = Geocoder(activity, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val cityName = addresses[0].locality
                        val stateName = addresses[0].adminArea
                        onLocationReceived(cityName ?: stateName)
                    } else {
                        onLocationReceived(null)
                    }
                    fusedLocationClient.removeLocationUpdates(this) // Stop updates after getting the location
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}
