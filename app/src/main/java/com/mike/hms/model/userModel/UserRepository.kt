package com.mike.hms.model.userModel

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepository(private val userDao: UserDao) {

    private val database = FirebaseDatabase.getInstance().reference

    suspend fun insertUser(user: UserEntity): Flow<Boolean> = flow {
        userDao.insertUser(user) // Local insertion
        val success = insertUserToFirebase(user) // Remote insertion
        emit(success)
    }

    fun getUserByID(userID: String): Flow<UserEntity?> = userDao.getUserByID(userID)
        .catch { e -> Log.e("UserRepository", "Error retrieving user by ID: ${e.message}") }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
        .catch { e -> Log.e("UserRepository", "Error retrieving users: ${e.message}") }

    suspend fun deleteUser(userID: String): Flow<Boolean> = flow {
        userDao.deleteUser(userID) // Local deletion
        val success = deleteUserFromFirebase(userID) // Remote deletion
        emit(success)
    }

    // Firebase functions
    private suspend fun insertUserToFirebase(user: UserEntity): Boolean {
        return try {
            val userRef = database.child("Users").child(user.userID)
            userRef.setValue(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error inserting user to Firebase: ${e.message}")
            false
        }
    }

    private suspend fun deleteUserFromFirebase(userID: String): Boolean {
        return try {
            val userRef = database.child("Users").child(userID)
            userRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting user from Firebase: ${e.message}")
            false
        }
    }
}
