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
import com.hestabit.sparkmatch.repository.StorageRepository
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.router.AuthRoute.PassionType
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class ProfileDetailsViewModel @Inject constructor(
    val userRepository: UserRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ProfileDetailsViewModel"
    }

    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()

    private val _homeTown = MutableStateFlow("")
    val homeTown = _homeTown.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val _profileImage = MutableStateFlow<Uri?>(null)
    val profileImage = _profileImage.asStateFlow()

    private val _profileImageUrl = MutableStateFlow<String?>(null)
    val profileImageUrl = _profileImageUrl.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0)
    val uploadProgress = _uploadProgress.asStateFlow()

    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage = _isUploadingImage.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<String>>(emptyList())
    val galleryImages = _galleryImages.asStateFlow()

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

    // Setting methods for initial data load
    fun setGalleryImages(images: List<String>) {
        _galleryImages.value = images
    }

    fun setProfileImageUrl(url: String?) {
        if (url != null) {
            _profileImageUrl.value = url
        }
    }

    fun updateFirstName(name: String) {
        _firstName.value = name
    }

    fun updateLastName(name: String) {
        _lastName.value = name
    }

    fun updateHomeTown(town: String) {
        _homeTown.value = town
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

        if (_firstName.value.isBlank() || _lastName.value.isBlank() || _selectedDate.value == null || _homeTown.value.isBlank()) {
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
                    "birthday" to formattedDate,
                    "homeTown" to "--homeTown--"
                )

                if (_profileImage.value != null) {
                    val imageUrl =
                        storageRepository.uploadImage(_profileImage.value!!, "profile_images")
                    if (imageUrl != null) {
                        basicUserData["profileImageUrl"] = imageUrl
                        _profileImageUrl.value = imageUrl
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

                userRepository.usersCollection().document(currentUser.uid)
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

        if (_about.value.trim().length < 150) {
            _savingError.value = "Please complete atleast 150 words of about section"
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
                userRepository.usersCollection()
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProfileDetails(
        updatedProfile: UserProfile,
        originalProfile: UserProfile,
        onComplete: (Boolean) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _savingError.value = "User not authenticated"
            onComplete(false)
            return
        }

        _isSaving.value = true
        _savingError.value = null

        // Create a map to hold only the fields that have changed
        val updatedFields = mutableMapOf<String, Any>()

        // Check each field to see if it has changed
        if (updatedProfile.firstName != originalProfile.firstName) {
            updatedFields["firstName"] = updatedProfile.firstName
            _firstName.value = updatedProfile.firstName
        }

        if (updatedProfile.lastName != originalProfile.lastName) {
            updatedFields["lastName"] = updatedProfile.lastName
            _lastName.value = updatedProfile.lastName
        }

        if (updatedProfile.gender != originalProfile.gender) {
            updatedFields["gender"] = updatedProfile.gender
            _gender.value = updatedProfile.gender
        }

        if (updatedProfile.interestPreference != originalProfile.interestPreference) {
            updatedFields["interestPreference"] = updatedProfile.interestPreference
            _interestPreference.value = updatedProfile.interestPreference
        }

        if (updatedProfile.profession != originalProfile.profession) {
            updatedFields["profession"] = updatedProfile.profession
            _profession.value = updatedProfile.profession
        }

        if (updatedProfile.about != originalProfile.about) {
            updatedFields["about"] = updatedProfile.about
            _about.value = updatedProfile.about
        }

        // Add location field comparison
        if (updatedProfile.location != originalProfile.location) {
            updatedFields["location"] = updatedProfile.location
        }

        // Compare passions lists to check if they're different
        val originalPassionSet = originalProfile.passions.toSet()
        val updatedPassionSet = updatedProfile.passions.toSet()

        if (originalPassionSet != updatedPassionSet) {
            // Convert passions to the string list format expected by Firestore
            val passionStrings = userRepository.passionsToStringList(updatedProfile.passionsObject)
            updatedFields["passions"] = passionStrings
            _passions.value = updatedProfile.passionsObject
        }

        // Handle gallery images
        if (!updatedProfile.galleryImages.equals(originalProfile.galleryImages)) {
            updatedFields["galleryImages"] = updatedProfile.galleryImages
            _galleryImages.value = updatedProfile.galleryImages
        }

        // Handle profile image separately as it requires special processing
        if (updatedProfile.profileImage != originalProfile.profileImage) {
            viewModelScope.launch {
                try {
                    val imageUrl = storageRepository.uploadImage(
                        updatedProfile.profileImage!!,
                        "profile_images"
                    )
                    if (imageUrl != null) {
                        updatedFields["profileImageUrl"] = imageUrl
                        _profileImage.value = updatedProfile.profileImage
                        _profileImageUrl.value = imageUrl
                    }

                    // Proceed with saving the other fields after image upload
                    saveUpdatedFields(currentUser.uid, updatedFields, onComplete)
                } catch (e: Exception) {
                    _savingError.value = "Failed to upload profile image: ${e.message}"
                    _isSaving.value = false
                    onComplete(false)
                }
            }
        } else {
            // If no profile image change, just save the other fields
            saveUpdatedFields(currentUser.uid, updatedFields, onComplete)
        }
    }

    private fun saveUpdatedFields(
        userId: String,
        updatedFields: Map<String, Any>,
        onComplete: (Boolean) -> Unit
    ) {
        if (updatedFields.isEmpty()) {
            _isSaving.value = false
            onComplete(true)
            return
        }

        viewModelScope.launch {
            try {
                val result = userRepository.updateUserProfile(userId, updatedFields)
                _isSaving.value = false

                if (result.isSuccess) {
                    onComplete(true)
                } else {
                    _savingError.value =
                        result.exceptionOrNull()?.message ?: "Failed to update profile"
                    onComplete(false)
                }
            } catch (e: Exception) {
                _savingError.value = "An unexpected error occurred: ${e.message}"
                _isSaving.value = false
                onComplete(false)
            }
        }
    }

    /**
     * Upload a profile image and update the user profile
     */
    fun uploadProfileImage(imageUri: Uri?) {
        if (imageUri == null) return

        viewModelScope.launch {
            _isUploadingImage.value = true
            try {
                val imageUrl = storageRepository.uploadImage(imageUri, "profile_images")

                if (imageUrl != null) {
                    // Update the user profile with the new image URL
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val updates = mapOf("profileImageUrl" to imageUrl)
                        val result = userRepository.updateUserProfile(currentUser.uid, updates)

                        if (result.isSuccess) {
                            _profileImage.value = imageUri
                            _profileImageUrl.value = imageUrl
                            Log.d(TAG, "Profile image updated successfully")
                        } else {
                            _savingError.value = "Failed to update profile with new image"
                        }
                    }
                } else {
                    _savingError.value = "Failed to upload image"
                }
            } catch (e: Exception) {
                _savingError.value = "Error uploading image: ${e.message}"
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    /**
     * Upload multiple gallery images
     */
    fun uploadGalleryImages(imageUris: List<Uri>) {
        if (imageUris.isEmpty()) return

        viewModelScope.launch {
            _isUploadingImage.value = true
            try {
                val imageUrls = storageRepository.uploadMultipleImages(imageUris, "gallery_images")

                if (imageUrls.isNotEmpty()) {
                    // Update the user profile with the new gallery images
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        // Get current gallery images and add new ones
                        val currentProfile = userRepository.getUserProfile(currentUser.uid)
                        val currentGalleryImages = currentProfile?.galleryImages ?: emptyList()
                        val updatedGallery = currentGalleryImages + imageUrls

                        val updates = mapOf("galleryImages" to updatedGallery)
                        val result = userRepository.updateUserProfile(currentUser.uid, updates)

                        if (result.isSuccess) {
                            _galleryImages.value = updatedGallery
                            Log.d(TAG, "Gallery images updated successfully")
                        } else {
                            _savingError.value = "Failed to update gallery images"
                        }
                    }
                } else {
                    _savingError.value = "Failed to upload gallery images"
                }
            } catch (e: Exception) {
                _savingError.value = "Error uploading gallery images: ${e.message}"
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    /**
     * Delete an image from storage and update the user profile
     */
    fun deleteImage(imageUrl: String, isProfileImage: Boolean = false) {
        viewModelScope.launch {
            try {
                val deleted = storageRepository.deleteImage(imageUrl)

                if (deleted) {
                    // Update the user profile accordingly
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val updates = if (isProfileImage) {
                            mapOf("profileImageUrl" to "")
                        } else {
                            // Remove from gallery images
                            val currentProfile = userRepository.getUserProfile(currentUser.uid)
                            val currentGallery = currentProfile?.galleryImages ?: emptyList()
                            val updatedGallery = currentGallery.filter { it != imageUrl }
                            mapOf("galleryImages" to updatedGallery)
                        }

                        val result = userRepository.updateUserProfile(currentUser.uid, updates)

                        if (result.isSuccess) {
                            if (isProfileImage) {
                                _profileImage.value = null
                                _profileImageUrl.value = null
                            } else {
                                _galleryImages.value =
                                    _galleryImages.value.filter { it != imageUrl }
                            }
                            Log.d(TAG, "Image deleted successfully")
                        } else {
                            _savingError.value = "Failed to update profile after image deletion"
                        }
                    }
                } else {
                    _savingError.value = "Failed to delete image"
                }
            } catch (e: Exception) {
                _savingError.value = "Error deleting image: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        calendarNavigationJob?.cancel()
    }
}