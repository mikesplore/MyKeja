package com.mike.hms.model.favorites

import com.mike.hms.model.houseModel.HouseEntity
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val favoriteDao: FavoriteDao
) {
    suspend fun insertFavorite(favorite: FavouriteEntity) {
        favoriteDao.insertFavorite(favorite)
    }

    fun getFavoriteHouses(): Flow<List<HouseEntity>> {
        return favoriteDao.getFavoriteHouses()
    }

    suspend fun deleteFavorite(houseID: String) {
        favoriteDao.deleteFavorite(houseID)
    }

    suspend fun deleteAllFavorites() {
        favoriteDao.deleteAllFavorites()
    }
}
