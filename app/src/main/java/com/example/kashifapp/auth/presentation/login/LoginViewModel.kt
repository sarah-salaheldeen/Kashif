package com.example.kashifapp.auth.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kashifapp.R
import com.example.kashifapp.auth.domain.repository.AuthRepository
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.presentation.toUiText
import com.example.kashifapp.core.presentation.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> {
                _state.update { it.copy(email = action.email, emailError = null) }
            }
            is LoginAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password, passwordError = null) }
            }
            LoginAction.OnPasswordVisibilityToggled -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            LoginAction.OnSignInClicked -> signInWithEmail()
            LoginAction.OnGoogleSignInClicked -> { /* handled in screen — needs Context */ }
            LoginAction.OnRegisterClicked -> {
                viewModelScope.launch { _events.send(LoginEvent.NavigateToRegister) }
        }
        }
    }

    private fun signInWithEmail() {
        if (!validInputs()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }
            val result = authRepository.signInWithEmail(
                email = _state.value.email.trim(),
                password = _state.value.password
            )
            _state.update { it.copy(isLoading = false) }
            when (result) {
                is Result.Success -> _events.send(LoginEvent.NavigateToHome)
                is Result.Error -> _state.update { it.copy(generalError = result.error.toUiText()) }
            }
        }
    }

    // Google sign-in needs Context for CredentialManager — handle in screen
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.signInWithGoogle(context)
            _state.update { it.copy(isLoading = false) }
            when (result) {
                is Result.Success -> _events.send(LoginEvent.NavigateToHome)
                is Result.Error -> if (result.error != DataError.Auth.GOOGLE_SIGN_IN_CANCELLED) {
                    _state.update { it.copy(generalError = result.error.toUiText()) }
                }
            }
        }
    }

    private fun validInputs(): Boolean {
        var isValid = true
        if (_state.value.email.isBlank()) {
            _state.update { it.copy(emailError = UiText.StringResourceId(R.string.error_email_required)) }
            isValid = false
        }
        if (_state.value.password.isBlank()) {
            _state.update { it.copy(passwordError = UiText.StringResourceId(R.string.error_password_required)) }
            isValid = false
        }
        return isValid
    }
}