package com.mike.hms.model.favorites

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mike.hms.model.houseModel.HouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert
    suspend fun insertFavorite(favorite: FavouriteEntity)

    @Query("SELECT * FROM HouseTable WHERE houseID IN (SELECT houseID FROM FavouriteEntity)")
    fun getFavoriteHouses(): Flow<List<HouseEntity>>

    @Query("DELETE FROM FavouriteEntity WHERE houseID = :houseID")
    suspend fun deleteFavorite(houseID: String)

    @Query("DELETE FROM FavouriteEntity")
    suspend fun deleteAllFavorites()
}