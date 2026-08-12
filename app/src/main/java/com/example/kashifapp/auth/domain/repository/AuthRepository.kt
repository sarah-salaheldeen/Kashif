package com.example.kashifapp.auth.domain.repository

import com.example.kashifapp.auth.domain.model.User
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?> // emits null when logged out
    suspend fun signInWithEmail(email: String, password: String): Result<User, DataError.Auth>
    suspend fun registerWithEmail(email: String, password: String, displayName: String): Result<User, DataError.Auth>
    suspend fun signInWithGoogle(): Result<User, DataError.Auth>
    suspend fun signOut(): Result<Unit, DataError.Auth>
    fun isLoggedIn(): Boolean
}