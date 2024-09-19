package com.mike.hms.model.roomModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomRepository(private val roomDao: RoomDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertRoom(room: RoomEntity) {
        viewmodelScope.launch {
            roomDao.insertRoom(room)
        }
    }

    fun getRoomByID(roomID: String) {
        viewmodelScope.launch {
            roomDao.getRoomByID(roomID)
        }
    }

    fun getAllRooms() {
        viewmodelScope.launch {
            roomDao.getAllRooms()
        }
    }

    fun deleteRoom(roomID: String) {
        viewmodelScope.launch {
            roomDao.deleteRoom(roomID)
        }
    }


}