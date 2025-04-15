package com.hestabit.sparkmatch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.hestabit.sparkmatch.data.AuthMethod
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser.asStateFlow()

    private val _authMethod = MutableStateFlow<AuthMethod>(AuthMethod.NONE)
    val authMethod: StateFlow<AuthMethod> = _authMethod.asStateFlow()

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    // Add a separate state flow for API responses
    private val _authResponse = MutableStateFlow<Response<Boolean>>(Response.InitialValue)
    val authResponse: StateFlow<Response<Boolean>> = _authResponse

    val isLoggedIn: Boolean = authRepository.isLoggedIn()

    // Method to access the authRepository for activity setting
    fun getAuthRepository(): AuthRepository {
        return authRepository
    }

    init {
        // Load current user on initialization
        viewModelScope.launch {
            when (val response = authRepository.getUser()) {
                is Response.Success -> {
                    _currentUser.value = response.result
                    if (response.result != null) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
                is Response.Failure -> {
                    Log.e("AuthViewModel", "Failed to get user: ${response.exception.message}")
                    _authState.value = AuthState.Error(response.exception.message ?: "Failed to get user")
                }
                else -> {
                    // Handle other response types
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun setNewUserState(isNew: Boolean) {
        _isNewUser.value = isNew
    }

    fun setAuthMethod(method: AuthMethod) {
        _authMethod.value = method
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
        _currentUser.value = null
        _isNewUser.value = false
        _authMethod.value = AuthMethod.NONE
        _authResponse.value = Response.InitialValue
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            _authState.value = AuthState.Loading
            _authResponse.value = Response.Loading

            val response = authRepository.login(email, password)
            _authResponse.value = response

            when (response) {
                is Response.Success -> {
                    // Update current user after successful login
                    when (val userResponse = authRepository.getUser()) {
                        is Response.Success -> {
                            _currentUser.value = userResponse.result
                            _authState.value = AuthState.Authenticated
                        }
                        is Response.Failure -> {
                            _authState.value = AuthState.Error(userResponse.exception.message ?: "Failed to get user info")
                        }
                        else -> {
                            // Handle other response types
                        }
                    }
                }
                is Response.Failure -> {
                    _authState.value = AuthState.Error(response.exception.message ?: "Login failed")
                }
                else -> {
                    // Handle other response types
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _authResponse.value = Response.Failure(e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
        }
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        try {
            _authState.value = AuthState.Loading
            _authResponse.value = Response.Loading

            val response = authRepository.signUp(email, password)
            _authResponse.value = response

            when (response) {
                is Response.Success -> {
                    // Update current user after successful signup
                    when (val userResponse = authRepository.getUser()) {
                        is Response.Success -> {
                            _currentUser.value = userResponse.result
                            _isNewUser.value = true
                            _authState.value = AuthState.Authenticated
                        }
                        is Response.Failure -> {
                            _authState.value = AuthState.Error(userResponse.exception.message ?: "Failed to get user info")
                        }
                        else -> {
                            // Handle other response types
                        }
                    }
                }
                is Response.Failure -> {
                    _authState.value = AuthState.Error(response.exception.message ?: "Signup failed")
                }
                else -> {
                    // Handle other response types
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _authResponse.value = Response.Failure(e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            _authState.value = AuthState.Loading
            _authResponse.value = Response.Loading

            val response = authRepository.signOut()
            _authResponse.value = response

            when (response) {
                is Response.Success -> {
                    _currentUser.value = null
                    resetAuthState()
                }
                is Response.Failure -> {
                    _authState.value = AuthState.Error(response.exception.message ?: "Signout failed")
                }
                else -> {
                    // Handle other response types
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _authResponse.value = Response.Failure(e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
        }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        _authResponse.value = Response.Loading

        authRepository.resetPassword(
            email = email,
            onSuccess = {
                _authState.value = AuthState.Unauthenticated
                _authResponse.value = Response.Success(true)
            },
            onFailure = { errorMessage ->
                _authState.value = AuthState.Error(errorMessage)
                _authResponse.value = Response.Failure(Exception(errorMessage))
            }
        )
    }

    fun updateProfile(displayName: String? = null, photoUri: String? = null) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        _authResponse.value = Response.Loading

        authRepository.updateProfile(
            displayName = displayName,
            photoUri = photoUri,
            onSuccess = {
                _authState.value = AuthState.Authenticated
                _authResponse.value = Response.Success(true)
                // Refresh current user data
                refreshCurrentUser()
            },
            onFailure = { errorMessage ->
                _authState.value = AuthState.Error(errorMessage)
                _authResponse.value = Response.Failure(Exception(errorMessage))
            }
        )
    }

    private fun refreshCurrentUser() = viewModelScope.launch {
        try {
            val response = authRepository.getUser()

            when (response) {
                is Response.Success -> {
                    _currentUser.value = response.result
                    if (response.result != null) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
                is Response.Failure -> {
                    Log.e("AuthViewModel", "Failed to refresh user: ${response.exception.message}")
                    _authState.value = AuthState.Error(response.exception.message ?: "Failed to refresh user")
                }
                else -> {
                    // Handle other response types
                }
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error refreshing user", e)
            _authState.value = AuthState.Error(e.message ?: "An unexpected error occurred")
        }
    }

    // Method for phone number authentication with callbacks
    fun verifyPhoneNumber(phoneNumber: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        _authResponse.value = Response.Loading

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                viewModelScope.launch {
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                viewModelScope.launch {
                                    refreshCurrentUser()
                                    _authState.value = AuthState.Authenticated
                                    _authResponse.value = Response.Success(true)
                                }
                            } else {
                                val message = task.exception?.message ?: "Verification failed"
                                _authState.value = AuthState.Error(message)
                                _authResponse.value = Response.Failure(Exception(message))
                            }
                        }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _authState.value = AuthState.Error(e.message ?: "Phone verification failed")
                _authResponse.value = Response.Failure(e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // Use the property setter instead of the conflicting method
                authRepository.verificationId = verificationId
                // We keep Loading state here as we're waiting for code verification
            }
        }

        try {
            authRepository.verifyPhoneNumber(phoneNumber, callbacks)
        } catch (e: Exception) {
            val message = e.message ?: "Failed to initiate phone verification"
            _authState.value = AuthState.Error(message)
            _authResponse.value = Response.Failure(e)
        }
    }

    // Method to store verification ID
    fun storeVerificationId(verificationId: String) {
        try {
            // Set the verification ID in the repository
            authRepository.verificationId = verificationId
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Failed to set verification ID")
            _authResponse.value = Response.Failure(e)
        }
    }

    fun verifyCode(code: String, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        _authResponse.value = Response.Loading

        try {
            authRepository.verifyCode(code) { isSuccessful, errorMessage ->
                if (isSuccessful) {
                    refreshCurrentUser()
                    _authState.value = AuthState.Authenticated
                    _authResponse.value = Response.Success(true)
                } else {
                    val message = errorMessage ?: "Code verification failed"
                    _authState.value = AuthState.Error(message)
                    _authResponse.value = Response.Failure(Exception(message))
                }
                onComplete(isSuccessful, errorMessage)
            }
        } catch (e: Exception) {
            val message = e.message ?: "An error occurred during verification"
            _authState.value = AuthState.Error(message)
            _authResponse.value = Response.Failure(e)
            onComplete(false, message)
        }
    }
}