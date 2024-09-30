package com.mike.hms.model.userModel

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(private val userDao: UserDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertUser(user: UserEntity, onSuccess: (Boolean) -> Unit) {

        insertUserToFirebase(user) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
        viewmodelScope.launch {
            userDao.insertUser(user)
            onSuccess(true)
        }
    }

    fun getUserByID(userID: String, onResult: (UserEntity) -> Unit) {
        viewmodelScope.launch {
            val user = userDao.getUserByID(userID)
            onResult(user)
        }
    }

    fun getAllUsers(onResult: (List<UserEntity>) -> Unit) {
        viewmodelScope.launch {
            val users = userDao.getAllUsers()
            onResult(users)
        }
        retrieveUsersFromFirebase { userList ->
            viewmodelScope.launch {
                userList.forEach { user ->
                    userDao.insertUser(user)
                }
                onResult(userList)
            }
        }
    }

    fun deleteUser(userID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            userDao.deleteUser(userID)
            onSuccess(true)

        }
        deleteUserFromFirebase(userID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Firebase Functions
    private fun insertUserToFirebase(user: UserEntity, onSuccess: (Boolean) -> Unit) {
        val userRef = database.child("Users").child(user.userID)

        userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveUsersFromFirebase(onSuccess: (List<UserEntity>) -> Unit) {
        val userRef = database.child("Users")

        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val userList = mutableListOf<UserEntity>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserEntity::class.java)
                    user?.let { userList.add(it) }
                }
                onSuccess(userList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteUserFromFirebase(userID: String, onSuccess: (Boolean) -> Unit) {
        val userRef = database.child("Users").child(userID)
        userRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }
}