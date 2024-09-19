package com.mike.hms.model.roomModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private val _rooms = MutableLiveData <List<RoomEntity>>()
    val rooms: LiveData<List<RoomEntity>> = _rooms

    private val _room = MutableLiveData<RoomEntity>()
    val room: LiveData<RoomEntity> = _room

    private val _roomAllocations = MutableLiveData<List<RoomAllocationEntity>>()
    val roomAllocations: LiveData<List<RoomAllocationEntity>> = _roomAllocations

    private val _roomAllocation = MutableLiveData<RoomAllocationEntity>()
    val roomAllocation: LiveData<RoomAllocationEntity> = _roomAllocation

    private val _roomBookings = MutableLiveData<List<RoomBookingWithTenantName>>()
    val roomBookings: LiveData<List<RoomBookingWithTenantName>> = _roomBookings

    private val _roomBooking = MutableLiveData<RoomBookingEntity>()
    val roomBooking: LiveData<RoomBookingEntity> = _roomBooking


    fun insertRoom(room: RoomEntity, onSuccess: (Boolean) -> Unit) {
        roomRepository.insertRoom(room) {
            onSuccess(it)
        }
    }

    fun getRoomByID(roomID: String) {
        roomRepository.getRoomByID(roomID) {
            _room.value = it
        }
    }

    fun getAllRooms() {
        roomRepository.getAllRooms {
            _rooms.value = it
        }
    }

    fun deleteRoom(roomID: String, onSuccess: (Boolean) -> Unit) {
        roomRepository.deleteRoom(roomID) {
            onSuccess(it)
        }
    }

    fun insertRoomAllocation(roomAllocation: RoomAllocationEntity, onSuccess: (Boolean) -> Unit) {
        roomRepository.insertRoomAllocation(roomAllocation) {
            onSuccess(it)
        }
    }

    fun getRoomAllocationByID(roomAllocationID: String) {
        roomRepository.getRoomAllocationByID(roomAllocationID) {
            _roomAllocation.value = it
        }
    }

    fun getAllRoomAllocations() {
        roomRepository.getAllRoomAllocations {
            _roomAllocations.value = it
        }

    }

    fun deleteRoomAllocation(roomAllocationID: String, onSuccess: (Boolean) -> Unit) {
        roomRepository.deleteRoomAllocation(roomAllocationID) {
            onSuccess(it)
        }
    }

    fun insertRoomBooking(roomBooking: RoomBookingEntity, onSuccess: (Boolean) -> Unit) {
        roomRepository.insertRoomBooking(roomBooking) {
            onSuccess(it)
        }
    }

    fun getRoomBookingByID(roomBookingID: String) {
        roomRepository.getRoomBookingByID(roomBookingID) {
            _roomBooking.value = it
        }
    }

    fun getAllRoomBookingsWithTenantName() {
        roomRepository.getAllRoomBookingsWithTenantName {
            _roomBookings.value = it
        }
    }

    fun deleteRoomBooking(roomBookingID: String, onSuccess: (Boolean) -> Unit) {
        roomRepository.deleteRoomBooking(roomBookingID) {
            onSuccess(it)
            roomRepository.getAllRoomBookings()
        }
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