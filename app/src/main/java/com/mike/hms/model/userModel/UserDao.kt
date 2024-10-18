package com.mike.hms.model.userModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userID = :userID")
    suspend fun getUserByID(userID: String): UserEntity?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM users WHERE userID = :userID")
    suspend fun deleteUser(userID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditCard(creditCard: CreditCard)

    @Transaction
    @Query("SELECT * FROM credit_cards WHERE userId = :userId")
    fun getCreditCardWithUser(userId: String): CreditCardWithUser

    @Query("DELETE FROM credit_cards WHERE cardId = :cardId")
    suspend fun deleteCreditCard(cardId: String)


}