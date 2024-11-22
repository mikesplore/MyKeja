package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PayPalRepository(private val payPalDao: PayPalDao) {

    private val database = FirebaseDatabase.getInstance().reference

    // Credit Card Operations
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
            Log.e("PayPalRepository", "Error inserting payPal: ${e.message}")
            false
        }
    }

    fun retrievePayPalByUserId(userId: String): Flow<Flow<PayPalWithUser>> = flow {
        emit(payPalDao.getPayPalWithUser(userId))
        val payPal = retrievePayPalFromFirebase()
        payPalDao.insertPayPal(payPal)
    }.catch { e ->
        Log.e("PayPalRepository", "Error retrieving payPal: ${e.message}")
    }

    private suspend fun retrievePayPalFromFirebase(): PayPalEntity {
        val payPalRef = database.child("PayPals")
        val dataSnapshot = payPalRef.get().await()
        return dataSnapshot.getValue(PayPalEntity::class.java) ?: PayPalEntity()
    }

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
            Log.e("PayPalRepository", "Error deleting payPal: ${e.message}")
            false
        }
    }

}