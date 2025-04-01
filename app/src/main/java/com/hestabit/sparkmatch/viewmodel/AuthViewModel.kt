package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isNewUser = MutableStateFlow(
        savedStateHandle.get<Boolean>("isNewUser") != false
    )

    val isNewUser: StateFlow<Boolean> = _isNewUser.asStateFlow()

    private val _authMethod = MutableStateFlow<AuthMethod>(
        savedStateHandle.get<AuthMethod>("authMethod") ?: AuthMethod.NONE
    )
    val authMethod: StateFlow<AuthMethod> = _authMethod.asStateFlow()

    fun setNewUserState(isNew: Boolean) {
        _isNewUser.value = isNew
        savedStateHandle["isNewUser"] = isNew
    }

    fun setAuthMethod(method: AuthMethod) {
        _authMethod.value = method
        savedStateHandle["authMethod"] = method
    }

    enum class AuthMethod {
        NONE,
        EMAIL,
        PHONE
    }

    fun resetAuthState() {
        setNewUserState(true)
        setAuthMethod(AuthMethod.NONE)
    }

}