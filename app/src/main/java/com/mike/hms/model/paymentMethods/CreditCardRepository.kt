package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.*
import com.mike.hms.model.userModel.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Repository for managing credit card payments.
 * @param creditCardDao The DAO for accessing credit card data locally via Room.
 */
class CreditCardRepository(private val creditCardDao: CreditCardDao) {

    private val database = FirebaseDatabase.getInstance().reference
    val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Start syncing data from Firebase to Room
        startFirebaseSync()
    }

    /**
     * Inserts a new credit card payment record into the Room database and Firebase.
     * @param creditCard The CreditCardEntity object to insert.
     */
    // Insert Credit Card
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

    // Retrieve Credit Card by User ID
    fun retrieveCreditCardByUserId(userId: String): Flow<CreditCardWithUser> = flow {
        val localCreditCard = creditCardDao.getCreditCardWithUser(userId).firstOrNull()
        if (localCreditCard != null) {
            emit(localCreditCard)
        } else {
            val firebaseCreditCard = retrieveCreditCardFromFirebase(userId)
            if (firebaseCreditCard != null) {
                creditCardDao.insertCreditCard(firebaseCreditCard)
                val updatedCreditCard = creditCardDao.getCreditCardWithUser(userId).firstOrNull()
                if (updatedCreditCard != null) {
                    emit(updatedCreditCard)
                } else {
                    emit(CreditCardWithUser(CreditCardEntity(), UserEntity()))
                }
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

    // Delete Credit Card
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

    // Real-time Firebase Sync
    private fun startFirebaseSync() {
        val creditCardsRef = database.child("CreditCards")

        creditCardsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val creditCard = dataSnapshot.getValue(CreditCardEntity::class.java)
                    if (creditCard != null) {
                        // Insert/Update local database
                        scope.launch{
                            creditCardDao.insertCreditCard(creditCard)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CreditCardRepository", "Firebase sync cancelled: ${error.message}")
            }
        })
    }

}
