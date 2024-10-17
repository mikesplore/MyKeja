package com.mike.hms

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


object HMSPreferences {
    // Keys for SharedPreferences
    private const val PREF_KEY_DARK_MODE = "dark_mode_key"
    private const val LOCATION_KEY = "location_key"
    private const val PREF_KEY_USER_ID = "user_id_key"




    private lateinit var preferences: SharedPreferences


    // MutableStates to hold preference values
    val darkMode: MutableState<Boolean> = mutableStateOf(false)
    val location: MutableState<String> = mutableStateOf("")
    val userId: MutableState<String> = mutableStateOf("")


    // Initialize preferences and load stored values
    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        // Load values from SharedPreferences

        darkMode.value = preferences.getBoolean(PREF_KEY_DARK_MODE, true)
        location.value = preferences.getString(LOCATION_KEY, "") ?: ""
        userId.value = preferences.getString(PREF_KEY_USER_ID, "") ?: ""

    }


    // Save dark mode preference
    fun saveDarkModePreference(isDarkMode: Boolean) {
        darkMode.value = isDarkMode
        preferences.edit().putBoolean(PREF_KEY_DARK_MODE, isDarkMode).apply()
        Log.d("HMSPreferences", "Dark mode saved: $isDarkMode")
    }

    // Save location preference
    fun saveLocation(location: String) {
        this.location.value = location
        preferences.edit().putString(LOCATION_KEY, location).apply()
        Log.d("HMSPreferences", "Location saved: $location")
    }

    // Save user ID preference
    fun saveUserId(userId: String) {
        this.userId.value = userId
        preferences.edit().putString(PREF_KEY_USER_ID, userId).apply()
        Log.d("HMSPreferences", "User ID saved: $userId")
    }



    fun clearAllData() {
        darkMode.value = true
        location.value = ""
        userId.value = ""

        preferences.edit().clear().apply()
        Log.d("HMSPreferences", "All preferences cleared")
    }
}