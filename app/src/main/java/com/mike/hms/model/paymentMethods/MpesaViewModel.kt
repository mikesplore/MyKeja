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
 * ViewModel for managing Mpesa payment data.
 *
 * Exposes state via flows to UI for data observation.
 *
 * @param mpesaRepository The repository managing Mpesa payment data.
 */
@HiltViewModel
class MpesaViewModel @Inject constructor(private val mpesaRepository: MpesaRepository) : ViewModel() {

    private val _mpesa = MutableStateFlow<MpesaWithUser?>(null)
    val mpesa: StateFlow<MpesaWithUser?> = _mpesa

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Retrieves Mpesa data by user ID.
     *
     * Updates `_mpesa` with the retrieved data or `_error` with an error message on failure.
     *
     * @param userId The ID of the user to fetch Mpesa data for.
     */
    fun getMpesa(userId: String) {
        viewModelScope.launch {
            mpesaRepository.retrieveMpesaByUserId(userId)
                .catch { e ->
                    _error.value = "Error fetching Mpesa data: ${e.message}"
                }
                .collect { retrievedMpesa ->
                    _mpesa.value = retrievedMpesa
                    _error.value = null // Clear any previous errors
                }
        }
    }

    /**
     * Inserts a new Mpesa payment record.
     *
     * Updates `_error` with an error message on failure or clears it on success.
     *
     * @param mpesa The MpesaEntity object to insert.
     */
    fun addMpesa(mpesa: MpesaEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            mpesaRepository.insertMpesa(mpesa)
                .catch { e ->
                    _error.value = "Error inserting Mpesa: ${e.message}"
                }
                .collect { isSuccess ->
                    if (isSuccess) {
                        onSuccess(true)
                        _error.value = null // Clear any previous errors
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to insert Mpesa data."
                    }
                }
        }
    }

    /**
     * Deletes an Mpesa payment record by user ID.
     *
     * Updates `_error` with an error message on failure or clears it on success.
     *
     * @param userId The ID of the user whose Mpesa data is being deleted.
     */
    fun deleteMpesa(userId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            mpesaRepository.deleteMpesa(userId)
                .catch { e ->
                    _error.value = "Error deleting Mpesa: ${e.message}"
                }
                .collect { isSuccess ->
                    if (isSuccess) {
                        onSuccess(true)
                        _mpesa.value = null // Clear cached Mpesa data
                        _error.value = null // Clear any previous errors
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to delete Mpesa data."
                    }
                }
        }
    }
}
