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
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.data.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val TAG = "AuthViewModel"

    private val auth: FirebaseAuth by lazy {
        try {
            FirebaseAuth.getInstance().also {
                Log.d(TAG, "Firebase Auth initialized successfully")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Firebase Auth", e)
            throw e
        }
    }

    // Current user flow - initialize with null
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Auth state - initialize with Unauthenticated
    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    private val _isNewUser = MutableStateFlow(false)
    val isNewUser: StateFlow<Boolean> = _isNewUser.asStateFlow()

    private val _authMethod = MutableStateFlow<AuthMethod>(
        AuthMethod.NONE
    )
    val authMethod: StateFlow<AuthMethod> = _authMethod.asStateFlow()

    // Initialize after creation
    init {
        try {
            checkAuthStatus()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking auth status", e)
            _authState.value = AuthState.Error(e.message ?: "Authentication error")
        }
    }

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    enum class AuthMethod {
        NONE,
        EMAIL,
        PHONE
    }

    fun setNewUserState(isNew: Boolean) {
        _isNewUser.value = isNew
    }

    fun setAuthMethod(method: AuthMethod) {
        _authMethod.value = method
    }

    fun checkAuthStatus() {
        try {
            val user = auth.currentUser
            _currentUser.value = user
            _authState.value = if (user != null) {
                Log.d(TAG, "User authenticated: ${user.uid}")
                AuthState.Authenticated
            } else {
                Log.d(TAG, "User not authenticated")
                AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking auth status", e)
            _authState.value = AuthState.Error(e.message ?: "Authentication error")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                Log.d(TAG, "Attempting to sign in with email: $email")
                auth.signInWithEmailAndPassword(email, password).await()
                _currentUser.value = auth.currentUser
                _authState.value = AuthState.Authenticated
                Log.d(TAG, "Sign in successful: ${auth.currentUser?.uid}")
            } catch (e: Exception) {
                Log.e(TAG, "Sign in failed", e)
                _authState.value = AuthState.Error(e.message ?: "Authentication failed")
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                Log.d(TAG, "Attempting to create user with email: $email")
                auth.createUserWithEmailAndPassword(email, password).await()
                _currentUser.value = auth.currentUser
                _authState.value = AuthState.Authenticated
                Log.d(TAG, "User creation successful: ${auth.currentUser?.uid}")
            } catch (e: Exception) {
                Log.e(TAG, "User creation failed", e)
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun signOut() {
        try {
            auth.signOut()
            _currentUser.value = null
            _authState.value = AuthState.Unauthenticated
            _isNewUser.value = false
            _authMethod.value = AuthMethod.NONE
            Log.d(TAG, "User signed out and state reset")
        } catch (e: Exception) {
            Log.e(TAG, "Error signing out", e)
            _authState.value = AuthState.Error(e.message ?: "Sign out failed")
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Password reset email sent to $email")
                        onSuccess()
                    } else {
                        Log.e(TAG, "Failed to send reset email", task.exception)
                        onFailure(task.exception?.message ?: "Failed to send reset email")
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending reset email", e)
            onFailure(e.message ?: "Error sending reset email")
        }
    }

    fun updateUserProfile(
        displayName: String? = null,
        photoUri: String? = null,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val user = auth.currentUser
            if (user == null) {
                onFailure("No authenticated user")
                return
            }

            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder().apply {
                if (displayName != null) setDisplayName(displayName)
                photoUri?.let { setPhotoUri(it.toUri()) }
            }.build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated")
                        _currentUser.value = auth.currentUser
                        onSuccess()
                    } else {
                        Log.e(TAG, "Failed to update profile", task.exception)
                        onFailure(task.exception?.message ?: "Failed to update profile")
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile", e)
            onFailure(e.message ?: "Error updating profile")
        }
    }

    fun verifyPhoneNumber(
        phoneNumber: String,
        activity: android.app.Activity,
        verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                activity,
                verificationCallbacks
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying phone number", e)
            verificationCallbacks.onVerificationFailed(FirebaseException("Failed to send verification code: ${e.message}"))
        }
    }

    fun verifyCode(code: String, onComplete: (Boolean, String?) -> Unit) {
        try {
            if (verificationId.isNullOrEmpty()) {
                onComplete(false, "Verification ID is invalid")
                return
            }

            val credential = PhoneAuthProvider.getCredential(verificationId ?: "", code)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Phone authentication successful")
                        _currentUser.value = auth.currentUser
                        _authState.value = AuthState.Authenticated
                        onComplete(true, null)
                    } else {
                        Log.e(TAG, "Phone authentication failed", task.exception)
                        onComplete(false, task.exception?.message)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying code", e)
            onComplete(false, e.message)
        }
    }

    fun setVerificationId(id: String) {
        verificationId = id
    }

    fun setResendToken(token: PhoneAuthProvider.ForceResendingToken) {
        resendToken = token
    }

    fun resetAuthState() {
        _isNewUser.value = false
        _authMethod.value = AuthMethod.NONE
        _authState.value = AuthState.Unauthenticated
        // Reset any other relevant state variables
    }
}