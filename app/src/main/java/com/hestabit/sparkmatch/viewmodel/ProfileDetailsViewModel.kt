package com.hestabit.sparkmatch.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hestabit.sparkmatch.data.ProfileDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProfileDetailsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailsState())

    val uiState: StateFlow<ProfileDetailsState> = _uiState.asStateFlow()

    fun updateFirstName(firstName: String) {
        _uiState.update { currentState ->
            currentState.copy(firstName = firstName)
        }
    }

    fun updateLastName(lastName: String) {
        _uiState.update { currentState ->
            currentState.copy(lastName = lastName)
        }
    }

    fun updateBirthDate(birthDate: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(birthDate = birthDate)
        }
    }

    fun updateProfileImage(imageUri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(profileImageUri = imageUri)
        }
    }

    fun saveProfileDetails(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to save profile details"
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateProfileDetails(): Boolean {
        val currentState = _uiState.value

        val isFirstNameValid = currentState.firstName.trim().isNotEmpty()
        val isLastNameValid = currentState.lastName.trim().isNotEmpty()
        val isBirthDateValid = currentState.birthDate != null

        val isAgeValid = currentState.birthDate?.let { birthDate ->
            val minAgeDate = LocalDate.now().minusYears(18)
            birthDate.isBefore(minAgeDate)
        } == true

        if (!isFirstNameValid || !isLastNameValid) {
            _uiState.update { it.copy(error = "Please enter your full name") }
            return false
        }

        if (!isBirthDateValid) {
            _uiState.update { it.copy(error = "Please select your birth date") }
            return false
        }

        if (!isAgeValid) {
            _uiState.update { it.copy(error = "You must be at least 18 years old") }
            return false
        }

        _uiState.update { it.copy(error = null) }
        return true
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}