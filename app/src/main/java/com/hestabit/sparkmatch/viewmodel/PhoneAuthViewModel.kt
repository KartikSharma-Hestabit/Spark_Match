package com.hestabit.sparkmatch.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.hestabit.sparkmatch.firebase.PhoneAuthRepository
import com.hestabit.sparkmatch.firebase.PhoneAuthState
import com.hestabit.sparkmatch.firebase.PhoneUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val phoneAuthRepository: PhoneAuthRepository
) : ViewModel() {

    private val _phoneAuthState = MutableStateFlow<PhoneUiState>(PhoneUiState.Initial)
    val phoneAuthState: StateFlow<PhoneUiState> = _phoneAuthState.asStateFlow()

    private var verificationId: String? = null

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _phoneAuthState.value = PhoneUiState.Loading
        viewModelScope.launch {
            phoneAuthRepository.sendVerificationCode(phoneNumber, activity).collect { state ->
                when (state) {
                    is PhoneAuthState.CodeSent -> {
                        verificationId = state.verificationId
                        _phoneAuthState.value = PhoneUiState.CodeSent
                    }
                    is PhoneAuthState.VerificationCompleted -> {
                        signInWithPhoneAuthCredential(state.credential)
                    }
                    is PhoneAuthState.VerificationFailed -> {
                        _phoneAuthState.value = PhoneUiState.Error(state.message)
                    }
                    is PhoneAuthState.TimeOut -> {
                        _phoneAuthState.value = PhoneUiState.Error("Verification timed out. Please try again.")
                    }
                }
            }
        }
    }

    fun verifyCode(code: String) {
        val id = verificationId
        if (id == null) {
            _phoneAuthState.value = PhoneUiState.Error("Verification ID is null. Please try again.")
            return
        }

        _phoneAuthState.value = PhoneUiState.Loading
        viewModelScope.launch {
            val result = phoneAuthRepository.verifyPhoneNumberWithCode(id, code)
            result.fold(
                onSuccess = { user ->
                    _phoneAuthState.value = PhoneUiState.Authenticated(user.uid)
                },
                onFailure = { e ->
                    _phoneAuthState.value = PhoneUiState.Error(e.message ?: "Failed to verify code")
                }
            )
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _phoneAuthState.value = PhoneUiState.Loading
        viewModelScope.launch {
            val result = phoneAuthRepository.verifyPhoneNumberWithCode(
                credential.provider,
                credential.smsCode ?: ""
            )
            result.fold(
                onSuccess = { user ->
                    _phoneAuthState.value = PhoneUiState.Authenticated(user.uid)
                },
                onFailure = { e ->
                    _phoneAuthState.value = PhoneUiState.Error(e.message ?: "Authentication failed")
                }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            phoneAuthRepository.signOut()
            _phoneAuthState.value = PhoneUiState.Initial
        }
    }
}