package com.mike.hms.model.houseModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseRepository(private val houseDao: HouseDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertHouse(house: HouseEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            houseDao.insertHouse(house)
            onSuccess(true)

        }
    }

    fun getHouseByID(houseID: String, onResult: (HouseEntity) -> Unit) {
        viewmodelScope.launch {
            val house = houseDao.getHouseByID(houseID)
            onResult(house)
        }
    }

    fun getAllHouses(onResult: (List<HouseEntity>) -> Unit) {
        viewmodelScope.launch {
            val houses = houseDao.getAllHouses()
            onResult(houses)

        }
    }

    fun deleteHouse(houseID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            houseDao.deleteHouse(houseID)
            onSuccess(true)

        }
    }


}