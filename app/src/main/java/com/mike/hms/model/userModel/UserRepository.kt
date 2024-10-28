package com.mike.hms.model.userModel

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.mike.hms.HMSPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserRepository(private val userDao: UserDao) {

    private val viewmodelScope = CoroutineScope(Dispatchers.IO)
    private val database = FirebaseDatabase.getInstance().reference

    fun insertUser(user: UserEntity, onSuccess: (Boolean) -> Unit) {
        // Inserting into Firebase (Runs on the main thread)
        insertUserToFirebase(user) { success ->
            CoroutineScope(Dispatchers.Main).launch {
                onSuccess(success)
            }
        }

        // Inserting into Room (Runs in the background)
        viewmodelScope.launch {
            userDao.insertUser(user)
            withContext(Dispatchers.Main) {
                onSuccess(true)
            }
        }
    }

    fun getUserByID(userID: String, onResult: (UserEntity) -> Unit) {
        viewmodelScope.launch {
            val user = userDao.getUserByID(userID)
            if (user != null) {
                onResult(user)
                Log.d("UserRepository", "$user with userID ${HMSPreferences.userId.value}")
            }
        }
    }

    fun getAllUsers(onResult: (List<UserEntity>) -> Unit) {
        viewmodelScope.launch {
            val users = userDao.getAllUsers()
            onResult(users)
        }
        retrieveUsersFromFirebase { userList ->
            viewmodelScope.launch {
                userList.forEach { user ->
                    userDao.insertUser(user)
                }
                onResult(userList)
            }
        }
    }

    fun deleteUser(userID: String, onSuccess: (Boolean) -> Unit) {
        // Deleting from Room (Runs in the background)
        viewmodelScope.launch {
            userDao.deleteUser(userID)
            withContext(Dispatchers.Main) {
                onSuccess(true)
            }
        }

        // Deleting from Firebase (Runs on the main thread)
        deleteUserFromFirebase(userID) { success ->
            CoroutineScope(Dispatchers.Main).launch {
                onSuccess(success)
            }
        }
    }

    //Firebase Functions
    private fun insertUserToFirebase(user: UserEntity, onSuccess: (Boolean) -> Unit) {
        val userRef = database.child("Users").child(user.userID)

        userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun retrieveUsersFromFirebase(onSuccess: (List<UserEntity>) -> Unit) {
        val userRef = database.child("Users")

        userRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val userList = mutableListOf<UserEntity>()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(UserEntity::class.java)
                    user?.let { userList.add(it) }
                }
                onSuccess(userList)
            } else {
                onSuccess(emptyList())
            }
        }
    }

    private fun deleteUserFromFirebase(userID: String, onSuccess: (Boolean) -> Unit) {
        val userRef = database.child("Users").child(userID)
        userRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    //Credit Card Functions
    fun insertCreditCard(creditCard: CreditCardEntity, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            userDao.insertCreditCard(creditCard)
            onSuccess(true)
        }
        insertCreditCardToFirebase(creditCard) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    private fun insertCreditCardToFirebase(creditCard: CreditCardEntity, onSuccess: (Boolean) -> Unit) {
        val creditCardRef = database.child("CreditCards").child(creditCard.userId)

        creditCardRef.setValue(creditCard).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }
    }

    fun retrieveCreditCardByUserId(userId: String, onResult: (CreditCardWithUser) -> Unit) {
        viewmodelScope.launch {
            val creditCard = userDao.getCreditCardWithUser(userId)
            onResult(creditCard)
        }
        retrieveCreditCardFromFirebase { creditCard ->
            viewmodelScope.launch {
                userDao.insertCreditCard(creditCard)
            }
        }
    }

    private fun retrieveCreditCardFromFirebase(onSuccess: (CreditCardEntity) -> Unit) {
        val creditCardRef = database.child("CreditCards")
        creditCardRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val creditCard = dataSnapshot.getValue(CreditCardEntity::class.java)
                if (creditCard != null) {
                    onSuccess(creditCard)
                }
            } else {
                onSuccess(CreditCardEntity())
            }
        }
    }

    fun deleteCreditCard(userId: String, onSuccess: (Boolean) -> Unit) {
        viewmodelScope.launch {
            userDao.deleteCreditCard(userId)
        }
        deleteCreditCardFromFirebase(userId) { success ->
            if (success) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }

    }

    private fun deleteCreditCardFromFirebase(userId: String, onSuccess: (Boolean) -> Unit) {
        val creditCardRef = database.child("CreditCards").child(userId)
        creditCardRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(true)
            } else {
                onSuccess(false)
            }
        }

    }
}