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
 * Repository for managing Mpesa payment methods.
 *
 * This class handles CRUD operations for Mpesa payments by integrating
 * a local Room database with a Firebase real-time database. Data is
 * synchronized in real-time from Firebase to Room, ensuring offline
 * availability and seamless updates.
 *
 * @property mpesaDao The DAO for accessing Mpesa data locally via Room.
 */
class MpesaRepository(private val mpesaDao: MpesaDao) {

    private val database = FirebaseDatabase.getInstance().reference
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Initialize Firebase-to-Room synchronization
        startFirebaseSync()
    }

    /**
     * Inserts a new Mpesa payment record into the Room database and Firebase.
     *
     * The method first adds the MpesaEntity object to the local database,
     * then synchronizes the data to Firebase for real-time updates.
     *
     * @param mpesa The MpesaEntity object to insert.
     * @return A [Flow] emitting `true` if the operation succeeded, or `false` if an error occurred.
     */
    fun insertMpesa(mpesa: MpesaEntity): Flow<Boolean> = flow {
        mpesaDao.insertMpesa(mpesa)
        emit(insertMpesaToFirebase(mpesa))
    }

    /**
     * Inserts Mpesa data into Firebase.
     *
     * This private method is used internally to push data to the Firebase database.
     *
     * @param mpesa The MpesaEntity object containing payment details.
     * @return A Boolean indicating whether the insertion was successful.
     */
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

    /**
     * Retrieves an Mpesa payment record by user ID.
     *
     * The method checks the local Room database for the requested record. If it is not found,
     * the data is fetched from Firebase and stored locally for future access.
     *
     * @param userId The ID of the user whose Mpesa record is being retrieved.
     * @return A [Flow] emitting the retrieved [MpesaWithUser] object.
     */
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

    /**
     * Deletes an Mpesa payment record by user ID.
     *
     * The record is removed from both the local Room database and the Firebase database.
     *
     * @param userId The ID of the user whose Mpesa record is being deleted.
     * @return A [Flow] emitting `true` if the operation succeeded, or `false` if an error occurred.
     */
    fun deleteMpesa(userId: String): Flow<Boolean> = flow {
        mpesaDao.deleteMpesa(userId)
        emit(deleteMpesaFromFirebase(userId))
    }

    /**
     * Deletes Mpesa data from Firebase by user ID.
     * @param userId The ID of the user whose Mpesa data is being deleted.
     */
    private suspend fun deleteMpesaFromFirebase(userId: String): Boolean {
        return try {
            val mpesaRef = database.child("Mpesa").child(userId)
            mpesaRef.removeValue().await()
            retrieveMpesaFromFirebase(userId)
            true
        } catch (e: Exception) {
            Log.e("MpesaRepository", "Error deleting Mpesa: ${e.message}")
            false
        }
    }

    /**
     * Synchronizes Firebase data to the local Room database in real-time.
     *
     * This method listens for changes in the Firebase "Mpesa" node and updates the local database
     * accordingly. It is automatically called when the repository is instantiated.
     */
    private fun startFirebaseSync() {
        val mpesaRef = database.child("Mpesa")

        mpesaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val mpesa = dataSnapshot.getValue(MpesaEntity::class.java)
                    if (mpesa != null) {
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
