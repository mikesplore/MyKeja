package com.mike.hms.model.houseModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(house: HouseEntity)

    @Query("SELECT * FROM HouseTable WHERE houseID = :houseID")
    suspend fun getHouseByID(houseID: String): HouseEntity?

    @Query("SELECT * FROM HouseTable")
    suspend fun getAllHouses(): List<HouseEntity>

    @Query("DELETE FROM HouseTable WHERE houseID = :houseID")
    suspend fun deleteHouse(houseID: String)


}