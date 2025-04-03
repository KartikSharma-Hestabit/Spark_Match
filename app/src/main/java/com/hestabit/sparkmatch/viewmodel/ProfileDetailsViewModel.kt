package com.hestabit.sparkmatch.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.router.AuthRoute.PassionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ProfileDetailsViewModel : ViewModel() {
    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage = _profileImage.asStateFlow()

    private val _currentYearMonth = MutableStateFlow(YearMonth.now().minusYears(18))
    val currentYearMonth = _currentYearMonth.asStateFlow()

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible = _isBottomSheetVisible.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _savingError = MutableStateFlow<String?>(null)
    val savingError = _savingError.asStateFlow()

    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    private var calendarNavigationJob: Job? = null

    fun updateFirstName(name: String) {
        _firstName.value = name
    }

    fun updateLastName(name: String) {
        _lastName.value = name
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun updateProfileImage(uri: Uri?) {
        _profileImage.value = uri
    }

    fun updateCurrentYearMonth(yearMonth: YearMonth, scope: CoroutineScope) {
        calendarNavigationJob?.cancel()
        calendarNavigationJob = scope.launch {
            _currentYearMonth.value = yearMonth
        }
    }

    fun showBottomSheet() {
        _isBottomSheetVisible.value = true
    }

    fun hideBottomSheet() {
        _isBottomSheetVisible.value = false
    }

    /**
     * Saves the current profile details to Firestore
     * Note: This method assumes gender and passions will be set later
     * @param gender The selected gender
     * @param passions The selected passions
     * @return Whether the save was successful
     */
    fun saveProfileDetails(gender: String, passions: List<PassionType>, onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_firstName.value.isBlank() || _lastName.value.isBlank() || _selectedDate.value == null) {
            _savingError.value = "Please fill in all required fields"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = _selectedDate.value?.format(dateFormatter) ?: ""

            val userProfile = UserProfile(
                firstName = _firstName.value,
                lastName = _lastName.value,
                profileImage = _profileImage.value,
                birthday = formattedDate,
                gender = gender,
                passions = passions
            )

            val result = userRepository.saveUserProfile(currentUser.uid, userProfile)
            _isSaving.value = false

            if (result.isSuccess) {
                onComplete(true)
            } else {
                _savingError.value = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                onComplete(false)
            }
        }
    }

    /**
     * Updates only the profile details entered in this screen
     * @param onComplete Callback with success status
     */
    fun savePartialProfileDetails(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_firstName.value.isBlank() || _lastName.value.isBlank() || _selectedDate.value == null) {
            _savingError.value = "Please fill in all required fields"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = _selectedDate.value?.format(dateFormatter) ?: ""

            // Create a full user profile with default values
            val userProfile = UserProfile(
                firstName = _firstName.value,
                lastName = _lastName.value,
                profileImage = _profileImage.value,
                birthday = formattedDate,
                gender = "", // Default empty values
                passions = emptyList() // Will be filled in later
            )

            // Try to save the full profile first, which creates the document
            val result = userRepository.saveUserProfile(currentUser.uid, userProfile)
            _isSaving.value = false

            if (result.isSuccess) {
                onComplete(true)
            } else {
                _savingError.value = result.exceptionOrNull()?.message ?: "Unknown error occurred"
                onComplete(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        calendarNavigationJob?.cancel()
    }
}