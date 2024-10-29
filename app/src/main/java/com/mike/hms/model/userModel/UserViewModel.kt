package com.mike.hms.model.userModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _userList = MutableStateFlow<List<UserEntity>>(emptyList())
    val userList: StateFlow<List<UserEntity>> = _userList.asStateFlow()

    private val _insertResult = MutableStateFlow<Boolean?>(null)
    val insertResult: StateFlow<Boolean?> = _insertResult.asStateFlow()

    private val _deleteResult = MutableStateFlow<Boolean?>(null)
    val deleteResult: StateFlow<Boolean?> = _deleteResult.asStateFlow()

    fun insertUser(user: UserEntity, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            userRepository.insertUser(user)
                .onEach { success ->
                    callback(success)
                    _insertResult.value = success }
                .launchIn(viewModelScope)
        }
    }

    fun getUserByID(userID: String) {
        userRepository.getUserByID(userID)
            .onEach { userEntity -> _user.value = userEntity }
            .launchIn(viewModelScope)
    }

    fun getAllUsers() {
        userRepository.getAllUsers()
            .onEach { userEntities -> _userList.value = userEntities }
            .launchIn(viewModelScope)
    }

    fun deleteUser(userID: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            userRepository.deleteUser(userID)
                .onEach { success ->
                    callback(success)
                    _deleteResult.value = success }
                .launchIn(viewModelScope)
        }
    }
}
