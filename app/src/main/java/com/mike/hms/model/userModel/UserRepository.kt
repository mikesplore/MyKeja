package com.mike.hms.model.userModel

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserRepository(private val userDao: UserDao) {

    private val database = FirebaseDatabase.getInstance().reference
    private val userReference = database.child("Users")

    init {
        // Start listening to Firebase changes
        startFirebaseUserListener()
    }

    fun insertUser(user: UserEntity): Flow<Boolean> = flow {
        userDao.insertUser(user) // Local insertion
        val success = insertUserToFirebase(user) // Remote insertion
        emit(success)
    }

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
        .catch { e -> Log.e("UserRepository", "Error retrieving users: ${e.message}") }

    fun deleteUser(userID: String): Flow<Boolean> = flow {
        userDao.deleteUser(userID) // Local deletion
        val success = deleteUserFromFirebase(userID) // Remote deletion
        emit(success)
    }

    fun getUserByID(userID: String): Flow<UserEntity?> = flow {
        val localUser = userDao.getUserByID(userID).firstOrNull()

        if (localUser != null) {
            emit(localUser)
        } else {
            val firebaseUser = getUserFromFirebaseByID(userID)
            if (firebaseUser != null) {
                userDao.insertUser(firebaseUser)
                emit(firebaseUser)
            } else {
                emit(null)
            }
        }
    }.catch { e ->
        Log.e("UserRepository", "Error retrieving user by ID: ${e.message}")
    }

    fun getUserByEmail(email: String): Flow<UserEntity?> = flow {
        val localUser = userDao.getUserByEmail(email).firstOrNull()

        if (localUser != null) {
            emit(localUser)
        } else {
            val firebaseUser = getUserFromFirebaseByEmail(email)
            if (firebaseUser != null) {
                userDao.insertUser(firebaseUser)
                emit(firebaseUser)
            } else {
                emit(null)
            }
        }
    }.catch { e ->
        Log.e("UserRepository", "Error retrieving user by email: ${e.message}")
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

    private suspend fun getUserFromFirebaseByID(userID: String): UserEntity? {
        return try {
            val snapshot = database.child("Users").child(userID).get().await()
            snapshot.getValue(UserEntity::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user from Firebase by ID: ${e.message}")
            null
        }
    }

    private suspend fun getUserFromFirebaseByEmail(email: String): UserEntity? {
        return try {
            val snapshot = database.child("Users").orderByChild("email").equalTo(email).get().await()
            snapshot.children.firstOrNull()?.getValue(UserEntity::class.java)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user from Firebase by email: ${e.message}")
            null
        }
    }

    private fun startFirebaseUserListener() {
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val userList = mutableListOf<UserEntity>()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(UserEntity::class.java)
                        if (user != null) {
                            userList.add(user)
                        }
                    }
                    userList.forEach {user ->
                        userDao.insertUser(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("UserRepository", "Firebase listener was cancelled: ${error.message}")
            }
        })
    }
}
