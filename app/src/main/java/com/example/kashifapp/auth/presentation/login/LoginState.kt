package com.example.kashifapp.auth.presentation.login

import com.example.kashifapp.core.presentation.util.UiText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: UiText? = null,
    val passwordError: UiText? = null,
    val generalError: UiText? = null
)
