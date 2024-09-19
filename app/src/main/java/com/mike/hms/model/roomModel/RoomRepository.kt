package com.mike.hms.model.roomModel

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomRepository(private val roomDao: RoomDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertRoom(room: RoomEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoom(room)
            onSuccess(true)
        }

        insertRoomToFirebase(room) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
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
        retrieveRoomsFromFirebase { roomList ->
            viewmodelScope.launch {
                roomList.forEach { room ->
                    roomDao.insertRoom(room)
                }
                onResult(roomList)
            }
        }
    }

    fun deleteRoom(roomID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoom(roomID)
            onSuccess(true)
        }
        deleteRoomFromFirebase(roomID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    fun insertRoomAllocation(roomAllocation: RoomAllocationEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoomAllocation(roomAllocation)
            onSuccess(true)
        }
        insertRoomAllocationToFirebase(roomAllocation) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
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
        retrieveRoomAllocationFromFirebase { roomAllocationList ->
            viewmodelScope.launch {
                roomAllocationList.forEach { roomAllocation ->
                    roomDao.insertRoomAllocation(roomAllocation)
                }
                onResult(roomAllocationList)
            }
        }
    }

    fun deleteRoomAllocation(roomAllocationID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoomAllocation(roomAllocationID)
            onSuccess(true)
        }
        deleteRoomAllocationFromFirebase(roomAllocationID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    fun insertRoomBooking(roomBooking: RoomBookingEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.insertRoomBooking(roomBooking)
            onSuccess(true)
        }
        insertRoomBookingToFirebase(roomBooking) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    fun getRoomBookingByID(roomBookingID: String, onResult: (RoomBookingEntity) -> Unit) {
        viewmodelScope.launch {
            val roomBooking = roomDao.getRoomBookingByID(roomBookingID)
            onResult(roomBooking)
        }
    }

    fun getAllRoomBookings() {
        retrieveRoomBookingFromFirebase { roomBookingList ->
            viewmodelScope.launch {
                roomBookingList.forEach { roomBooking ->
                    roomDao.insertRoomBooking(roomBooking)
                }
            }
        }
    }

    fun getAllRoomBookingsWithTenantName(onResult: (List<RoomBookingWithTenantName>) -> Unit) {
        viewmodelScope.launch {
            val roomBookings = roomDao.getAllRoomBookingsWithTenantName()
            onResult(roomBookings)
        }
    }

    fun deleteRoomBooking(roomBookingID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            roomDao.deleteRoomBooking(roomBookingID)
            onSuccess(true)
        }
        deleteRoomBookingFromFirebase(roomBookingID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Firebase Functions

    //Rooms
    private fun insertRoomToFirebase(room: RoomEntity, onSuccess: (Boolean) -> Unit) {
        val roomRef = database.child("Rooms").child(room.roomID)

        roomRef.setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveRoomsFromFirebase(onSuccess: (List<RoomEntity>) -> Unit) {
        val roomRef = database.child("Rooms")

        roomRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val roomList = mutableListOf<RoomEntity>()
                for (roomSnapshot in dataSnapshot.children) {
                    val room = roomSnapshot.getValue(RoomEntity::class.java)
                    room?.let { roomList.add(it) }
                }
                onSuccess(roomList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteRoomFromFirebase(roomID: String, onSuccess: (Boolean) -> Unit) {
        val roomRef = database.child("Rooms").child(roomID)
        roomRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Room Allocations
    private fun insertRoomAllocationToFirebase(
        room: RoomAllocationEntity,
        onSuccess: (Boolean) -> Unit
    ) {
        val roomRef = database.child("RoomAllocation").child(room.roomID)

        roomRef.setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveRoomAllocationFromFirebase(onSuccess: (List<RoomAllocationEntity>) -> Unit) {
        val roomRef = database.child("RoomAllocation")

        roomRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val roomList = mutableListOf<RoomAllocationEntity>()
                for (roomSnapshot in dataSnapshot.children) {
                    val room = roomSnapshot.getValue(RoomAllocationEntity::class.java)
                    room?.let { roomList.add(it) }
                }
                onSuccess(roomList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteRoomAllocationFromFirebase(roomID: String, onSuccess: (Boolean) -> Unit) {
        val roomRef = database.child("RoomAllocation").child(roomID)
        roomRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun insertRoomBookingToFirebase(room: RoomBookingEntity, onSuccess: (Boolean) -> Unit) {
        val roomRef = database.child("RoomBooking").child(room.roomID)

        roomRef.setValue(room).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveRoomBookingFromFirebase(onSuccess: (List<RoomBookingEntity>) -> Unit) {
        val roomRef = database.child("RoomBooking")

        roomRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val roomList = mutableListOf<RoomBookingEntity>()
                for (roomSnapshot in dataSnapshot.children) {
                    val room = roomSnapshot.getValue(RoomBookingEntity::class.java)
                    room?.let { roomList.add(it) }
                }
                onSuccess(roomList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteRoomBookingFromFirebase(roomID: String, onSuccess: (Boolean) -> Unit) {
        val roomRef = database.child("RoomBooking").child(roomID)
        roomRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }
}