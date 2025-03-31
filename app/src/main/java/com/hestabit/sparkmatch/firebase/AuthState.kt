package com.hestabit.sparkmatch.firebase

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class UserExists(val identifier: String) : AuthState()
    data class NewUser(val identifier: String) : AuthState()
    data class Authenticated(val userId: String) : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}