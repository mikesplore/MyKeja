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
class CreditCardViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository
) : ViewModel() {

    private val _creditCard = MutableStateFlow<CreditCardWithUser?>(null)
    val creditCard: StateFlow<CreditCardWithUser?> = _creditCard

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Insert Credit Card
    fun insertCreditCard(creditCard: CreditCardEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            creditCardRepository.insertCreditCard(creditCard)
                .catch { exception ->
                    Log.e(
                        "CreditCardViewModel",
                        "Error inserting credit card: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                    onSuccess(false)
                }
                .collect { result ->
                    if (result) {
                        Log.d("CreditCardViewModel", "Inserted credit card: $creditCard")
                        onSuccess(true)
                        getCreditCard(creditCard.userId) // Fetch the updated credit card
                    } else {
                        _error.value = "Failed to insert credit card."
                        onSuccess(false)
                    }
                }
            _isLoading.value = false
        }
    }

    // Get Credit Card
    fun getCreditCard(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            creditCardRepository.retrieveCreditCardByUserId(userId)
                .catch { exception ->
                    Log.e(
                        "CreditCardViewModel",
                        "Error retrieving credit card: ${exception.message}"
                    )
                    _error.value = exception.localizedMessage
                }
                .collect { creditCardWithUser ->
                    _creditCard.value = creditCardWithUser
                    Log.d("CreditCardViewModel", "Retrieved credit card: $creditCardWithUser")
                    // You can access individual fields like this:
                     Log.d("CreditCardViewModel", "Card Number: ${creditCardWithUser.creditCard.cardNumber}")
                    // ... other fields ...
                }
            _isLoading.value = false
        }
    }

    // Delete Credit Card
    fun deleteCreditCard(userId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            creditCardRepository.deleteCreditCard(userId)
                .catch { exception ->
                    Log.e("CreditCardViewModel", "Error deleting credit card: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { result ->
                    if (result) {
                        onSuccess(true)
                        Log.d("CreditCardViewModel", "Deleted credit card for user ID: $userId")
                        _creditCard.value = null // Clear the state after deletion
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to delete credit card for user ID: $userId"
                    }
                }
            _isLoading.value = false
        }
    }
}
