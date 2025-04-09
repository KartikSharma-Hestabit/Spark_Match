package com.hestabit.sparkmatch.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.router.AuthRoute.PassionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    // Initialize with default value "Male" instead of null
    private val _gender = MutableStateFlow("Male")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _passions = MutableStateFlow<List<PassionType>>(emptyList())
    val passions = _passions.asStateFlow()

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

    // Update to take a specific gender value rather than toggling
    fun updateGender(newGender: String) {
        _gender.value = newGender
    }

    // Add multiple passions at once
    fun updatePassions(passionList: List<PassionType>) {
        _passions.value = passionList
    }

    // Add or remove a single passion
    fun togglePassion(passion: PassionType) {
        _passions.value = if (_passions.value.contains(passion)) {
            _passions.value.filter { it != passion }
        } else {
            _passions.value + passion
        }
    }

    /**
     * Saves the current profile details to Firestore
     * @return Whether the save was successful
     */
    fun saveProfileDetails(onComplete: (Boolean) -> Unit) {
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
                gender = _gender.value,
                passions = _passions.value
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

            // Create user profile with current state values
            val userProfile = UserProfile(
                firstName = _firstName.value,
                lastName = _lastName.value,
                profileImage = _profileImage.value,
                birthday = formattedDate,
                gender = _gender.value, // Use the current gender value
                passions = _passions.value // Use current passions
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
     * Load existing profile data from repository
     */
    fun loadUserProfile() {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile(currentUser.uid)
                profile?.let {
                    _firstName.value = it.firstName
                    _lastName.value = it.lastName
                    _profileImage.value = it.profileImage

                    // Parse birthday to LocalDate if available
                    if (it.birthday.isNotEmpty()) {
                        try {
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            _selectedDate.value = LocalDate.parse(it.birthday, formatter)
                            // Update current year month for the calendar
                            _selectedDate.value?.let { date ->
                                _currentYearMonth.value = YearMonth.of(date.year, date.month)
                            }
                        } catch (e: Exception) {
                            // Handle date parsing error
                        }
                    }

                    // Update gender and passions
                    if (it.gender.isNotEmpty()) {
                        _gender.value = it.gender
                    }

                    if (it.passions.isNotEmpty()) {
                        _passions.value = it.passions
                    }
                }
            } catch (e: Exception) {
                _savingError.value = "Failed to load profile: ${e.message}"
            }
        }
    }

    private val TAG = "ProfileDetailsVM"

    /**
     * Saves basic profile details (name, birthday, profile image)
     * Called from the ProfileDetails screen
     */
    fun saveBasicProfileDetails(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "Save basic profile failed: User not authenticated")
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_firstName.value.isBlank() || _lastName.value.isBlank() || _selectedDate.value == null) {
            Log.e(TAG, "Save basic profile failed: Required fields missing")
            _savingError.value = "Please fill in all required fields"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = _selectedDate.value?.format(dateFormatter) ?: ""

                Log.d(TAG, "Saving basic profile - Name: ${_firstName.value} ${_lastName.value}, Birthday: $formattedDate")

                // Only save the basic details
                val basicUserData = hashMapOf(
                    "firstName" to _firstName.value,
                    "lastName" to _lastName.value,
                    "birthday" to formattedDate
                )

                // If profile image exists, upload and save it
                if (_profileImage.value != null) {
                    Log.d(TAG, "Uploading profile image")
                    val imageUrl = userRepository.uploadProfileImage(_profileImage.value)
                    if (imageUrl != null) {
                        basicUserData["profileImageUrl"] = imageUrl
                    }
                }

                // Save to Firestore with merge option
                userRepository.usersCollection.document(currentUser.uid)
                    .set(basicUserData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Basic profile saved successfully")
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to save basic profile", e)
                        _savingError.value = e.message ?: "Failed to save profile"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during basic profile save", e)
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    /**
     * Saves gender selection
     * Called from the Gender screen
     */
    fun saveGenderSelection(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "Save gender failed: User not authenticated")
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_gender.value.isBlank()) {
            Log.e(TAG, "Save gender failed: No gender selected")
            _savingError.value = "Please select a gender"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                Log.d(TAG, "Saving gender: ${_gender.value}")

                // Only save the gender
                val genderData = hashMapOf(
                    "gender" to _gender.value
                )

                // Save to Firestore with merge option
                userRepository.usersCollection.document(currentUser.uid)
                    .set(genderData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Gender saved successfully")
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to save gender", e)
                        _savingError.value = e.message ?: "Failed to save gender"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during gender save", e)
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    /**
     * Saves passions/interests
     * Called from the Passions screen
     */
    fun savePassions(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "Save passions failed: User not authenticated")
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                // Convert passion types to strings for storage
                val passionStrings = _passions.value.map { it.id }
                Log.d(TAG, "Saving passions: ${passionStrings.joinToString()}")

                // Only save the passions
                val passionsData = hashMapOf(
                    "passions" to passionStrings
                )

                // Save to Firestore with merge option
                userRepository.usersCollection.document(currentUser.uid)
                    .set(passionsData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(TAG, "Passions saved successfully")
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to save passions", e)
                        _savingError.value = e.message ?: "Failed to save passions"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during passions save", e)
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        calendarNavigationJob?.cancel()
    }
}