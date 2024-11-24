package com.mike.hms.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mike.hms.model.statements.StatementEntity
import com.mike.hms.model.statements.StatementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(
    private val repository: StatementRepository
) : ViewModel() {

    private val _statements = MutableStateFlow<List<StatementEntity>>(emptyList())
    val statements: StateFlow<List<StatementEntity>> = _statements

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Insert Statement
    fun insertStatement(statement: StatementEntity, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertStatement(statement)
                .catch { exception ->
                    Log.e("StatementViewModel", "Error inserting statement: ${exception.message}")
                    _error.value = exception.localizedMessage
                    onSuccess(false)
                }
                .collect { result ->
                    if (result) {
                        Log.d("StatementViewModel", "Inserted statement: $statement")
                        onSuccess(true)
                        getStatements(statement.userId) // Refresh data
                    } else {
                        _error.value = "Failed to insert statement."
                        onSuccess(false)
                    }
                }
            _isLoading.value = false
        }
    }

    // Get Statements by User ID
    fun getStatements(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.retrieveStatementsByUserId(userId)
                .catch { exception ->
                    Log.e("StatementViewModel", "Error retrieving statements: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { statementsFlow ->
                    statementsFlow
                        .catch { innerException ->
                            Log.e(
                                "StatementViewModel",
                                "Error in inner flow: ${innerException.message}"
                            )
                            _error.value = innerException.localizedMessage
                        }
                        .collect { retrievedStatements ->
                            if (retrievedStatements.isNotEmpty()) {
                                Log.d("StatementViewModel", "Retrieved statements: $retrievedStatements")
                                _statements.value = retrievedStatements
                            } else {
                                _error.value = "No statements found for user ID: $userId"
                            }
                        }
                }
            _isLoading.value = false
        }
    }

    // Delete Statements by User ID
    fun deleteStatements(userId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteStatementsForUser(userId)
                .catch { exception ->
                    Log.e("StatementViewModel", "Error deleting statements: ${exception.message}")
                    _error.value = exception.localizedMessage
                }
                .collect { result ->
                    if (result) {
                        onSuccess(true)
                        Log.d("StatementViewModel", "Deleted statements for user ID: $userId")
                        _statements.value = emptyList()
                    } else {
                        onSuccess(false)
                        _error.value = "Failed to delete statements for user ID: $userId"
                    }
                }
            _isLoading.value = false
        }
    }
}
