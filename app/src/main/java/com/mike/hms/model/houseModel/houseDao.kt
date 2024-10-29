package com.mike.hms.model.houseModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(house: HouseEntity)

    @Query("SELECT * FROM HouseTable WHERE houseID = :houseID")
    fun getHouseByID(houseID: String): Flow<HouseEntity?>

    @Query("SELECT * FROM HouseTable")
    fun getAllHouses(): Flow<List<HouseEntity>>

    @Query("DELETE FROM HouseTable WHERE houseID = :houseID")
    suspend fun deleteHouse(houseID: String)
}