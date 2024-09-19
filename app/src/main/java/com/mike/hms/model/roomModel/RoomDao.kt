package com.mike.hms.model.roomModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM RoomTable WHERE roomID = :roomID")
    suspend fun getRoomByID(roomID: String): RoomEntity

    @Query("SELECT * FROM RoomTable")
    suspend fun getAllRooms(): List<RoomEntity>

    @Query("DELETE FROM RoomTable WHERE roomID = :roomID")
    suspend fun deleteRoom(roomID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomAllocation(roomAllocation: RoomAllocationEntity)

    @Query("SELECT * FROM RoomAllocation")
    suspend fun getAllRoomAllocations(): List<RoomAllocationEntity>

    @Query("SELECT * FROM RoomAllocation WHERE roomAllocationID = :roomAllocationID")
    suspend fun getRoomAllocationByID(roomAllocationID: String): RoomAllocationEntity

    @Query(
        """
        SELECT 
            rb.roomBookingID,
            rb.roomID,
            rb.tenantID,
            rb.checkInDate,
            rb.checkOutDate,
            rb.bookingStatus,
            t.firstName,
            t.lastName
        FROM RoomBooking rb
        INNER JOIN tenantTable t ON rb.tenantID = t.tenantID
        """
    )
    suspend fun getAllRoomBookingsWithTenantName(): List<RoomBookingWithTenantName>

    @Query("DELETE FROM RoomAllocation WHERE roomAllocationID = :roomAllocationID")
    suspend fun deleteRoomAllocation(roomAllocationID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoomBooking(roomBooking: RoomBookingEntity)

    @Query("SELECT * FROM RoomBooking WHERE roomBookingID = :roomBookingID")
    suspend fun getRoomBookingByID(roomBookingID: String): RoomBookingEntity

    @Query("SELECT * FROM RoomBooking")
    suspend fun getAllRoomBookings(): List<RoomBookingEntity>

    @Query("DELETE FROM RoomBooking WHERE roomBookingID = :roomBookingID")
    suspend fun deleteRoomBooking(roomBookingID: String)

}