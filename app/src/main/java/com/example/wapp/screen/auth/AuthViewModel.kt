package com.example.wapp.screen.auth

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapp.data.User
import com.example.wapp.getCurrentTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application): AndroidViewModel(application= application){
    private val USER_COLLECTION = "users"
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser


    private val _users = MutableStateFlow<List<User>?>(null)
    val users: StateFlow<List<User>?> get() = _users


    private val _usersInfo = MutableStateFlow<User?>(null)
    val userInfo: StateFlow<User?> get() = _usersInfo



    fun logout(onSuccess: () -> Unit){
        auth.signOut()
        onSuccess()
    }

    fun getAllUsers(context: Context){
        viewModelScope.launch {
            try {
                val users = withContext(Dispatchers.IO){
                    loadAllUsers(context)
                }
                _users.value = users

            }catch (e: Exception){
                _users.value = null
            }
        }
    }



    fun updateUserByUserUid(item:User, context: Context,){
        val currentTimestamp = getCurrentTimestamp()

        val user = item.copy(
            updatedAt = currentTimestamp
        ).toMap()
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){

                    updateUser(item = item, context=context, updatedData = user)
                }
            }catch (e: Exception){
                Toast.makeText(context, "Terjadi Kesalahan Sistem ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateUser(item:User, context: Context, updatedData: Map<String, Any>){
        db.collection(USER_COLLECTION)
            .document(item.id)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Gagal memperbarui data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    suspend private fun loadAllUsers(context: Context):List<User>? {
        return try {
            val snapShot = db.collection(USER_COLLECTION).get().await()
            if (!snapShot.isEmpty) {
                snapShot.documents.map { document ->
                    val id = document.id
                    val name = document.getString("name")
                    val email = document.getString("email") ?: ""
                    val phone = document.getString("phone") ?: ""
                    val role = document.getString("role") ?: ""
                    val createdAt = document.getString("createdAt") ?: ""
                    val lastLogin = document.getString("lastLogin") ?: ""
                    val updatedAt = document.getString("updatedAt") ?: ""
                    User(
                        id = id,
                        name = name!!,
                        email = email,
                        phone = phone,
                        role = role,
                        createdAt = createdAt,
                        lastLogin = lastLogin,
                        updatedAt = updatedAt
                    )
                }
            } else {
                Toast.makeText(context, "Tidak Ada User Ditemukan", Toast.LENGTH_SHORT).show()
                null
            }
        } catch (e: Exception) {
            null
        }
    }



    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        updateLastLogin(userId) {
                            onSuccess()
                        }
                    } else {
                        onError("User Tidak Ditemukan, Silahkan Buat Account Terlebih Dahulu")
                    }
                } else {
                    onError("Login Gagal Tolong perika Login Anda")
                }
            }
    }
    private fun updateLastLogin(userId: String, onComplete: () -> Unit) {
        val currentTimestamp = getCurrentTimestamp()
        val userRef = db.collection(USER_COLLECTION).document(userId)

        userRef.update("lastLogin", currentTimestamp)
            .addOnSuccessListener {
                onComplete()
            }
            .addOnFailureListener { e ->
                println("Error updating lastLogin: ${e.message}")
            }
    }

    fun registerUser(
        email: String,
        password: String,
        name: String,
        phone: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {

                        val currentTimestamp = getCurrentTimestamp()

                        val user = User(
                            id = userId,
                            name = name,
                            email = email,
                            phone = phone,
                            role = "user",
                            createdAt = currentTimestamp,
                            lastLogin = currentTimestamp,
                            updatedAt = currentTimestamp
                        )

                        val userData = user.toMap()

                        db.collection(USER_COLLECTION).document(userId).set(userData)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { e ->

                                val errorMessage = e.message ?: "Error saving user data"
                                onError(errorMessage)
                            }
                    } else {
                        onError("Failed to get user ID.")
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Registration failed"
                    onError(errorMessage)
                }
            }
    }


    fun User.toMap(): Map<String, Any> {
        return mapOf(
            "name" to this.name,
            "email" to this.email,
            "phone" to this.phone,
            "role" to this.role,
            "createdAt" to this.createdAt,
            "lastLogin" to this.lastLogin,
            "updatedAt" to this.updatedAt
        )
    }

    fun fetchUser(onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection(USER_COLLECTION).document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("role") ?: "user"
                        val email = document.getString("email") ?: ""
                        val lastLogin = document.getString("lastLogin") ?: ""
                        val createdAt = document.getString("createdAt") ?: ""
                        val name = document.getString("name") ?: ""
                        val phone = document.getString("phone") ?: ""
                        val updatedAt = document.getString("updatedAt") ?: ""
                        val user = User(
                            id = userId,
                            name = name,
                            email = email,
                            phone = phone,
                            role = role,
                            createdAt = createdAt,
                            lastLogin = lastLogin,
                            updatedAt = updatedAt
                        )
                        onSuccess(user)
                    } else {
                        onError("User data not found")
                    }
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Error fetching user role")
                }
        } else {
            onError("User ID is null")
        }
    }




    fun getUser() {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
                    getUserInfo()
                }
                _usersInfo.value = userInfo
            } catch (e: Exception) {
                _usersInfo.value = null
            }
        }
    }

    suspend fun getUserInfo(): User? {
        return try {
            val userId = auth.currentUser?.uid

            if (userId != null) {
                val documentSnapshot = db.collection(USER_COLLECTION).document(userId).get().await()

                if (documentSnapshot.exists()) {
                    val id = documentSnapshot.id
                    val name = documentSnapshot.getString("name") ?: ""
                    val email = documentSnapshot.getString("email") ?: ""
                    val role = documentSnapshot.getString("role") ?: ""
                    val phone = documentSnapshot.getString("phone") ?: ""
                    val createdAt = documentSnapshot.getString("createdAt") ?: ""
                    val lastLogin = documentSnapshot.getString("lastLogin") ?: ""
                    val updatedAt = documentSnapshot.getString("updatedAt") ?:""

                    User(id = id, name =  name, email= email,role= role, phone =  phone, createdAt =  createdAt, lastLogin =  lastLogin, updatedAt = updatedAt)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


}