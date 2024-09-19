package com.mike.hms.model.houseModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseRepository(private val houseDao: HouseDao) {
    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertHouse(house: HouseEntity) {
        viewmodelScope.launch {
            houseDao.insertHouse(house)
        }
    }

    fun getHouseByID(houseID: String) {
        viewmodelScope.launch {
            houseDao.getHouseByID(houseID)
        }
    }

    fun getAllHouses() {
        viewmodelScope.launch {
            houseDao.getAllHouses()
        }
    }

    fun deleteHouse(houseID: String) {
        viewmodelScope.launch {
            houseDao.deleteHouse(houseID)
        }
    }


}