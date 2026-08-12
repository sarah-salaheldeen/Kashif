package com.example.kashifapp.auth.presentation.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.example.kashifapp.R
import com.example.kashifapp.auth.presentation.login.components.AuthTextField
import com.example.kashifapp.ui.theme.BackgroundColor
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ColorPrimaryText
import com.example.kashifapp.ui.theme.ColorSecondaryText
import com.example.kashifapp.ui.theme.DarkBrown
import com.example.kashifapp.ui.theme.KashifAppTheme

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.NavigateToHome -> onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBrown
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(
            targetState = state.authMode,
            transitionSpec = {
                fadeIn() + slideInVertically { -it } togetherWith
                        fadeOut() + slideOutVertically { it }
            },
            label = "auth_mode_title"
        ) { mode ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (mode == AuthMode.LOGIN) {
                    Text(
                        text = stringResource(R.string.sign_in_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorSecondaryText
                    )
                }else {
                    Text(
                        text = stringResource(R.string.sign_up_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ColorPrimaryText
                    )
                }
                if (mode == AuthMode.REGISTER) {
                    Text(
                        text = stringResource( R.string.join_kashif_community),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorSecondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        AnimatedContent(
            targetState = state.authMode,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally { if (targetState == AuthMode.REGISTER) it else -it } togetherWith
                        fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally { if (targetState == AuthMode.REGISTER) -it else it }
            },
            label = "auth_form"
        ) { mode ->
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (mode == AuthMode.REGISTER) {
                    AuthTextField(
                        value = state.displayName,
                        onValueChange = { onAction(LoginAction.OnDisplayNameChanged(it)) },
                        label = stringResource(R.string.display_name),
                        leadingIcon = R.drawable.ic_person,
                        errorMessage = state.displayNameError,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                }

                AuthTextField(
                    value = state.email,
                    onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
                    label = stringResource(R.string.email),
                    leadingIcon = R.drawable.ic_email,
                    errorMessage = state.emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next

                )
                AuthTextField(
                    value = state.password,
                    onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                    label = stringResource(R.string.password),
                    leadingIcon = R.drawable.ic_password,
                    errorMessage = state.passwordError,
                    isPassword = true,
                    isPasswordVisible = state.isPasswordVisible,
                    onPasswordVisibilityToggled = { onAction(LoginAction.OnPasswordVisibilityToggled) },
                    keyboardType = KeyboardType.Password,
                    imeAction = if (mode == AuthMode.LOGIN) ImeAction.Done else ImeAction.Next
                )

                if (mode == AuthMode.REGISTER) {
                    AuthTextField(
                        value = state.confirmedPassword,
                        onValueChange = { onAction(LoginAction.OnConfirmedPasswordChanged(it)) },
                        label = stringResource(R.string.confirm_password),
                        leadingIcon = R.drawable.ic_password,
                        errorMessage = state.confirmPasswordError,
                        isPassword = true,
                        isPasswordVisible = state.isConfirmPasswordVisible,
                        onPasswordVisibilityToggled = { onAction(LoginAction.OnConfirmPasswordVisibilityToggled) },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(visible = state.generalError != null) {
            state.generalError?.let { error ->
                Text(
                    text = error.asString(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAction(LoginAction.OnSubmitClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(start = 16.dp, end = 16.dp),
            colors = ButtonColors(
                containerColor = ChipBackgroundSelected,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray ,
                disabledContentColor = Color.Black
            ),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            AnimatedContent(
                targetState = state.isLoading,
                label = "submit_button_content"
            ) { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(
                            if (state.authMode == AuthMode.LOGIN) R.string.sign_in else R.string.sign_up
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onAction(LoginAction.OnGoogleSignInClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(start = 16.dp, end = 16.dp),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_google),
                tint = Color.Unspecified,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.continue_with_google),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = ColorPrimaryText
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(
                    if (state.authMode == AuthMode.LOGIN) R.string.no_account else R.string.have_account
                ),
                color = ColorSecondaryText,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(
                    if (state.authMode == AuthMode.LOGIN) R.string.register_now else R.string.login
                ),
                color = ChipBackgroundSelected,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onAction(LoginAction.OnToggleAuthMode)
                }
            )
        }
    }
}

@Preview(locale = "ar")
@Composable
fun PlacesListScreenPreview() {
    KashifAppTheme {
        LoginScreen(LoginState(), {})
    }
}