package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.*
import com.mike.hms.model.userModel.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PayPalRepository(private val payPalDao: PayPalDao) {

    private val database = FirebaseDatabase.getInstance().reference
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Start syncing Firebase to Room
        startFirebaseSync()
    }

    // Insert PayPal
    fun insertPayPal(payPal: PayPalEntity): Flow<Boolean> = flow {
        payPalDao.insertPayPal(payPal)
        emit(insertPayPalToFirebase(payPal))
    }

    private suspend fun insertPayPalToFirebase(payPal: PayPalEntity): Boolean {
        return try {
            val payPalRef = database.child("PayPals").child(payPal.userId)
            payPalRef.setValue(payPal).await()
            true
        } catch (e: Exception) {
            Log.e("PayPalRepository", "Error inserting PayPal: ${e.message}")
            false
        }
    }

    // Retrieve PayPal by User ID
    fun retrievePayPalByUserId(userId: String): Flow<PayPalWithUser> = flow {
        val localPayPal = payPalDao.getPayPalWithUser(userId).firstOrNull()
        if (localPayPal != null) {
            emit(localPayPal)
        } else {
            val firebasePayPal = retrievePayPalFromFirebase(userId)
            if (firebasePayPal != null) {
                payPalDao.insertPayPal(firebasePayPal)
                emit(payPalDao.getPayPalWithUser(userId).firstOrNull() ?: PayPalWithUser(PayPalEntity(), UserEntity()))
            } else {
                emit(PayPalWithUser(PayPalEntity(), UserEntity()))
            }
        }
    }.catch { e ->
        Log.e("PayPalRepository", "Error retrieving PayPal: ${e.message}")
        emit(PayPalWithUser(PayPalEntity(), UserEntity()))
    }

    private suspend fun retrievePayPalFromFirebase(userId: String): PayPalEntity? {
        return try {
            val payPalRef = database.child("PayPals").child(userId)
            val dataSnapshot = payPalRef.get().await()
            dataSnapshot.getValue(PayPalEntity::class.java)
        } catch (e: Exception) {
            Log.e("PayPalRepository", "Error retrieving PayPal from Firebase: ${e.message}")
            null
        }
    }

    // Delete PayPal
    fun deletePayPal(userId: String): Flow<Boolean> = flow {
        payPalDao.deletePayPal(userId)
        emit(deletePayPalFromFirebase(userId))
    }

    private suspend fun deletePayPalFromFirebase(userId: String): Boolean {
        return try {
            val payPalRef = database.child("PayPals").child(userId)
            payPalRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("PayPalRepository", "Error deleting PayPal: ${e.message}")
            false
        }
    }

    // Real-time Firebase Sync
    private fun startFirebaseSync() {
        val payPalRef = database.child("PayPals")

        payPalRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val payPal = dataSnapshot.getValue(PayPalEntity::class.java)
                    if (payPal != null) {
                        // Insert/Update local database
                        scope.launch{
                            payPalDao.insertPayPal(payPal)
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PayPalRepository", "Firebase sync cancelled: ${error.message}")
            }
        })
    }
}
