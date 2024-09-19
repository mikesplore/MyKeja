package com.mike.hms.model.roomModel

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Query("SELECT * FROM RoomTable WHERE roomID = :roomID")
    suspend fun getRoomByID(roomID: String): RoomEntity?

    @Query("SELECT * FROM RoomTable")
    suspend fun getAllRooms(): List<RoomEntity>

    @Query("DELETE FROM RoomTable WHERE roomID = :roomID")
    suspend fun deleteRoom(roomID: String)

}