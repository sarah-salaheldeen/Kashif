package com.example.kashifapp.core.presentation.util

import com.example.kashifapp.R
import com.example.kashifapp.core.domain.util.DataError

fun DataError.toUiText(): UiText {
    val stringResource = when (this) {
        DataError.Local.DISK_FULL -> R.string.error_disk_full
        DataError.Local.UNKNOWN -> R.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> R.string.error_no_internet
        DataError.Remote.SERVER -> R.string.error_unknown
        DataError.Remote.SERIALIZATION -> R.string.error_serialization
        DataError.Remote.UNKNOWN -> R.string.error_unknown
        DataError.Auth.INVALID_EMAIL -> R.string.error_invalid_email
        DataError.Auth.WRONG_PASSWORD -> R.string.error_wrong_password
        DataError.Auth.EMAIL_ALREADY_IN_USE -> R.string.error_email_already_in_use
        DataError.Auth.WEAK_PASSWORD -> R.string.error_weak_password
        DataError.Auth.USER_NOT_FOUND -> R.string.error_user_not_found
        DataError.Auth.GOOGLE_SIGN_IN_CANCELLED -> R.string.error_google_sign_in_cancelled
        DataError.Auth.UNKNOWN -> R.string.error_unknown
        DataError.Remote.BAD_REQUEST -> R.string.bad_request
        DataError.Location.PERMISSION_DENIED -> R.string.error_location_permission
        DataError.Location.UNAVAILABLE -> R.string.error_location_unavailable
        DataError.Location.DISABLED -> R.string.error_location_disabled
    }
    return UiText.StringResourceId(stringResource)
}