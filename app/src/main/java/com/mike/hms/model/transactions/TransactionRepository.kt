package com.mike.hms.model.transactions

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TransactionRepository(private val transactionDao: TransactionDao) {

    private val database = FirebaseDatabase.getInstance().reference

    // Insert Transaction
    fun insertTransaction(transaction: TransactionEntity): Flow<Boolean> = flow {
        transactionDao.insertTransaction(transaction) // Insert locally into Room
        emit(insertTransactionToFirebase(transaction)) // Sync with Firebase
    }

    private suspend fun insertTransactionToFirebase(transaction: TransactionEntity): Boolean {
        return try {
            val transactionRef = database.child("Transactions").child(transaction.transactionID)
            transactionRef.setValue(transaction).await()
            true
        } catch (e: Exception) {
            Log.e("TransactionRepository", "Error inserting transaction: ${e.message}")
            false
        }
    }

    // Retrieve Transactions by User ID
    fun retrieveTransactionsByUserId(userId: String): Flow<Flow<List<TransactionEntity>>> = flow {
        emit(transactionDao.getTransactionsByUserId(userId)) // Emit Room data
        val transactions = retrieveTransactionsFromFirebase(userId) // Fetch from Firebase
        transactions.forEach { transaction ->
            transactionDao.insertTransaction(transaction) // Sync with Room
        }
    }.catch { e ->
        Log.e("TransactionRepository", "Error retrieving transactions: ${e.message}")
    }

    private suspend fun retrieveTransactionsFromFirebase(userId: String): List<TransactionEntity> {
        return try {
            val transactionsRef = database.child("Transactions").orderByChild("userId").equalTo(userId)
            val dataSnapshot = transactionsRef.get().await()
            dataSnapshot.children.mapNotNull { it.getValue(TransactionEntity::class.java) }
        } catch (e: Exception) {
            Log.e("TransactionRepository", "Error fetching transactions: ${e.message}")
            emptyList()
        }
    }

    // Delete All Transactions for a User
    fun deleteTransactionsForUser(userId: String): Flow<Boolean> = flow {
        val transactionsToDelete = transactionDao.getTransactionsByUserId(userId)
        transactionsToDelete.collect { transactions ->
            transactions.forEach { transaction ->
                transactionDao.insertTransaction(transaction)
                deleteTransactionFromFirebase(transaction.transactionID)
            }
        }
        emit(true)
    }

    private suspend fun deleteTransactionFromFirebase(transactionID: String): Boolean {
        return try {
            val transactionRef = database.child("Transactions").child(transactionID)
            transactionRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("TransactionRepository", "Error deleting transaction: ${e.message}")
            false
        }
    }
}
