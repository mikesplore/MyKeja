package com.mike.hms.model.tenantModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TenantRepository(private val tenantDao: TenantDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)

    fun insertTenant(tenant: TenantEntity) {
        viewmodelScope.launch {
            tenantDao.insertTenant(tenant)
        }
    }

    fun getTenantByID(tenantID: String) {
        viewmodelScope.launch {
            tenantDao.getTenantByID(tenantID)
        }
    }

    fun getAllTenants() {
        viewmodelScope.launch {
            tenantDao.getAllTenants()
        }
    }

    fun deleteTenant(tenantID: String) {
        viewmodelScope.launch {
            tenantDao.deleteTenant(tenantID)
        }
    }
}