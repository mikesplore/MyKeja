package com.mike.hms.model.houseModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HouseViewModel @Inject constructor(
    private val houseRepository: HouseRepository
) : ViewModel() {

    private val _house = MutableStateFlow<HouseEntity?>(null)
    val house: StateFlow<HouseEntity?> = _house.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _houses = MutableStateFlow<List<HouseEntity>>(emptyList())
    val houses: StateFlow<List<HouseEntity>> = _houses.asStateFlow()


    fun insertHouse(house: HouseEntity, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            houseRepository.insertHouse(house)
                .onEach { success ->
                    onComplete(success)
                     }
                .launchIn(viewModelScope)
        }
    }

    fun getHouseByID(houseID: String) {
        houseRepository.getHouseByID(houseID)
            .onEach { houseEntity -> _house.value = houseEntity }
            .launchIn(viewModelScope)
    }

    fun getAllHouses() {
        houseRepository.getAllHouses()
            .onEach { houseEntities -> _houses.value = houseEntities }
            .launchIn(viewModelScope)
    }

    fun deleteHouse(houseID: String) {
        viewModelScope.launch {
            houseRepository.deleteHouse(houseID)
                .onEach {
                    getAllHouses()
                }
                .launchIn(viewModelScope)
        }
    }

     fun isFavorite(houseID: String) {
         viewModelScope.launch {
             houseRepository.isFavorite(houseID)
                 .onEach { isFavorite ->
                     _isFavorite.value = isFavorite
                     Log.d("HouseViewModel", "House with ID $houseID is favorite: ${_isFavorite.value}")
                 }
                 .launchIn(viewModelScope)
         }
     }
}
