package com.mike.hms.model.bedModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BedViewModel(private val bedRepository: BedRepository): ViewModel() {

    private val _beds = MutableLiveData<List<BedEntity>>()
    val beds: LiveData<List<BedEntity>> = _beds

    private val _bed = MutableLiveData<BedEntity>()
    val bed: LiveData<BedEntity> = _bed

    fun insertBed(bed: BedEntity, onSuccess: (Boolean) -> Unit){
        bedRepository.insertBed(bed) {
            onSuccess(it)
        }
    }

    fun getBedByID(bedID: String){
        bedRepository.getBedByID(bedID) {
            _bed.value = it
        }
    }

    fun getBedsByRoomID(roomID: String){
        bedRepository.getBedsByRoomID(roomID) {
            _beds.value = it
        }
    }

    fun getAllBeds(){
        bedRepository.getAllBeds {
            _beds.value = it
        }
    }

    fun deleteBed(bedID: String, onSuccess: (Boolean) -> Unit){
        bedRepository.deleteBed(bedID) {
            onSuccess(it)
        }
    }

    class BedViewModelFactory(private val repository: BedRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BedViewModel::class.java)) {
                return BedViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class for BedViewModel")
        }
    }

}
