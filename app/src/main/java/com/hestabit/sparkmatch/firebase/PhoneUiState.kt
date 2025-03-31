package com.hestabit.sparkmatch.firebase

sealed class PhoneUiState {
    object Initial : PhoneUiState()
    object Loading : PhoneUiState()
    object CodeSent : PhoneUiState()
    data class UserExists(val phoneNumber: String) : PhoneUiState()
    data class NewUser(val phoneNumber: String) : PhoneUiState()
    data class Authenticated(val userId: String) : PhoneUiState()
    data class Error(val message: String) : PhoneUiState()
}