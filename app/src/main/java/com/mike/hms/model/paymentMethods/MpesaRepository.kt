package com.mike.hms.model.paymentMethods

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MpesaRepository(private val mpesaDao: MpesaDao) {

    private val database = FirebaseDatabase.getInstance().reference

    // Mpesa Operations
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

    fun retrieveMpesaByUserId(userId: String): Flow<Flow<MpesaWithUser>> = flow {
        emit(mpesaDao.getMpesaWithUser(userId))
        val mpesa = retrieveMpesaFromFirebase()
        mpesaDao.insertMpesa(mpesa)
    }.catch { e ->
        Log.e("MpesaRepository", "Error retrieving Mpesa: ${e.message}")
    }

    private suspend fun retrieveMpesaFromFirebase(): MpesaEntity {
        val mpesaRef = database.child("Mpesa")
        val dataSnapshot = mpesaRef.get().await()
        return dataSnapshot.getValue(MpesaEntity::class.java) ?: MpesaEntity()
    }

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

}