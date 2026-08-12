package com.example.kashifapp.auth.presentation.login

sealed interface LoginEvent {
    object NavigateToHome: LoginEvent
}