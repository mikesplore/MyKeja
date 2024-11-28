package com.mike.hms.model.paymentMethods

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

    @Query("DELETE FROM credit_cards WHERE userId = :userId")
    suspend fun deleteCreditCard(userId: String)
}

@Dao
interface PayPalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayPal(payPal: PayPalEntity)

    @Transaction
    @Query("SELECT * FROM paypal WHERE userId = :userId")
    fun getPayPalWithUser(userId: String): Flow<PayPalWithUser>

    @Query("DELETE FROM paypal WHERE userId = :userId")
    suspend fun deletePayPal(userId: String)

    @Query("UPDATE credit_cards SET balance = :balance WHERE userId = :userId")
    suspend fun updatePayPalBalance(userId: String, balance: String)
}

@Dao
interface MpesaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMpesa(mpesa: MpesaEntity)

    @Transaction
    @Query("SELECT * FROM mpesa WHERE userId = :userId")
    fun getMpesaWithUser(userId: String): Flow<MpesaWithUser>

    @Query("DELETE FROM mpesa WHERE userId = :userId")
    suspend fun deleteMpesa(userId: String)
}