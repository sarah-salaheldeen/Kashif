package com.example.kashifapp.auth.presentation.login

import com.example.kashifapp.core.presentation.util.UiText

enum class AuthMode { LOGIN, REGISTER }
data class LoginState(
    val authMode: AuthMode = AuthMode.LOGIN,
    //shared fields
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,

    //Register only
    val displayName: String = "",
    val confirmedPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false,

    //loading & errors
    val isLoading: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val displayNameError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val generalError: UiText? = null
)
