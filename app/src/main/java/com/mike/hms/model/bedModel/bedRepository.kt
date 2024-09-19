package com.mike.hms.model.bedModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BedRepository(private val bedDao: BedDao){

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertBed(bed: BedEntity){
        viewmodelScope.launch {
            bedDao.insertBed(bed)
        }
    }

    fun getBedByID(bedID: String){
        viewmodelScope.launch {
            bedDao.getBedByID(bedID)
        }
    }

    fun getBedsByRoomID(roomID: String){
        viewmodelScope.launch {
            bedDao.getBedsByRoomID(roomID)
        }
    }

    fun getAllBeds(){
        viewmodelScope.launch {
            bedDao.getAllBeds()
        }
    }

    fun deleteBed(bedID: String){
        viewmodelScope.launch {
            bedDao.deleteBed(bedID)
        }
    }

}