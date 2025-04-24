package com.hestabit.sparkmatch.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.StorageRepository
import com.hestabit.sparkmatch.repository.UserRepository
import com.hestabit.sparkmatch.router.AuthRoute.PassionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // New error state flows for profession and about fields
    private val _professionError = MutableStateFlow("")
    val professionError: StateFlow<String> = _professionError.asStateFlow()

    private val _aboutError = MutableStateFlow("")
    val aboutError: StateFlow<String> = _aboutError.asStateFlow()

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
        // Clear error if input meets minimum length requirement
        if (about.trim().length >= 50) {
            _aboutError.value = ""
        }
    }

    fun updateProfession(profession: String) {
        _profession.value = profession
        // Clear error if input meets minimum length requirement
        if (profession.trim().length >= 2) {
            _professionError.value = ""
        }
    }

    // New update methods for error states
    fun updateProfessionError(error: String) {
        _professionError.value = error
    }

    fun updateAboutError(error: String) {
        _aboutError.value = error
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

                // Use repository to save basic profile details
                val result = userRepository.saveBasicProfileDetails(
                    userId = currentUser.uid,
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    birthday = formattedDate,
                    homeTown = _homeTown.value,
                    profileImageUri = _profileImage.value
                )

                if (result.isSuccess) {
                    _isSaving.value = false
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to save profile"
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
                val result = userRepository.saveGenderSelection(
                    userId = currentUser.uid,
                    gender = _gender.value
                )

                if (result.isSuccess) {
                    _isSaving.value = false
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to save gender"
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
                val result = userRepository.saveInterestPreference(
                    userId = currentUser.uid,
                    preference = _interestPreference.value
                )

                if (result.isSuccess) {
                    _isSaving.value = false
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to save interest preference"
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

        // Check for minimum length requirements
        if (_profession.value.trim().length < 2) {
            _professionError.value = "Profession must be at least 2 characters"
            _savingError.value = "Please enter a valid profession"
            onComplete(false)
            return
        } else {
            _professionError.value = ""
        }

        if (_about.value.trim().length < 50) {
            _aboutError.value = "About must be at least 50 characters"
            _savingError.value = "Please provide more details in the about section"
            onComplete(false)
            return
        } else {
            _aboutError.value = ""
        }

        _isSaving.value = true
        _savingError.value = null

        viewModelScope.launch {
            try {
                // Use repository to save about details
                val result = userRepository.saveAboutDetails(
                    userId = currentUser.uid,
                    profession = _profession.value,
                    about = _about.value
                )

                if (result.isSuccess) {
                    _isSaving.value = false
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to save about details"
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
                // Use repository to save passions
                val result = userRepository.savePassions(
                    userId = currentUser.uid,
                    passions = _passions.value
                )

                if (result.isSuccess) {
                    _isSaving.value = false
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to save passions"
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

        viewModelScope.launch {
            try {
                // Use repository to update profile details
                val result = userRepository.updateProfileDetails(
                    userId = currentUser.uid,
                    originalProfile = originalProfile,
                    updatedProfile = updatedProfile
                )

                _isSaving.value = false

                if (result.isSuccess) {
                    onComplete(true)
                } else {
                    _savingError.value = result.exceptionOrNull()?.message ?: "Failed to update profile"
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
                storageRepository.getUploadProgress().collect { progress ->
                    _uploadProgress.value = progress
                }

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
    fun uploadGalleryImages(imageUris: List<Uri>, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        if (imageUris.isEmpty()) {
            onComplete(false, "No images to upload")
            return
        }

        viewModelScope.launch {
            _isUploadingImage.value = true
            _savingError.value = "" // Clear any previous errors

            try {
                // Create a separate coroutine for progress monitoring
                val progressJob = launch {
                    storageRepository.getUploadProgress().collect { progress ->
                        _uploadProgress.value = progress
                    }
                }

                // Perform the actual upload on IO dispatcher
                val imageUrls = withContext(Dispatchers.IO) {
                    storageRepository.uploadMultipleImages(imageUris, "gallery_images")
                }

                if (imageUrls.isNotEmpty()) {
                    // Update the user profile with the new gallery images
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        // Get current gallery images and add new ones - also on IO dispatcher
                        val response = withContext(Dispatchers.IO) {
                            userRepository.getUserProfile(currentUser.uid)
                        }

                        if (response is Response.Success) {
                            val currentProfile = response.result
                            val currentGalleryImages = currentProfile.galleryImages
                            val updatedGallery = currentGalleryImages + imageUrls

                            val updates = mapOf("galleryImages" to updatedGallery)
                            // Update the profile on IO dispatcher
                            val result = withContext(Dispatchers.IO) {
                                userRepository.updateUserProfile(currentUser.uid, updates)
                            }

                            if (result.isSuccess) {
                                _galleryImages.value = updatedGallery
                                Log.d(TAG, "Gallery images updated successfully")
                                withContext(Dispatchers.Main) {
                                    onComplete(true, null)
                                }
                            } else {
                                _savingError.value = "Failed to update gallery images"
                                withContext(Dispatchers.Main) {
                                    onComplete(false, "Failed to update profile with new images")
                                }
                            }
                        } else {
                            _savingError.value = "Failed to get current user profile"
                            withContext(Dispatchers.Main) {
                                onComplete(false, "Failed to get current user profile")
                            }
                        }
                    } else {
                        _savingError.value = "User not authenticated"
                        withContext(Dispatchers.Main) {
                            onComplete(false, "User not authenticated")
                        }
                    }
                } else {
                    _savingError.value = "Failed to upload gallery images"
                    withContext(Dispatchers.Main) {
                        onComplete(false, "Failed to upload images")
                    }
                }
                progressJob.cancel()
            } catch (e: Exception) {
                Log.e(TAG, "Error uploading gallery images", e)
                _savingError.value = "Error uploading gallery images: ${e.message}"
                withContext(Dispatchers.Main) {
                    onComplete(false, e.message)
                }
            } finally {
                _isUploadingImage.value = false
                _uploadProgress.value = 0
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
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val response = userRepository.getUserProfile(currentUser.uid)
                        if (response is Response.Success) {
                            val currentProfile = response.result
                            val updates = if (isProfileImage) {
                                mapOf("profileImageUrl" to "")
                            } else {
                                val currentGallery = currentProfile.galleryImages
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
                        } else {
                            _savingError.value = "Failed to get current user profile"
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
        _savingError.value = ""
        calendarNavigationJob?.cancel()
    }
}