package com.example.kashifapp.auth.presentation.login

sealed interface LoginAction {
    data object OnToggleAuthMode: LoginAction
    data class OnEmailChanged(val email: String): LoginAction
    data class OnPasswordChanged(val password: String): LoginAction
    object OnPasswordVisibilityToggled: LoginAction

    data class OnDisplayNameChanged(val displayName: String): LoginAction
    data class OnConfirmedPasswordChanged(val confirmedPassword: String): LoginAction
    data object OnConfirmPasswordVisibilityToggled: LoginAction
    data object OnSubmitClicked: LoginAction
    object OnGoogleSignInClicked: LoginAction
}