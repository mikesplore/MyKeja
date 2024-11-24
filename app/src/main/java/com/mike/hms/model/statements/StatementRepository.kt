package com.mike.hms.model.statements

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StatementRepository(private val statementDao: StatementDao) {

    private val database = FirebaseDatabase.getInstance().reference

    // Insert Statement
    fun insertStatement(statement: StatementEntity): Flow<Boolean> = flow {
        statementDao.insertStatement(statement) // Insert locally into Room
        emit(insertStatementToFirebase(statement)) // Sync with Firebase
    }

    private suspend fun insertStatementToFirebase(statement: StatementEntity): Boolean {
        return try {
            val statementRef = database.child("Statements").child(statement.statementID)
            statementRef.setValue(statement).await()
            true
        } catch (e: Exception) {
            Log.e("StatementRepository", "Error inserting statement: ${e.message}")
            false
        }
    }

    // Retrieve Statements by User ID
    fun retrieveStatementsByUserId(userId: String): Flow<Flow<List<StatementEntity>>> = flow {
        emit(statementDao.getStatementsByUserId(userId)) // Emit Room data
        val statements = retrieveStatementsFromFirebase(userId) // Fetch from Firebase
        statements.forEach { statement ->
            statementDao.insertStatement(statement) // Sync with Room
        }
    }.catch { e ->
        Log.e("StatementRepository", "Error retrieving statements: ${e.message}")
    }

    private suspend fun retrieveStatementsFromFirebase(userId: String): List<StatementEntity> {
        return try {
            val statementsRef = database.child("Statements").orderByChild("userId").equalTo(userId)
            val dataSnapshot = statementsRef.get().await()
            dataSnapshot.children.mapNotNull { it.getValue(StatementEntity::class.java) }
        } catch (e: Exception) {
            Log.e("StatementRepository", "Error fetching statements: ${e.message}")
            emptyList()
        }
    }

    // Delete All Statements for a User
    fun deleteStatementsForUser(userId: String): Flow<Boolean> = flow {
        val statementsToDelete = statementDao.getStatementsByUserId(userId)
        statementsToDelete.collect { statements ->
            statements.forEach { statement ->
                statementDao.insertStatement(statement)
                deleteStatementFromFirebase(statement.statementID)
            }
        }
        emit(true)
    }

    private suspend fun deleteStatementFromFirebase(statementID: String): Boolean {
        return try {
            val statementRef = database.child("Statements").child(statementID)
            statementRef.removeValue().await()
            true
        } catch (e: Exception) {
            Log.e("StatementRepository", "Error deleting statement: ${e.message}")
            false
        }
    }
}
