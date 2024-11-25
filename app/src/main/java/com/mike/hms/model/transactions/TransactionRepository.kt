package com.mike.hms.model.transactions

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TransactionRepository(private val transactionDao: TransactionDao) {

    private val database = FirebaseDatabase.getInstance().reference
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Initialize Firebase-to-Room synchronization
        startFirebaseSync()
    }

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
    fun retrieveTransactionsByUserId(userId: String): Flow<List<TransactionEntity>> = flow {
        emit(transactionDao.getTransactionsByUserId(userId).firstOrNull() ?: emptyList()) // Emit local Room data
        val transactions = retrieveTransactionsFromFirebase(userId) // Fetch from Firebase
        transactions.forEach { transaction ->
            transactionDao.insertTransaction(transaction) // Sync with Room
        }
        emit(transactions)
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

    /**
     * Synchronizes Firebase data to the local Room database in real-time.
     *
     * This method listens for changes in the Firebase "Transactions" node and updates the local database
     * accordingly. It is automatically called when the repository is instantiated.
     */
    private fun startFirebaseSync() {
        val transactionsRef = database.child("Transactions")

        transactionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Update Room database with Firebase changes
                snapshot.children.forEach { dataSnapshot ->
                    val transaction = dataSnapshot.getValue(TransactionEntity::class.java)
                    if (transaction != null) {
                        scope.launch{
                            transactionDao.insertTransaction(transaction)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TransactionRepository", "Firebase sync cancelled: ${error.message}")
            }
        })
    }
}
