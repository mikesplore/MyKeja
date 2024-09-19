package com.mike.hms.model.roomModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel(){

    fun insertRoom(room: RoomEntity) {
        roomRepository.insertRoom(room)
    }

    fun getRoomByID(roomID: String) {
        roomRepository.getRoomByID(roomID)
    }

    fun getAllRooms() {
        roomRepository.getAllRooms()
    }

    fun deleteRoom(roomID: String) {
        roomRepository.deleteRoom(roomID)
    }

    fun insertRoomAllocation(roomAllocation: RoomAllocationEntity) {
        roomRepository.insertRoomAllocation(roomAllocation)
        }

    fun getRoomAllocationByID(roomAllocationID: String) {
        roomRepository.getRoomAllocationByID(roomAllocationID)
    }

    fun getAllRoomAllocations() {
        roomRepository.getAllRoomAllocations()

    }

    fun deleteRoomAllocation(roomAllocationID: String) {
        roomRepository.deleteRoomAllocation(roomAllocationID)
    }

    fun insertRoomBooking(roomBooking: RoomBookingEntity) {
        roomRepository.insertRoomBooking(roomBooking)
    }

    fun getRoomBookingByID(roomBookingID: String) {
        roomRepository.getRoomBookingByID(roomBookingID)
    }

    fun getAllRoomBookings() {
        roomRepository.getAllRoomBookings()
    }

    fun deleteRoomBooking(roomBookingID: String) {
        roomRepository.deleteRoomBooking(roomBookingID)

    }

    class RoomViewModelFactory(private val repository: RoomRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                return RoomViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class for RoomViewModel")
        }
    }

}