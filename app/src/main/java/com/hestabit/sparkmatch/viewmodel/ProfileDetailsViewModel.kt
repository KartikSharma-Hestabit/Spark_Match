package com.hestabit.sparkmatch.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.repository.UserRepositoryImpl
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
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class ProfileDetailsViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
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

    private val _gender = MutableStateFlow("Male")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _passions = MutableStateFlow<List<PassionType>>(emptyList())

    private val _interestPreference = MutableStateFlow("Everyone")
    val interestPreference: StateFlow<String> = _interestPreference.asStateFlow()

    private val _profession = MutableStateFlow("")
    val profession = _profession.asStateFlow()

    private val _about = MutableStateFlow("")
    val about = _about.asStateFlow()

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

    fun updateGender(newGender: String) {
        _gender.value = newGender
    }

    fun updateInterestPreference(preference: String) {
        _interestPreference.value = preference
    }

    fun updatePassions(passionList: List<PassionType>) {
        _passions.value = passionList
    }

    fun updateAbout(about: String) {
        _about.value = about
    }

    fun updateProfession(profession: String) {
        _profession.value = profession
    }

    fun saveBasicProfileDetails(onComplete: (Boolean) -> Unit) {
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
            try {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = _selectedDate.value?.format(dateFormatter) ?: ""
                val basicUserData = hashMapOf(
                    "firstName" to _firstName.value,
                    "lastName" to _lastName.value,
                    "birthday" to formattedDate
                )

                if (_profileImage.value != null) {
                    val imageUrl = userRepository.uploadProfileImage(_profileImage.value)
                    if (imageUrl != null) {
                        basicUserData["profileImageUrl"] = imageUrl
                    }
                }
                userRepository.usersCollection().document(currentUser.uid)
                    .set(basicUserData, SetOptions.merge())
                    .addOnSuccessListener {
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        _savingError.value = e.message ?: "Failed to save profile"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    fun saveGenderSelection(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_gender.value.isBlank()) {
            _savingError.value = "Please select a gender"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                val genderData = hashMapOf(
                    "gender" to _gender.value
                )

                userRepository.usersCollection().document(currentUser.uid)
                    .set(genderData, SetOptions.merge())
                    .addOnSuccessListener {
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        _savingError.value = e.message ?: "Failed to save gender"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    fun saveInterestPreference(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_interestPreference.value.isBlank()) {
            _savingError.value = "Please select who you're interested in"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                val preferenceData = hashMapOf(
                    "interestPreference" to _interestPreference.value
                )

                userRepository.usersCollection.document(currentUser.uid)
                    .set(preferenceData, SetOptions.merge())
                    .addOnSuccessListener {
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        _savingError.value = e.message ?: "Failed to save interest preference"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    fun saveAboutDetails(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        if (_profession.value.isBlank() || _about.value.isBlank()) {
            _savingError.value = "Please fill in all required fields"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                // Prepare data to update in Firestore
                val updatedFields = mapOf(
                    "profession" to _profession.value,
                    "about" to _about.value
                )

                // Update only the profession and about fields in Firestore
                userRepository.usersCollection
                    .document(currentUser.uid)
                    .set(updatedFields, SetOptions.merge())
                    .addOnSuccessListener {
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        _savingError.value = e.message ?: "Failed to save about details"
                        _isSaving.value = false
                        onComplete(false)
                    }

            } catch (e: Exception) {
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    fun savePassions(onComplete: (Boolean) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                val passionStrings = _passions.value.map { it.id }
                val passionsData = hashMapOf(
                    "passions" to passionStrings
                )
                userRepository.usersCollection().document(currentUser.uid)
                    .set(passionsData, SetOptions.merge())
                    .addOnSuccessListener {
                        _isSaving.value = false
                        onComplete(true)
                    }
                    .addOnFailureListener { e ->
                        _savingError.value = e.message ?: "Failed to save passions"
                        _isSaving.value = false
                        onComplete(false)
                    }
            } catch (e: Exception) {
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