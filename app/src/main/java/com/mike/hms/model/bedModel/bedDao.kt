package com.mike.hms.model.bedModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BedDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBed(bed: BedEntity)

    @Query("SELECT * FROM BedTable WHERE roomID = :roomID")
    suspend fun getBedsByRoomID(roomID: String): List<BedEntity>

    @Query("SELECT * FROM BedTable WHERE bedID = :bedID")
    suspend fun getBedByID(bedID: String): BedEntity

    @Query("SELECT * FROM BedTable")
    suspend fun getAllBeds(): List<BedEntity>

    @Query("DELETE FROM BedTable WHERE bedID = :bedID")
    suspend fun deleteBed(bedID: String)



}