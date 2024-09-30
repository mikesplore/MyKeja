package com.mike.hms.model.userModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM userTable WHERE userID = :userID")
    suspend fun getUserByID(userID: String): UserEntity

    @Query("SELECT * FROM userTable")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM userTable WHERE userID = :userID")
    suspend fun deleteUser(userID: String)

}