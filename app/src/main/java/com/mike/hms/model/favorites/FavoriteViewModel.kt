package com.mike.hms.model.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mike.hms.model.houseModel.HouseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<HouseEntity>>(emptyList())
    val favorites = _favorites.asStateFlow()

    fun fetchFavoriteHouses() {
        viewModelScope.launch {
            favoriteRepository.getFavoriteHouses().collect { favoriteList ->
                _favorites.value = favoriteList
            }
        }
    }

    fun addFavorite(favorite: FavouriteEntity) {
        viewModelScope.launch {
            favoriteRepository.insertFavorite(favorite)
        }
    }

    fun removeFavorite(favouriteID: String) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favouriteID)
        }
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            favoriteRepository.deleteAllFavorites()
        }
    }
}
