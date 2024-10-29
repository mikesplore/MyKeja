package com.mike.hms.model.creditCardModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditCard(creditCard: CreditCardEntity)

    @Transaction
    @Query("SELECT * FROM credit_cards WHERE userId = :userId")
    fun getCreditCardWithUser(userId: String): Flow<CreditCardWithUser>

    @Query("DELETE FROM credit_cards WHERE cardId = :cardId")
    suspend fun deleteCreditCard(cardId: String)
}