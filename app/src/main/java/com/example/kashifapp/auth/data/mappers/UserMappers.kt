package com.example.kashifapp.auth.data.mappers

import com.example.kashifapp.auth.domain.model.User
import com.example.kashifapp.core.domain.util.DataError
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomainUser() = User(
    id = uid,
    email = email ?: "",
    displayName = displayName ?: "",
    photoUrl = photoUrl?.toString() ?: ""
)

// Maps Firebase exceptions to your domain AuthError
fun Throwable.toAuthError(): DataError.Auth = when {
    message?.contains("INVALID_EMAIL") == true      -> DataError.Auth.INVALID_EMAIL
    message?.contains("WRONG_PASSWORD") == true     -> DataError.Auth.WRONG_PASSWORD
    message?.contains("EMAIL_ALREADY_IN_USE") == true -> DataError.Auth.EMAIL_ALREADY_IN_USE
    message?.contains("WEAK_PASSWORD") == true      -> DataError.Auth.WEAK_PASSWORD
    message?.contains("USER_NOT_FOUND") == true     -> DataError.Auth.USER_NOT_FOUND
    else                                             -> DataError.Auth.UNKNOWN
}