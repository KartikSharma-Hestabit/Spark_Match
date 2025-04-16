package com.hestabit.sparkmatch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.DiscoverRepository
import com.hestabit.sparkmatch.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val discoverRepository: DiscoverRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val TAG = "ProfileViewModel"

    private val _profileData = MutableStateFlow<Response<UserProfile>>(Response.InitialValue)
    val profileData: StateFlow<Response<UserProfile>> = _profileData.asStateFlow()

    private val _otherUserProfile = MutableStateFlow<Response<UserProfile>>(Response.InitialValue)
    val otherUserProfile: StateFlow<Response<UserProfile>> = _otherUserProfile.asStateFlow()

    // Keep track of the last userId we fetched so we don't fetch the same profile twice
    private var lastFetchedUserId: String? = null

    private val auth = FirebaseAuth.getInstance()

    init {
        fetchCurrentUserProfile()
    }

    /**
     * Fetches the current user's profile from Firestore
     */
    fun fetchCurrentUserProfile() {
        viewModelScope.launch {
            try {
                _profileData.value = Response.Loading
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val profile = userRepository.getUserProfile(currentUser.uid)
                    if (profile != null) {
                        _profileData.value = Response.Success(profile)
                    } else {
                        _profileData.value = Response.Failure(Exception("Profile not found"))
                    }
                } else {
                    _profileData.value = Response.Failure(Exception("User not authenticated"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching current user profile", e)
                _profileData.value = Response.Failure(e)
            }
        }
    }

    /**
     * Fetches a specific user profile by ID
     * This can be a Firebase UID or a custom ID (like firstName_lastName)
     */
    fun fetchUserProfile(userId: String) {
        // Important: Reset the state to make sure we're showing fresh data
        _otherUserProfile.value = Response.InitialValue

        // Update the lastFetchedUserId to track what we're fetching
        lastFetchedUserId = userId

        viewModelScope.launch {
            try {
                Log.d(TAG, "Fetching profile for userId: $userId")
                _otherUserProfile.value = Response.Loading

                // First try to fetch directly from the user repository
                var profile = userRepository.getUserProfile(userId)

                // If not found, try checking if it's a composite ID (firstName_lastName)
                if (profile == null && userId.contains("_")) {
                    val nameParts = userId.split("_")
                    if (nameParts.size >= 2) {
                        val firstName = nameParts[0]
                        val lastName = nameParts[1]

                        Log.d(TAG, "Looking up profile by firstName: $firstName, lastName: $lastName")

                        // Query Firestore for a match on firstName and lastName
                        try {
                            val querySnapshot = firestore.collection("users")
                                .whereEqualTo("firstName", firstName)
                                .whereEqualTo("lastName", lastName)
                                .get()
                                .await()

                            if (!querySnapshot.isEmpty) {
                                // Get the first matching document
                                val document = querySnapshot.documents[0]
                                profile = document.toObject(UserProfile::class.java)
                                Log.d(TAG, "Found profile in Firestore: $profile")
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error querying Firestore", e)
                        }
                    }
                }

                // If still not found, fetch from discover repository
                if (profile != null) {
                    _otherUserProfile.value = Response.Success(profile)
                    Log.d(TAG, "Successfully loaded profile: ${profile.firstName} ${profile.lastName}")
                } else {
                    // If we couldn't find the profile, try getting it from the discover repository
                    // This is a fallback for demo purposes
                    val profilesList = discoverRepository.fetchProfiles()
                    if (profilesList is Response.Success) {
                        // Try to find a profile with matching ID components
                        val matchingProfile = if (userId.contains("_")) {
                            val nameParts = userId.split("_")
                            if (nameParts.size >= 2) {
                                val firstName = nameParts[0]
                                val lastName = nameParts[1]

                                Log.d(TAG, "Looking for profile in discover repo with firstName: $firstName, lastName: $lastName")

                                profilesList.result.find {
                                    it.firstName == firstName && it.lastName == lastName
                                }
                            } else null
                        } else null

                        if (matchingProfile != null) {
                            _otherUserProfile.value = Response.Success(matchingProfile)
                            Log.d(TAG, "Found matching profile in discover repo: ${matchingProfile.firstName} ${matchingProfile.lastName}")
                        } else {
                            // Only if we really can't find the profile, resort to a random profile
                            Log.d(TAG, "No matching profile found for userId: $userId, using random profile")
                            val randomProfile = profilesList.result.randomOrNull()
                            if (randomProfile != null) {
                                _otherUserProfile.value = Response.Success(randomProfile)
                            } else {
                                _otherUserProfile.value = Response.Failure(Exception("Profile not found"))
                            }
                        }
                    } else {
                        _otherUserProfile.value = Response.Failure(Exception("Failed to fetch profiles"))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user profile", e)
                _otherUserProfile.value = Response.Failure(e)
            }
        }
    }

    /**
     * Fetches a random profile from the available profiles
     * Useful for demonstration purposes
     */
    fun getRandomProfile() {
        viewModelScope.launch {
            try {
                _otherUserProfile.value = Response.Loading
                val profilesList = discoverRepository.fetchProfiles()
                if (profilesList is Response.Success && profilesList.result.isNotEmpty()) {
                    // Get a random profile from the list
                    val randomProfile = profilesList.result.random()
                    _otherUserProfile.value = Response.Success(randomProfile)
                } else {
                    _otherUserProfile.value = Response.Failure(Exception("No profiles available"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching random profile", e)
                _otherUserProfile.value = Response.Failure(e)
            }
        }
    }

    /**
     * Creates a demo profile to use when no profiles are available
     * For testing and demonstration purposes only
     */
    private fun createDemoProfile(): UserProfile {
        return UserProfile(
            firstName = "Jessica",
            lastName = "Parker",
            profileImageUrl = null,
            birthday = "1998-05-15",
            gender = "Female",
            interestPreference = "Men",
            location = "Chicago, IL",
            profession = "Professional model",
            about = "My name is Jessica Parker and I enjoy meeting new people and finding ways to help them have an uplifting experience. I enjoy reading..",
            passions = listOf("Travelling", "Books", "Music"),
            galleryImages = emptyList()
        )
    }
}