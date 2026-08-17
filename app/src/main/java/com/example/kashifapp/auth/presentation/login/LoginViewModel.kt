package com.example.kashifapp.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kashifapp.R
import com.example.kashifapp.auth.domain.repository.AuthRepository
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.onError
import com.example.kashifapp.core.domain.util.onSuccess
import com.example.kashifapp.core.presentation.util.toUiText
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
            is LoginAction.OnToggleAuthMode -> {
                _state.update {
                    it.copy(
                        authMode = if (it.authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN,
                        emailError = null,
                        passwordError = null,
                        displayNameError = null,
                        confirmPasswordError = null,
                        generalError = null
                    )
                }
            }
            is LoginAction.OnEmailChanged -> {
                _state.update { it.copy(email = action.email, emailError = null) }
            }
            is LoginAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password, passwordError = null) }
            }
            LoginAction.OnPasswordVisibilityToggled -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginAction.OnDisplayNameChanged -> {
                _state.update { it.copy(displayName = action.displayName, displayNameError = null) }
            }
            is LoginAction.OnConfirmedPasswordChanged -> {
                _state.update { it.copy(confirmedPassword = action.confirmedPassword, confirmPasswordError = null) }
            }
            is LoginAction.OnConfirmPasswordVisibilityToggled -> {
                _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            }
            LoginAction.OnSubmitClicked -> {
                when (state.value.authMode) {
                    AuthMode.LOGIN -> signInWithEmail()
                    AuthMode.REGISTER -> register()
                }
            }
            LoginAction.OnGoogleSignInClicked -> { signInWithGoogle() }
        }
    }

    private fun signInWithEmail() {
        if (!validateLoginInputs()) return
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

    private fun register() {
        if (!validateRegisterInputs()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }
            authRepository.registerWithEmail(
                email = _state.value.email.trim(),
                password = _state.value.password,
                displayName = _state.value.displayName.trim()
            )
                .onSuccess { _events.send(LoginEvent.NavigateToHome) }
                .onError { _state.update { s -> s.copy(generalError = it.toUiText()) } }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.signInWithGoogle()
            _state.update { it.copy(isLoading = false) }
            when (result) {
                is Result.Success -> _events.send(LoginEvent.NavigateToHome)
                is Result.Error -> if (result.error != DataError.Auth.GOOGLE_SIGN_IN_CANCELLED) {
                    _state.update { it.copy(generalError = result.error.toUiText()) }
                }
            }
        }
    }

    private fun validateLoginInputs(): Boolean {
        var valid = true
        if (_state.value.email.isBlank()) {
            _state.update { it.copy(emailError = UiText.StringResourceId(R.string.error_email_required)) }
            valid = false
        }
        if (_state.value.password.isBlank()) {
            _state.update { it.copy(passwordError = UiText.StringResourceId(R.string.error_password_required)) }
            valid = false
        }
        return valid
    }

    private fun validateRegisterInputs(): Boolean {
        var valid = true
        if (_state.value.displayName.isBlank()) {
            _state.update { it.copy(displayNameError = UiText.StringResourceId(R.string.error_name_required)) }
            valid = false
        }
        if (_state.value.email.isBlank()) {
            _state.update { it.copy(emailError = UiText.StringResourceId(R.string.error_email_required)) }
            valid = false
        }
        if (_state.value.password.length < 6) {
            _state.update { it.copy(passwordError = UiText.StringResourceId(R.string.error_password_too_short)) }
            valid = false
        }
        if (_state.value.confirmedPassword != _state.value.password) {
            _state.update { it.copy(confirmPasswordError = UiText.StringResourceId(R.string.error_passwords_dont_match)) }
            valid = false
        }
        return valid
    }
}