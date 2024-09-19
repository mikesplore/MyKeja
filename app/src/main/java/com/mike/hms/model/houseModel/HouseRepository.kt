package com.mike.hms.model.houseModel

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseRepository(private val houseDao: HouseDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertHouse(house: HouseEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            houseDao.insertHouse(house)
            onSuccess(true)

        }
        insertHouseToFirebase(house) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    fun getHouseByID(houseID: String, onResult: (HouseEntity) -> Unit) {
        viewmodelScope.launch {
            val house = houseDao.getHouseByID(houseID)
            onResult(house)
        }
    }

    fun getAllHouses(onResult: (List<HouseEntity>) -> Unit) {
        viewmodelScope.launch {
            val houses = houseDao.getAllHouses()
            onResult(houses)
        }
        retrieveHousesFromFirebase { houseList ->
            viewmodelScope.launch {
                houseList.forEach { house ->
                    houseDao.insertHouse(house)
                }
                onResult(houseList)
            }
        }
    }

    fun deleteHouse(houseID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            houseDao.deleteHouse(houseID)
            onSuccess(true)

        }
        deleteHouseFromFirebase(houseID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Firebase Functions
    private fun insertHouseToFirebase(house: HouseEntity, onSuccess: (Boolean) -> Unit) {
        val houseReference = database.child("Houses")
        houseReference.child(house.houseID).setValue(house)
            .addOnFailureListener {
                onSuccess(false)
            }
            .addOnSuccessListener {
                onSuccess(true)

            }
    }

    private fun retrieveHousesFromFirebase(onSuccess: (List<HouseEntity>) -> Unit) {
        val houseRef = database.child("Houses")

        houseRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val houseList = mutableListOf<HouseEntity>()
                for (houseSnapshot in dataSnapshot.children) {
                    val house = houseSnapshot.getValue(HouseEntity::class.java)
                    house?.let { houseList.add(it) }
                }
                onSuccess(houseList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteHouseFromFirebase(houseID: String, onSuccess: (Boolean) -> Unit) {
        val houseRef = database.child("Houses").child(houseID)
        houseRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }


}