package com.hestabit.sparkmatch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.hestabit.sparkmatch.data.AuthMethod
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.data.AuthUiState
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

    val isLoggedIn: Boolean = authRepository.isLoggedIn()

    init {
        loadCurrentUser()
    }

    // Centralized method to load current user
    private fun loadCurrentUser() = viewModelScope.launch {
        Log.d("AuthViewModel", "Loading current user...")
        when (val response = authRepository.getUser()) {
            is Response.Success -> {
                Log.d("AuthViewModel", "User loaded: ${response.result?.email}")
                _authUiState.update { state ->
                    state.copy(
                        currentUser = response.result,
                        authState = if (response.result != null)
                            AuthState.Authenticated
                        else
                            AuthState.Unauthenticated
                    )
                }
            }
            is Response.Failure -> {
                Log.e("AuthViewModel", "Failed to get user: ${response.exception.message}")
                _authUiState.update {
                    it.copy(
                        authState = AuthState.Error(
                            response.exception.message ?: "Failed to get user"
                        )
                    )
                }
            }
            else -> {
                Log.d("AuthViewModel", "No user found")
                _authUiState.update {
                    it.copy(authState = AuthState.Unauthenticated)
                }
            }
        }
    }

    // Login method with extensive logging
    fun login(email: String, password: String) = viewModelScope.launch {
        Log.d("AuthViewModel", "Attempting login with email: $email")
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        try {
            val response = authRepository.login(email, password)
            Log.d("AuthViewModel", "Login response: $response")

            when (response) {
                is Response.Success -> {
                    Log.d("AuthViewModel", "Login successful")
                    val userResponse = authRepository.getUser()
                    Log.d("AuthViewModel", "User fetch response: $userResponse")

                    when (userResponse) {
                        is Response.Success -> {
                            _authUiState.update {
                                it.copy(
                                    currentUser = userResponse.result,
                                    authState = AuthState.Authenticated,
                                    isNewUser = false
                                )
                            }
                            Log.d("AuthViewModel", "User authenticated: ${userResponse.result?.email}")
                        }
                        is Response.Failure -> {
                            Log.e("AuthViewModel", "Failed to get user info: ${userResponse.exception.message}")
                            _authUiState.update {
                                it.copy(
                                    authState = AuthState.Error(
                                        userResponse.exception.message ?: "Failed to get user info"
                                    )
                                )
                            }
                        }
                        else -> {
                            Log.d("AuthViewModel", "Unexpected user response")
                        }
                    }
                }
                is Response.Failure -> {
                    Log.e("AuthViewModel", "Login failed: ${response.exception.message}")
                    _authUiState.update {
                        it.copy(
                            authState = AuthState.Error(
                                response.exception.message ?: "Login failed"
                            )
                        )
                    }
                }
                else -> {
                    Log.d("AuthViewModel", "Unexpected login response")
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Login exception", e)
            _authUiState.update {
                it.copy(
                    authState = AuthState.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }

    // Signup method with extensive logging
    fun signUp(email: String, password: String) = viewModelScope.launch {
        Log.d("AuthViewModel", "Attempting signup with email: $email")
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        try {
            val response = authRepository.signUp(email, password)
            Log.d("AuthViewModel", "Signup response: $response")

            when (response) {
                is Response.Success -> {
                    Log.d("AuthViewModel", "Signup successful")
                    // Refresh user and update state
                    val userResponse = authRepository.getUser()
                    Log.d("AuthViewModel", "User fetch response: $userResponse")

                    when (userResponse) {
                        is Response.Success -> {
                            _authUiState.update {
                                it.copy(
                                    currentUser = userResponse.result,
                                    authState = AuthState.Authenticated,
                                    isNewUser = true
                                )
                            }
                            Log.d("AuthViewModel", "User created: ${userResponse.result?.email}")
                        }
                        is Response.Failure -> {
                            Log.e("AuthViewModel", "Failed to get user info: ${userResponse.exception.message}")
                            _authUiState.update {
                                it.copy(
                                    authState = AuthState.Error(
                                        userResponse.exception.message ?: "Failed to get user info"
                                    )
                                )
                            }
                        }
                        else -> {
                            Log.d("AuthViewModel", "Unexpected user response")
                        }
                    }
                }
                is Response.Failure -> {
                    Log.e("AuthViewModel", "Signup failed: ${response.exception.message}")
                    _authUiState.update {
                        it.copy(
                            authState = AuthState.Error(
                                response.exception.message ?: "Signup failed"
                            )
                        )
                    }
                }
                else -> {
                    Log.d("AuthViewModel", "Unexpected signup response")
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Signup exception", e)
            _authUiState.update {
                it.copy(
                    authState = AuthState.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }

    // Simplified method to handle authentication operations
    private fun handleAuthOperation(
        operation: suspend () -> Response<Boolean>,
        onSuccess: (FirebaseUser?) -> Unit = {}
    ) = viewModelScope.launch {
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        try {
            val response = operation()
            when (response) {
                is Response.Success -> {
                    // Refresh user and update state
                    when (val userResponse = authRepository.getUser()) {
                        is Response.Success -> {
                            onSuccess(userResponse.result)
                            _authUiState.update {
                                it.copy(
                                    currentUser = userResponse.result,
                                    authState = AuthState.Authenticated,
                                    isNewUser = userResponse.result != null
                                )
                            }
                        }
                        is Response.Failure -> {
                            _authUiState.update {
                                it.copy(
                                    authState = AuthState.Error(
                                        userResponse.exception.message ?: "Failed to get user info"
                                    )
                                )
                            }
                        }
                        else -> {}
                    }
                }
                is Response.Failure -> {
                    _authUiState.update {
                        it.copy(
                            authState = AuthState.Error(
                                response.exception.message ?: "Authentication failed"
                            )
                        )
                    }
                }
                else -> {}
            }
        } catch (e: Exception) {
            _authUiState.update {
                it.copy(
                    authState = AuthState.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }

    // Sign out method
    fun signOut() = handleAuthOperation(
        { authRepository.signOut() },
        onSuccess = {
            _authUiState.update {
                it.copy(
                    currentUser = null,
                    authState = AuthState.Unauthenticated,
                    isNewUser = false,
                    authMethod = AuthMethod.NONE
                )
            }
        }
    )

    // Reset password method
    fun resetPassword(email: String) = viewModelScope.launch {
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        authRepository.resetPassword(
            email = email,
            onSuccess = {
                _authUiState.update {
                    it.copy(
                        authState = AuthState.Unauthenticated
                    )
                }
            },
            onFailure = { errorMessage ->
                _authUiState.update {
                    it.copy(
                        authState = AuthState.Error(errorMessage)
                    )
                }
            }
        )
    }

    // Phone verification method
    fun verifyPhoneNumber(phoneNumber: String) = viewModelScope.launch {
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                viewModelScope.launch {
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                loadCurrentUser()
                            } else {
                                _authUiState.update {
                                    it.copy(
                                        authState = AuthState.Error(
                                            task.exception?.message ?: "Verification failed"
                                        )
                                    )
                                }
                            }
                        }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _authUiState.update {
                    it.copy(
                        authState = AuthState.Error(
                            e.message ?: "Phone verification failed"
                        )
                    )
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                authRepository.verificationId = verificationId
            }
        }

        try {
            authRepository.verifyPhoneNumber(phoneNumber, callbacks)
        } catch (e: Exception) {
            _authUiState.update {
                it.copy(
                    authState = AuthState.Error(
                        e.message ?: "Failed to initiate phone verification"
                    )
                )
            }
        }
    }

    // Verify code method
    fun verifyCode(
        code: String,
        onComplete: (Boolean, String?) -> Unit = { _, _ -> }
    ) = viewModelScope.launch {
        _authUiState.update { it.copy(authState = AuthState.Loading) }

        try {
            authRepository.verifyCode(code) { isSuccessful, errorMessage ->
                if (isSuccessful) {
                    loadCurrentUser()
                } else {
                    _authUiState.update {
                        it.copy(
                            authState = AuthState.Error(
                                errorMessage ?: "Code verification failed"
                            )
                        )
                    }
                }
                onComplete(isSuccessful, errorMessage)
            }
        } catch (e: Exception) {
            _authUiState.update {
                it.copy(
                    authState = AuthState.Error(
                        e.message ?: "An error occurred during verification"
                    )
                )
            }
            onComplete(false, e.message)
        }
    }

    // Setter methods for UI state
    fun setNewUserState(isNew: Boolean) {
        _authUiState.update { it.copy(isNewUser = isNew) }
    }

    fun setAuthMethod(method: AuthMethod) {
        _authUiState.update { it.copy(authMethod = method) }
    }
}