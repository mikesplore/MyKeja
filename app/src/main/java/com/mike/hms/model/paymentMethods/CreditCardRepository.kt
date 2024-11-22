package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CreditCardRepository(private val creditCardDao: CreditCardDao) {

    private val database = FirebaseDatabase.getInstance().reference

    // Credit Card Operations
    fun insertCreditCard(creditCard: CreditCardEntity): Flow<Boolean> = flow {
        creditCardDao.insertCreditCard(creditCard)
        emit(insertCreditCardToFirebase(creditCard))
    }

    private suspend fun insertCreditCardToFirebase(creditCard: CreditCardEntity): Boolean {
        return try {
            val creditCardRef = database.child("CreditCards").child(creditCard.userId)
            creditCardRef.setValue(creditCard).await()
            true
        } catch (e: Exception) {
            Log.e("CreditCardRepository", "Error inserting credit card: ${e.message}")
            false
        }
    }

    fun retrieveCreditCardByUserId(userId: String): Flow<Flow<CreditCardWithUser>> = flow {
        emit(creditCardDao.getCreditCardWithUser(userId))
        val creditCard = retrieveCreditCardFromFirebase()
        creditCardDao.insertCreditCard(creditCard)
    }.catch { e ->
        Log.e("CreditCardRepository", "Error retrieving credit card: ${e.message}")
    }

    private suspend fun retrieveCreditCardFromFirebase(): CreditCardEntity {
        val creditCardRef = database.child("CreditCards")
        val dataSnapshot = creditCardRef.get().await()
        return dataSnapshot.getValue(CreditCardEntity::class.java) ?: CreditCardEntity()
    }

    fun deleteCreditCard(userId: String): Flow<Boolean> = flow {
        creditCardDao.deleteCreditCard(userId)
        emit(deleteCreditCardFromFirebase(userId))
    }

    private suspend fun deleteCreditCardFromFirebase(userId: String): Boolean {
        return try {
            val creditCardRef = database.child("CreditCards").child(userId)
            creditCardRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("CreditCardRepository", "Error deleting credit card: ${e.message}")
            false
        }
    }

}