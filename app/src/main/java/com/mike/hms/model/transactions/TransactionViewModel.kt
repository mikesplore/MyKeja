package com.mike.hms.model.transactions

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
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Insert Transaction
    fun insertTransaction(transaction: TransactionEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertTransaction(transaction)
                .catch { exception ->
                    Log.e("TransactionViewModel", "Error inserting transaction: ${exception.message}")
                    _error.value = exception.localizedMessage
                    onSuccess(false)
                }
                .collect { result ->
                    if (result) {
                        Log.d("TransactionViewModel", "Inserted transaction: $transaction")
                        onSuccess(true)
                        getTransactions(transaction.userId) // Refresh data
                    } else {
                        _error.value = "Failed to insert transaction."
                        onSuccess(false)
                    }
                }
            _isLoading.value = false
        }
    }

    // Get Transactions by User ID
    fun getTransactions(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.retrieveTransactionsByUserId(userId)
                .catch { exception ->
                    Log.e("TransactionViewModel", "Error retrieving transactions: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { transactionsFlow ->
                    transactionsFlow
                        .catch { innerException ->
                            Log.e(
                                "TransactionViewModel",
                                "Error in inner flow: ${innerException.message}"
                            )
                            _error.value = innerException.localizedMessage
                        }
                        .collect { retrievedTransactions ->
                            if (retrievedTransactions.isNotEmpty()) {
                                Log.d("TransactionViewModel", "Retrieved transactions: $retrievedTransactions")
                                _transactions.value = retrievedTransactions
                            } else {
                                _error.value = "No transactions found for user ID: $userId"
                            }
                        }
                }
            _isLoading.value = false
        }
    }


}
