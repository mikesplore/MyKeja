package com.mike.hms.model.tenantModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TenantViewModel(private val tenantRepository: TenantRepository) {
    private val _tenants = MutableLiveData<List<TenantEntity>>()
    val tenants: LiveData<List<TenantEntity>> = _tenants

    private val _tenant = MutableLiveData<TenantEntity>()
    val tenant: LiveData<TenantEntity> = _tenant

    fun insertTenant(tenant: TenantEntity, onSuccess: (Boolean) -> Unit) {
        tenantRepository.insertTenant(tenant) {
            onSuccess(it)
        }
    }

    fun getTenantByID(tenantID: String) {
        tenantRepository.getTenantByID(tenantID) {
            _tenant.value = it
        }
    }

    fun getAllTenants() {
        tenantRepository.getAllTenants {
            _tenants.value = it
        }
    }

    fun deleteTenant(tenantID: String, onSuccess: (Boolean) -> Unit) {
        tenantRepository.deleteTenant(tenantID) {
            onSuccess(it)
        }
    }

}