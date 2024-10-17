package com.mike.hms.model.userModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _users = MutableLiveData<List<UserEntity>>()
    val users: LiveData<List<UserEntity>> = _users

    private val _creditCard = MutableLiveData<CreditCardWithUser>()
    val creditCard: LiveData<CreditCardWithUser> = _creditCard

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    fun insertUser(user: UserEntity, onSuccess: (Boolean) -> Unit) {
        userRepository.insertUser(user) {
            onSuccess(it)
        }
    }

    fun getUserByID(userID: String) {
        userRepository.getUserByID(userID) {
            _user.value = it
        }
    }

    fun getAllUsers() {
        userRepository.getAllUsers {
            _users.value = it
        }
    }

    fun deleteUser(userID: String, onSuccess: (Boolean) -> Unit) {
        userRepository.deleteUser(userID) {
            onSuccess(it)
        }
    }

    fun insertCreditCard(creditCard: CreditCard, onSuccess: (Boolean) -> Unit) {
        userRepository.insertCreditCard(creditCard) {
            onSuccess(it)
        }
    }

    fun getCreditCard(userId: String) {
        userRepository.retrieveCreditCardByUserId(userId) {
            _creditCard.postValue(it)
        }
    }

    fun deleteCreditCard(userId: String, onSuccess: (Boolean) -> Unit) {
        userRepository.deleteCreditCard(userId) {
            onSuccess(it)
        }
    }

    class UserViewModelFactory(private val repository: UserRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class for UserViewModel")
        }
    }

}