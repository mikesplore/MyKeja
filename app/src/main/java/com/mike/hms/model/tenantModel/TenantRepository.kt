package com.mike.hms.model.tenantModel

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TenantRepository(private val tenantDao: TenantDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertTenant(tenant: TenantEntity, onSuccess: (Boolean) -> Unit) {

        insertTenantToFirebase(tenant) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
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
        retrieveTenantsFromFirebase { tenantList ->
            viewmodelScope.launch {
                tenantList.forEach { tenant ->
                    tenantDao.insertTenant(tenant)
                }
                onResult(tenantList)
            }
        }
    }

    fun deleteTenant(tenantID: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            tenantDao.deleteTenant(tenantID)
            onSuccess(true)

        }
        deleteTenantFromFirebase(tenantID) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Firebase Functions
    private fun insertTenantToFirebase(tenant: TenantEntity, onSuccess: (Boolean) -> Unit) {
        val tenantRef = database.child("Tenants").child(tenant.tenantID)

        tenantRef.setValue(tenant).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveTenantsFromFirebase(onSuccess: (List<TenantEntity>) -> Unit) {
        val tenantRef = database.child("Tenants")

        tenantRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val tenantList = mutableListOf<TenantEntity>()
                for (tenantSnapshot in dataSnapshot.children) {
                    val tenant = tenantSnapshot.getValue(TenantEntity::class.java)
                    tenant?.let { tenantList.add(it) }
                }
                onSuccess(tenantList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteTenantFromFirebase(tenantID: String, onSuccess: (Boolean) -> Unit) {
        val tenantRef = database.child("Tenants").child(tenantID)
        tenantRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }
}