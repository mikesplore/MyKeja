package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.*
import com.mike.hms.model.userModel.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MpesaRepository(private val mpesaDao: MpesaDao) {

    private val database = FirebaseDatabase.getInstance().reference
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Start syncing data from Firebase to Room
        startFirebaseSync()
    }

    // Insert Mpesa
    fun insertMpesa(mpesa: MpesaEntity): Flow<Boolean> = flow {
        mpesaDao.insertMpesa(mpesa)
        emit(insertMpesaToFirebase(mpesa))
    }

    private suspend fun insertMpesaToFirebase(mpesa: MpesaEntity): Boolean {
        return try {
            val mpesaRef = database.child("Mpesa").child(mpesa.userId)
            mpesaRef.setValue(mpesa).await()
            true
        } catch (e: Exception) {
            Log.e("MpesaRepository", "Error inserting Mpesa: ${e.message}")
            false
        }
    }

    // Retrieve Mpesa by User ID
    fun retrieveMpesaByUserId(userId: String): Flow<MpesaWithUser> = flow {
        val localMpesa = mpesaDao.getMpesaWithUser(userId).firstOrNull()
        if (localMpesa != null) {
            emit(localMpesa)
        } else {
            val firebaseMpesa = retrieveMpesaFromFirebase(userId)
            if (firebaseMpesa != null) {
                mpesaDao.insertMpesa(firebaseMpesa)
                emit(mpesaDao.getMpesaWithUser(userId).firstOrNull() ?: MpesaWithUser(MpesaEntity(), UserEntity()))
            } else {
                emit(MpesaWithUser(MpesaEntity(), UserEntity()))
            }
        }
    }.catch { e ->
        Log.e("MpesaRepository", "Error retrieving Mpesa: ${e.message}")
        emit(MpesaWithUser(MpesaEntity(), UserEntity()))
    }

    private suspend fun retrieveMpesaFromFirebase(userId: String): MpesaEntity? {
        return try {
            val mpesaRef = database.child("Mpesa").child(userId)
            val dataSnapshot = mpesaRef.get().await()
            dataSnapshot.getValue(MpesaEntity::class.java)
        } catch (e: Exception) {
            Log.e("MpesaRepository", "Error retrieving Mpesa from Firebase: ${e.message}")
            null
        }
    }

    // Delete Mpesa
    fun deleteMpesa(userId: String): Flow<Boolean> = flow {
        mpesaDao.deleteMpesa(userId)
        emit(deleteMpesaFromFirebase(userId))
    }

    private suspend fun deleteMpesaFromFirebase(userId: String): Boolean {
        return try {
            val mpesaRef = database.child("Mpesa").child(userId)
            mpesaRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("MpesaRepository", "Error deleting Mpesa: ${e.message}")
            false
        }
    }

    // Real-time Firebase Sync
    private fun startFirebaseSync() {
        val mpesaRef = database.child("Mpesa")

        mpesaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val mpesa = dataSnapshot.getValue(MpesaEntity::class.java)
                    if (mpesa != null) {
                        // Insert/Update local database
                        scope.launch {
                            mpesaDao.insertMpesa(mpesa)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MpesaRepository", "Firebase sync cancelled: ${error.message}")
            }
        })
    }
}
