package com.mike.hms.model.houseModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HouseViewModel(private val houseRepository: HouseRepository) : ViewModel() {

    private val _houses = MutableLiveData<List<HouseEntity>>()
    val houses: LiveData<List<HouseEntity>> = _houses

    private val _housesLoading = MutableLiveData(true)
    val housesLoading: LiveData<Boolean> = _housesLoading

    private val _house = MutableLiveData<HouseEntity>()
    val house: LiveData<HouseEntity> = _house

    fun insertHouse(house: HouseEntity, onSuccess: (Boolean) -> Unit) {
        houseRepository.insertHouse(house) {
            onSuccess(it)
        }
    }

    fun getHouseByID(houseID: String) {
        houseRepository.getHouseByID(houseID) {
            _house.postValue(it)
        }
    }

    fun getAllHouses() {
        houseRepository.getAllHouses {
            _housesLoading.postValue(true)
            _houses.postValue(it)
            Log.d("HouseViewModel", "Fetched ${houses.value?.size} houses")
            _housesLoading.postValue(false)

        }
    }

    fun deleteHouse(houseID: String, onSuccess: (Boolean) -> Unit) {
        houseRepository.deleteHouse(houseID) {
            onSuccess(it)
        }
    }

    class HouseViewModelFactory(private val repository: HouseRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseViewModel::class.java)) {
                return HouseViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class for HouseViewModel")
        }
    }
}