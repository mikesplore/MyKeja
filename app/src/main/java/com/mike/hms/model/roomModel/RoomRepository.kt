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

    fun insertRoomAllocation(roomAllocation: RoomAllocationEntity) {
        viewmodelScope.launch {
            roomDao.insertRoomAllocation(roomAllocation)
        }
    }

    fun getRoomAllocationByID(roomAllocationID: String) {
        viewmodelScope.launch {
            roomDao.getRoomAllocationByID(roomAllocationID)
        }
    }

    fun getAllRoomAllocations() {
        viewmodelScope.launch {
            roomDao.getAllRoomBookingsWithTenantName()
        }
    }

    fun deleteRoomAllocation(roomAllocationID: String) {
        viewmodelScope.launch {
            roomDao.deleteRoomAllocation(roomAllocationID)
        }
    }

    fun insertRoomBooking(roomBooking: RoomBookingEntity) {
        viewmodelScope.launch {
            roomDao.insertRoomBooking(roomBooking)
        }
    }

    fun getRoomBookingByID(roomBookingID: String) {
        viewmodelScope.launch {
            roomDao.getRoomBookingByID(roomBookingID)
        }
    }

    fun getAllRoomBookings() {
        viewmodelScope.launch {
            roomDao.getAllRoomBookings()
        }
    }

    fun deleteRoomBooking(roomBookingID: String) {
        viewmodelScope.launch {
            roomDao.deleteRoomBooking(roomBookingID)
        }
    }


}