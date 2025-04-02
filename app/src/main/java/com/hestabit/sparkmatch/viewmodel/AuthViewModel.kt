package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.data.AuthState
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

    //Firebase
    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun login(email: String, password: String){
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun signUp(email: String, password: String){
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}