package com.mike.hms.model.paymentMethods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing PayPal payment data.
 *
 * Exposes state via flows to UI for data observation.
 *
 * @param paypalRepository The repository managing PayPal payment data.
 */
@HiltViewModel
class PayPalViewModel @Inject constructor(private val paypalRepository: PayPalRepository) : ViewModel() {

    private val _paypal = MutableStateFlow<PayPalWithUser?>(null)
    val paypal: StateFlow<PayPalWithUser?> = _paypal

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Retrieves PayPal data by user ID.
     *
     * Updates `_paypal` with the retrieved data or `_error` with an error message on failure.
     *
     * @param userId The ID of the user to fetch PayPal data for.
     */
    fun getPayPal(userId: String) {
        viewModelScope.launch {
            paypalRepository.retrievePayPalByUserId(userId)
                .catch { e ->
                    _error.value = "Error fetching PayPal data: ${e.message}"
                }
                .collect { retrievedPayPal ->
                    _paypal.value = retrievedPayPal
                    _error.value = null // Clear any previous errors
                }
        }
    }

    /**
     * Inserts a new PayPal payment record.
     *
     * Updates `_error` with an error message on failure or clears it on success.
     *
     * @param paypal The PayPalEntity object to insert.
     */
    fun addPayPal(paypal: PayPalEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            paypalRepository.insertPayPal(paypal)
                .catch { e ->
                    _error.value = "Error inserting PayPal: ${e.message}"
                }
                .collect { isSuccess ->
                    if (isSuccess) {
                        onSuccess(true)
                        _error.value = null // Clear any previous errors
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to insert PayPal data."
                    }
                }
        }
    }

    /**
     * Deletes an PayPal payment record by user ID.
     *
     * Updates `_error` with an error message on failure or clears it on success.
     *
     * @param userId The ID of the user whose PayPal data is being deleted.
     */
    fun deletePayPal(userId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            paypalRepository.deletePayPal(userId)
                .catch { e ->
                    _error.value = "Error deleting PayPal: ${e.message}"
                }
                .collect { isSuccess ->
                    if (isSuccess) {
                        onSuccess(true)
                        _paypal.value = null // Clear cached PayPal data
                        _error.value = null // Clear any previous errors
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to delete PayPal data."
                    }
                }
        }
    }
}
