package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hestabit.sparkmatch.firebase.AuthRepository
import com.hestabit.sparkmatch.firebase.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserFlow.collect { user ->
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user.uid)
                } else {
                    if (_authState.value !is AuthState.UserExists &&
                        _authState.value !is AuthState.NewUser &&
                        _authState.value !is AuthState.PasswordResetSent) {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
            }
        }
    }

    fun checkIfEmailExists(email: String) {
        // Validate email
        if (email.isBlank()) {
            _authState.value = AuthState.Error("Email cannot be empty")
            return
        }

        // Basic email format validation
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailRegex.toRegex())) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.checkIfUserExists(email)
            result.fold(
                onSuccess = { exists ->
                    if (exists) {
                        _authState.value = AuthState.UserExists(email)
                    } else {
                        _authState.value = AuthState.NewUser(email)
                    }
                },
                onFailure = { e ->
                    _authState.value = AuthState.Error(e.message ?: "Error checking email")
                }
            )
        }
    }

    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithEmailPassword(email, password)
            result.fold(
                onSuccess = { user ->
                    _authState.value = AuthState.Authenticated(user.uid)
                },
                onFailure = { e ->
                    _authState.value = AuthState.Error(e.message ?: "Sign in failed")
                }
            )
        }
    }

    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signUpWithEmailPassword(email, password)
            result.fold(
                onSuccess = { user ->
                    // Send verification email for new users
                    authRepository.sendEmailVerification()
                    _authState.value = AuthState.Authenticated(user.uid)
                },
                onFailure = { e ->
                    _authState.value = AuthState.Error(e.message ?: "Sign up failed")
                }
            )
        }
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            result.fold(
                onSuccess = {
                    _authState.value = AuthState.PasswordResetSent
                },
                onFailure = { e ->
                    _authState.value = AuthState.Error(e.message ?: "Password reset failed")
                }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result.isSuccess) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
}