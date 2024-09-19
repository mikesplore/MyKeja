package com.mike.hms.model.tenantModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TenantRepository(private val tenantDao: TenantDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertTenant(tenant: TenantEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            tenantDao.insertTenant(tenant)
            onSuccess(true)
        }
    }

    fun getTenantByID(tenantID: String, onResult: (TenantEntity) -> Unit) {
        viewmodelScope.launch {
            val tenant = tenantDao.getTenantByID(tenantID)
            onResult(tenant)
        }
    }

    fun getAllTenants(onResult: (List<TenantEntity>) -> Unit) {
        viewmodelScope.launch {
            val tenants = tenantDao.getAllTenants()
            onResult(tenants)
        }
    }

    fun deleteTenant(tenantID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            tenantDao.deleteTenant(tenantID)
            onSuccess(true)

        }
    }
}