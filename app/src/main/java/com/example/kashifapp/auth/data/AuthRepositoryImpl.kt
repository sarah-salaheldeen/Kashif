package com.example.kashifapp.auth.data

import com.example.kashifapp.auth.domain.model.User
import com.example.kashifapp.auth.domain.repository.AuthRepository
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val firestoreUserDataSource: FirestoreUserDataSource
) : AuthRepository {

    override val currentUser: Flow<User?> = authDataSource.currentUser

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<User, DataError.Auth> {
        return authDataSource.signInWithEmail(email, password)
    }

    override suspend fun registerWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User, DataError.Auth> {
        return authDataSource.registerWithEmail(email, password, displayName)
            .onSuccess { user ->
                // Create Firestore document after successful registration
                firestoreUserDataSource.createUserDocument(user)
            }
    }

    override suspend fun signInWithGoogle(): Result<User, DataError.Auth> {
        return authDataSource.signInWithGoogle()
            .onSuccess { user ->
                // Only create document on first Google sign-in
                if (!firestoreUserDataSource.userDocumentExists(user.id)) {
                    firestoreUserDataSource.createUserDocument(user)
                }
            }
    }

    override suspend fun signOut(): Result<Unit, DataError.Auth> {
        return authDataSource.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return authDataSource.isLoggedIn()
    }
}