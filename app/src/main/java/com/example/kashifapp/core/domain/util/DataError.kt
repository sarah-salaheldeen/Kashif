package com.example.kashifapp.core.domain.util

sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        BAD_REQUEST,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        UNKNOWN
    }

    enum class Auth: DataError {
        INVALID_EMAIL,
        WRONG_PASSWORD,
        EMAIL_ALREADY_IN_USE,
        WEAK_PASSWORD,
        USER_NOT_FOUND,
        GOOGLE_SIGN_IN_CANCELLED,
        UNKNOWN
    }

    enum class Location: DataError {
        PERMISSION_DENIED,
        UNAVAILABLE,
        DISABLED  // ← GPS/location services are off
    }
}