package com.mike.hms.model.userModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userID = :userID")
    suspend fun getUserByID(userID: String): UserEntity

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM users WHERE userID = :userID")
    suspend fun deleteUser(userID: String)

}