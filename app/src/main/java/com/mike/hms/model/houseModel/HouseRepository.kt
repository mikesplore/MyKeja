package com.mike.hms.model.houseModel

import android.util.Log
import androidx.compose.ui.geometry.isEmpty
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HouseRepository(private val houseDao: HouseDao) {

    private val database = FirebaseDatabase.getInstance().reference

    fun insertHouse(house: HouseEntity): Flow<Boolean> = flow {
        houseDao.insertHouse(house) // Local insertion
        emit(insertHouseToFirebase(house)) // Emit result of Firebase insertion
    }.catch { e ->
        Log.e("HouseRepository", "Error inserting house: ${e.message}")
        emit(false) // Emit false if there's an error
    }

    fun getHouseByID(houseID: String): Flow<HouseEntity?> =
        houseDao.getHouseByID(houseID)
            .catch { e ->
                Log.e("HouseRepository", "Error getting house by ID: ${e.message}")
                emit(null) // Emit null if there's an error
            }

    fun getAllHouses(): Flow<List<HouseEntity>> = flow {
        val roomHouses = houseDao.getAllHouses().first() // Get the first emission from Room Flow
        emit(roomHouses) // Emit Room data first

        if (roomHouses.isEmpty()) { // Check if Room database is empty
            Log.d("HouseRepository", "Fetching houses from Firebase since Room is empty")
            val firebaseHouses = retrieveHousesFromFirebase()
            if (firebaseHouses.isNotEmpty()) {
                firebaseHouses.forEach { houseDao.insertHouse(it) } // Update Room with Firebase data
                Log.d("HouseRepository", "Inserted ${firebaseHouses.size} houses from Firebase into Room")
            } else {
                Log.d("HouseRepository", "No houses found in Firebase")
            }
        }

        emitAll(houseDao.getAllHouses()) // Emit updated Room data
    }.catch { e ->
        Log.e("HouseRepository", "Error getting all houses: ${e.message}")
        emit(emptyList()) // Emit empty list if there's an error
    }

    fun deleteHouse(houseID: String): Flow<Boolean> = flow {
        if (deleteHouseFromFirebase(houseID)) { // Delete from Firebase
            houseDao.deleteHouse(houseID) // Delete from Room
            emit(true)
        } else {
            emit(false)
        }
    }.catch { e ->
        Log.e("HouseRepository", "Error deleting house: ${e.message}")
        emit(false) // Emit false if there's an error
    }

    // Firebase Functions
    private suspend fun insertHouseToFirebase(house: HouseEntity): Boolean {
        return try {
            database.child("Houses").child(house.houseID).setValue(house).await()
            true
        } catch (e: Exception) {
            Log.e("HouseRepository", "Error inserting house to Firebase", e)
            false
        }
    }

    private suspend fun retrieveHousesFromFirebase(): List<HouseEntity> {
        return try {
            val dataSnapshot = database.child("Houses").get().await()
            dataSnapshot.children.mapNotNull { it.getValue(HouseEntity::class.java) }
        } catch (e: Exception) {
            Log.e("HouseRepository", "Error retrieving houses from Firebase", e)
            emptyList()
        }
    }

    private suspend fun deleteHouseFromFirebase(houseID: String): Boolean {
        return try {
            database.child("Houses").child(houseID).removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("HouseRepository", "Error deleting house from Firebase", e)
            false
        }
    }

    fun isFavorite(houseID: String): Flow<Boolean> = flow {
        val isFavorite = houseDao.isFavorite(houseID)
        emit(isFavorite)
        Log.d("HouseRepository", "House with ID $houseID is favorite: $isFavorite")
        }.catch { e ->
        Log.e("HouseRepository", "Error checking if house is favorite: ${e.message}")
        emit(false)
    }
}
