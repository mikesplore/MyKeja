package com.mike.hms.model.bedModel

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BedRepository(private val bedDao: BedDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference


    fun insertBed(bed: BedEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            bedDao.insertBed(bed)
            onSuccess(true)
        }
        insertBedToFirebase(bed) { success ->
            if (success) {
                onSuccess(true)
            }
            else{
                onSuccess(false)
            }
        }
    }

    fun getBedByID(bedID: String, onSuccess: (BedEntity) -> Unit) {
        viewmodelScope.launch {
            val bed = bedDao.getBedByID(bedID)
            onSuccess(bed)
        }
    }

    fun getBedsByRoomID(roomID: String, onResult: (List<BedEntity>) -> Unit) {
        viewmodelScope.launch {
            val beds = bedDao.getBedsByRoomID(roomID)
            onResult(beds)
        }
    }

    fun getAllBeds(onResult: (List<BedEntity>) -> Unit) {
        viewmodelScope.launch {
            val beds = bedDao.getAllBeds()
            onResult(beds)
        }
        retrieveBedsFromFirebase { bedList ->
            viewmodelScope.launch {
                bedList.forEach { bed ->
                    bedDao.insertBed(bed)
                }
            }
            onResult(bedList)
        }

    }

    fun deleteBed(bedID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            bedDao.deleteBed(bedID)
        }
        deleteBedFromFirebase(bedID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }


    //Firebase Functions
    private fun insertBedToFirebase(bed: BedEntity, onSuccess: (Boolean) -> Unit) {
        val bedRef = database.child("Beds").child(bed.bedID)

        bedRef.setValue(bed).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

   private fun retrieveBedsFromFirebase(onSuccess: (List<BedEntity>) -> Unit) {
        val bedRef = database.child("Beds")

        bedRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val bedList = mutableListOf<BedEntity>()
                for (bedSnapshot in dataSnapshot.children) {
                    val bed = bedSnapshot.getValue(BedEntity::class.java)
                    bed?.let { bedList.add(it) }
                }
                onSuccess(bedList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

   private  fun deleteBedFromFirebase(bedID: String, onSuccess: (Boolean) -> Unit) {
        val bedRef = database.child("Beds").child(bedID)
        bedRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }
}