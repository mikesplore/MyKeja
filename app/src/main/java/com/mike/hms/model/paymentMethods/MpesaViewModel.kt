package com.mike.hms.model.paymentMethods

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MpesaViewModel @Inject constructor(
    private val mpesaRepository: MpesaRepository
) : ViewModel() {

    private val _mpesa = MutableStateFlow<MpesaWithUser?>(null)
    val mpesa: StateFlow<MpesaWithUser?> = _mpesa

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Insert Pay Pal
    fun insertMpesa(mpesa: MpesaEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            mpesaRepository.insertMpesa(mpesa)
                .catch { exception ->
                    Log.e(
                        "MpesaViewModel",
                        "Error inserting mpesa: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                    onSuccess(false)
                }
                .collect { result ->
                    if (result) {
                        Log.d("MpesaViewModel", "Inserted mpesa: $mpesa")
                        onSuccess(true)
                        getMpesa(mpesa.userId) // Fetch the updated mpesa
                    } else {
                        _error.value = "Failed to insert mpesa."
                        onSuccess(false)
                    }
                }
            _isLoading.value = false
        }
    }

    // Get Credit Card
    fun getMpesa(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            mpesaRepository.retrieveMpesaByUserId(userId)
                .catch { exception ->
                    Log.e(
                        "MpesaViewModel",
                        "Error retrieving mpesa: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                }
                .collect { mpesaFlow ->
                    mpesaFlow
                        .catch { innerException ->
                            Log.e(
                                "MpesaViewModel",
                                "Error in inner flow: ${innerException.message}"
                            )
                            _error.value = innerException.localizedMessage
                        }
                        .collect { mpesaWithUser ->
                            if (true) {
                                Log.d(
                                    "MpesaViewModel",
                                    "Retrieved mpesa: $mpesaWithUser"
                                )
                                _mpesa.value = mpesaWithUser
                            } else {
                                _error.value = "No mpesa found for user ID: $userId"
                            }
                        }
                }
            _isLoading.value = false
        }
    }

    // Delete Credit Card
    fun deleteMpesa(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            mpesaRepository.deleteMpesa(userId)
                .catch { exception ->
                    Log.e("MpesaViewModel", "Error deleting mpesa: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { result ->
                    if (result) {
                        Log.d("MpesaViewModel", "Deleted mpesa for user ID: $userId")
                        _mpesa.value = null // Clear the state after deletion
                    } else {
                        _error.value = "Failed to delete payPal for user ID: $userId"
                    }
                }
            _isLoading.value = false
        }
    }
}
