package com.mike.hms.model.creditCardModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditCardViewModel @Inject constructor(private val creditCardRepository: CreditCardRepository) : ViewModel() {

    private val _creditCard = MutableLiveData<CreditCardWithUser>()
    val creditCard: LiveData<CreditCardWithUser> = _creditCard

    private val _insertResult = MutableLiveData<Boolean>()
    val insertResult: LiveData<Boolean> = _insertResult

    private val _deleteResult = MutableLiveData<Boolean>()
    val deleteResult: LiveData<Boolean> = _deleteResult

    fun insertCreditCard(creditCard: CreditCardEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            creditCardRepository.insertCreditCard(creditCard).collect { result ->
                Log.d("CreditCardViewModel", "Inserted credit card: $creditCard, success: $result")
                _insertResult.postValue(result)
                if (result) {
                    onSuccess(true)
                    getCreditCard(creditCard.userId)
                }
                else {
                    onSuccess(false)
                }
            }
        }
    }

    fun getCreditCard(userId: String) {
        viewModelScope.launch {
            Log.d("CreditCardViewModel", "Getting credit card for user: $userId")
            creditCardRepository.retrieveCreditCardByUserId(userId).collect { creditCardFlow ->
                creditCardFlow.collect { creditCardWithUser ->
                    Log.d("CreditCardViewModel", "Retrieved credit card: $creditCardWithUser")
                    _creditCard.postValue(creditCardWithUser)
                }
            }
        }
    }

    fun deleteCreditCard(userId: String) {
        viewModelScope.launch {
            creditCardRepository.deleteCreditCard(userId).collect { result ->
                Log.d("CreditCardViewModel", "Deleted credit card for user $userId, success: $result")
                _deleteResult.postValue(result)
            }
        }
    }
}
