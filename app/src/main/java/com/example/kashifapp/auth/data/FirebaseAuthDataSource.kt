package com.example.kashifapp.auth.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.kashifapp.R
import com.example.kashifapp.auth.data.mappers.toAuthError
import com.example.kashifapp.auth.data.mappers.toDomainUser
import com.example.kashifapp.auth.domain.model.User
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val firestoreUserDataSource: FirestoreUserDataSource
) {
    // Reactive stream of the current Firebase user mapped to domain User
    val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toDomainUser())
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<User, DataError.Auth> = runCatching {
        auth.signInWithEmailAndPassword(email, password).await()
            .user?.toDomainUser() ?: throw Exception("User not found")
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it.toAuthError())}
    )

    suspend fun registerWithEmail(
        email: String,
        password: String,
        displayName: String
    ): Result<User, DataError.Auth> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        result.user?.updateProfile(profileUpdates)?.await()
        val user = result.user?.toDomainUser() ?: throw Exception("User not found")
        // Create Firestore user doc on registration
        firestoreUserDataSource.createUserDocument(user)
        user
    }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it.toAuthError()) }
    )

    suspend fun signInWithGoogle(): Result<User, DataError.Auth> {
        return try {
            val credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val credentialResponse = credentialManager.getCredential(context, request)
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credentialResponse.credential.data)
            val firebaseCredential = GoogleAuthProvider
                .getCredential(googleIdTokenCredential.idToken, null)
            val result = auth.signInWithCredential(firebaseCredential).await()
            val user = result.user?.toDomainUser() ?: throw Exception("User not found")
            // Create Firestore doc if first time Google sign-in
            if (result.additionalUserInfo?.isNewUser == true) {
                firestoreUserDataSource.createUserDocument(user)
            }
            Result.Success(user)
        } catch (e: GetCredentialCancellationException) {
            Result.Error(DataError.Auth.GOOGLE_SIGN_IN_CANCELLED)
        } catch (e: Exception) {
            Result.Error(e.toAuthError())
        }
    }

    suspend fun signOut(): Result<Unit, DataError.Auth> = runCatching {
        auth.signOut()
    }.fold(
        onSuccess = { Result.Success(Unit) },
        onFailure = { Result.Error(DataError.Auth.UNKNOWN) }
    )

    fun isLoggedIn() = auth.currentUser != null
 }