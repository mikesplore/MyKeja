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
class PayPalViewModel @Inject constructor(
    private val payPalRepository: PayPalRepository
) : ViewModel() {

    private val _payPal = MutableStateFlow<PayPalWithUser?>(null)
    val payPal: StateFlow<PayPalWithUser?> = _payPal

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Insert Credit Card
    fun insertPayPal(payPal: PayPalEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            payPalRepository.insertPayPal(payPal)
                .catch { exception ->
                    Log.e(
                        "PayPalViewModel",
                        "Error inserting payPal: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                    onSuccess(false)
                }
                .collect { result ->
                    if (result) {
                        Log.d("PayPalViewModel", "Inserted payPal: $payPal")
                        onSuccess(true)
                        getPayPal(payPal.userId) // Fetch the updated payPal
                    } else {
                        _error.value = "Failed to insert payPal."
                        onSuccess(false)
                    }
                }
            _isLoading.value = false
        }
    }

    // Get Credit Card
    fun getPayPal(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            payPalRepository.retrievePayPalByUserId(userId)
                .catch { exception ->
                    Log.e(
                        "PayPalViewModel",
                        "Error retrieving payPal: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                }
                .collect { payPalFlow ->
                    payPalFlow
                        .catch { innerException ->
                            Log.e(
                                "PayPalViewModel",
                                "Error in inner flow: ${innerException.message}"
                            )
                            _error.value = innerException.localizedMessage
                        }
                        .collect { payPalWithUser ->
                            if (true) {
                                Log.d(
                                    "PayPalViewModel",
                                    "Retrieved payPal: $payPalWithUser"
                                )
                                _payPal.value = payPalWithUser
                            } else {
                                _error.value = "No payPal found for user ID: $userId"
                            }
                        }
                }
            _isLoading.value = false
        }
    }

    // Delete Credit Card
    fun deletePayPal(userId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            payPalRepository.deletePayPal(userId)
                .catch { exception ->
                    Log.e("PayPalViewModel", "Error deleting payPal: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { result ->
                    if (result) {
                        onSuccess(true)
                        Log.d("PayPalViewModel", "Deleted payPal for user ID: $userId")
                        _payPal.value = null // Clear the state after deletion
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to delete payPal for user ID: $userId"
                    }
                }
            _isLoading.value = false
        }
    }
}
