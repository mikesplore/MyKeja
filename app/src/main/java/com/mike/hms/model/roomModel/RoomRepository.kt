package com.mike.hms.model.roomModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomRepository(private val roomDao: RoomDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertRoom(room: RoomEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoom(room)
            onSuccess(true)
        }
    }

    fun getRoomByID(roomID: String, onResult: (RoomEntity) -> Unit) {
        viewmodelScope.launch {
            val room = roomDao.getRoomByID(roomID)
            onResult(room)
        }
    }

    fun getAllRooms(onResult: (List<RoomEntity>) -> Unit) {
        viewmodelScope.launch {
            val rooms = roomDao.getAllRooms()
            onResult(rooms)

        }
    }

    fun deleteRoom(roomID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoom(roomID)
            onSuccess(true)
        }
    }

    fun insertRoomAllocation(roomAllocation: RoomAllocationEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoomAllocation(roomAllocation)
            onSuccess(true)
        }
    }

    fun getRoomAllocationByID(roomAllocationID: String, onResult: (RoomAllocationEntity) -> Unit) {
        viewmodelScope.launch {
            val roomAllocation = roomDao.getRoomAllocationByID(roomAllocationID)
            onResult(roomAllocation)
        }
    }

    fun getAllRoomAllocations(onResult: (List<RoomAllocationEntity>) -> Unit) {
        viewmodelScope.launch {
            val roomAllocations = roomDao.getAllRoomAllocations()
            onResult(roomAllocations)
        }
    }

    fun deleteRoomAllocation(roomAllocationID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoomAllocation(roomAllocationID)
            onSuccess(true)
        }
    }

    fun insertRoomBooking(roomBooking: RoomBookingEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoomBooking(roomBooking)
            onSuccess(true)
        }
    }

    fun getRoomBookingByID(roomBookingID: String, onResult: (RoomBookingEntity) -> Unit) {
        viewmodelScope.launch {
            val roomBooking = roomDao.getRoomBookingByID(roomBookingID)
            onResult(roomBooking)
        }
    }

    fun getAllRoomBookings(onResult: (List<RoomBookingEntity>) -> Unit) {
        viewmodelScope.launch {
            val roomBookings = roomDao.getAllRoomBookings()
            onResult(roomBookings)
        }
    }

    fun deleteRoomBooking(roomBookingID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoomBooking(roomBookingID)
            onSuccess(true)
        }
    }
}