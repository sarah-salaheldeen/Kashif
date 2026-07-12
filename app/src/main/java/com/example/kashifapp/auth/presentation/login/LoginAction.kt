package com.example.kashifapp.auth.presentation.login

sealed interface LoginAction {
    data class OnEmailChanged(val email: String): LoginAction
    data class OnPasswordChanged(val password: String): LoginAction
    object OnPasswordVisibilityToggled: LoginAction
    object OnSignInClicked: LoginAction
    object OnGoogleSignInClicked: LoginAction
    object OnRegisterClicked: LoginAction
}