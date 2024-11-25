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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Retrieves transactions for a specific user by their ID.
     * Updates the `_transactions` StateFlow with the results or sets an error message if the operation fails.
     *
     * @param userId The ID of the user whose transactions are to be fetched.
     */
    fun fetchTransactions(userId: String) {
        viewModelScope.launch {
            repository.retrieveTransactionsByUserId(userId)
                .catch { e ->
                    _error.value = "Error fetching transactions: ${e.message}"
                }
                .collect { transactions ->
                    _transactions.value = transactions
                }
        }
    }

    /**
     * Adds a new transaction to the repository.
     * Updates the `_transactions` StateFlow after successful insertion or sets an error message on failure.
     *
     * @param transaction The transaction to add.
     */
    fun addTransaction(transaction: TransactionEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
                .catch { e ->
                    _error.value = "Error adding transaction: ${e.message}"
                }
                .collect { success ->
                    if (success) {
                        onSuccess(true)
                        fetchTransactions(transaction.userId) // Refresh the transactions list
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to add transaction."
                    }
                }
        }
    }

    /**
     * Clears the error message from the `_error` StateFlow.
     */
    fun clearError() {
        _error.value = null
    }
}
