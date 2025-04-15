package com.hestabit.sparkmatch.data

import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val currentUser: FirebaseUser? = null,
    val authState: AuthState = AuthState.Unauthenticated,
    val isNewUser: Boolean = false,
    val authMethod: AuthMethod = AuthMethod.NONE
)
