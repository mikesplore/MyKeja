package com.mike.hms.model.statements

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatementDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatement(statement: StatementEntity)

    @Query("SELECT * FROM statement WHERE userId = :userId")
    fun getStatementsByUserId(userId: String): Flow<List<StatementEntity>>
}
