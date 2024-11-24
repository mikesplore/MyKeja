package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.mike.hms.model.userModel.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.text.get

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

    fun retrieveCreditCardByUserId(userId: String): Flow<CreditCardWithUser> = flow {
        val localCreditCard = creditCardDao.getCreditCardWithUser(userId).firstOrNull()
        if (localCreditCard != null) {
            emit(localCreditCard)
        } else {
            val firebaseCreditCard = retrieveCreditCardFromFirebase(userId)
            if (firebaseCreditCard != null) {
                creditCardDao.insertCreditCard(firebaseCreditCard)
                emit(creditCardDao.getCreditCardWithUser(userId).first())
            } else {
                emit(CreditCardWithUser(CreditCardEntity(), UserEntity()))
            }
        }
    }.catch { e ->
        Log.e("CreditCardRepository", "Error retrieving credit card: ${e.message}")
        emit(CreditCardWithUser(CreditCardEntity(), UserEntity()))
    }

    private suspend fun retrieveCreditCardFromFirebase(userId: String): CreditCardEntity? {
        return try {
            val creditCardRef = database.child("CreditCards").child(userId)
            val dataSnapshot = creditCardRef.get().await()
            dataSnapshot.getValue(CreditCardEntity::class.java)
        } catch (e: Exception) {
            Log.e("CreditCardRepository", "Error retrieving credit card from Firebase: ${e.message}")
            null
        }
    }

    fun updateCreditCard(creditCard: CreditCardEntity): Flow<Boolean> = flow {
        creditCardDao.insertCreditCard(creditCard)
        emit(updateCreditCardInFirebase(creditCard))
    }

    private suspend fun updateCreditCardInFirebase(creditCard: CreditCardEntity): Boolean {
        return try {
            val creditCardRef = database.child("CreditCards").child(creditCard.userId)
            creditCardRef.setValue(creditCard).await()
            true
        } catch (e: Exception) {
            Log.e("CreditCardRepository", "Error updating credit card in Firebase: ${e.message}")
            false
        }
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