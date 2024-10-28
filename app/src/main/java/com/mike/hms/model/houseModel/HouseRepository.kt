package com.mike.hms.model.houseModel

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class HouseRepository(private val houseDao: HouseDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertHouse(house: HouseEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            try {
                houseDao.insertHouse(house) // Insert to Room
                val firebaseResult = insertHouseToFirebase(house) // Insert to Firebase

                // Check both Room and Firebase results
                onSuccess(firebaseResult)
            } catch (e: Exception) {
                // Handle the exception
                Log.e("HouseRepository", "Error inserting house", e)
                onSuccess(false)
            }
        }
    }

    fun getHouseByID(houseID: String, onResult: (HouseEntity?) -> Unit) {
        viewmodelScope.launch {
            val house = houseDao.getHouseByID(houseID)
            onResult(house)
        }
    }

    fun getAllHouses(onResult: (List<HouseEntity>) -> Unit) {
        viewmodelScope.launch {
            // Retrieve from Room first
            val houses = houseDao.getAllHouses()
            onResult(houses)

            // Update Room with latest Firebase data
            val firebaseHouses = retrieveHousesFromFirebase()
            firebaseHouses?.forEach { houseDao.insertHouse(it) }
        }
    }

    fun deleteHouse(houseID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            val firebaseResult = deleteHouseFromFirebase(houseID) // Delete from Firebase
            onSuccess(firebaseResult)
        }
    }

    // Firebase Functions with Coroutines
    private suspend fun insertHouseToFirebase(house: HouseEntity): Boolean {
        return try {
            database.child("Houses").child(house.houseID).setValue(house).await()
            true
        } catch (e: Exception) {
            Log.e("HouseRepository", "Error inserting house to Firebase", e)
            false
        }
    }


    private suspend fun retrieveHousesFromFirebase(): List<HouseEntity>? {
        return try {
            val dataSnapshot = database.child("Houses").get().await()
            if (dataSnapshot.exists()) {
                dataSnapshot.children.mapNotNull { it.getValue(HouseEntity::class.java) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("HouseRepository", "Error retrieving houses from Firebase", e)
            null
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
}
