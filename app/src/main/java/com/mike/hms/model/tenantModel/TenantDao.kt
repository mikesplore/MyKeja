package com.mike.hms.model.tenantModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TenantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: TenantEntity)

    @Query("SELECT * FROM tenantTable WHERE tenantID = :tenantID")
    suspend fun getTenantByID(tenantID: String): TenantEntity?

    @Query("SELECT * FROM tenantTable")
    suspend fun getAllTenants(): List<TenantEntity>

    @Query("DELETE FROM tenantTable WHERE tenantID = :tenantID")
    suspend fun deleteTenant(tenantID: String)

}