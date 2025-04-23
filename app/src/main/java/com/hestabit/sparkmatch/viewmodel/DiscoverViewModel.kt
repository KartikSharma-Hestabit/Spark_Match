package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.auth.User
import com.hestabit.sparkmatch.data.LikedBy
import com.hestabit.sparkmatch.utils.Utils.printDebug
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.SwipeDirection
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.DiscoverRepository
import com.hestabit.sparkmatch.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    private val _cardsList: MutableStateFlow<Response<List<UserProfile>>> = MutableStateFlow(
        Response.InitialValue
    )
    val cardsList: StateFlow<Response<List<UserProfile>>> = _cardsList

    private val _moveCard = MutableStateFlow<SwipeDirection?>(null)
    val moveCard = _moveCard.asStateFlow()

    private val _selectedProfile = MutableStateFlow<UserProfile?>(null)
    val selectedProfile = _selectedProfile.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() = viewModelScope.launch {
        try {
            _cardsList.value = Response.Loading
            val result = discoverRepository.fetchProfiles()
            _cardsList.value = result
        } catch (e: Exception) {
            e.printStackTrace()
            _cardsList.value = Response.Failure(e)
        }
    }

    fun reloadData() {
        fetchUsers()
        if (_cardsList.value !is Response.Loading) {
            fetchUsers()
        }
    }

    fun removeCard(swipeDirection: SwipeDirection, user: UserProfile, onMatchFound : () -> Unit) {
        printDebug("swipeDirection - ${swipeDirection.name}, user - ${user.firstName}")
        val current = _cardsList.value
        if (current is Response.Success) {
            _cardsList.value = Response.Loading
            val updatedList = current.result.toMutableList().apply {
                remove(user)
            }
            _cardsList.value = Response.Success(updatedList)
            if (swipeDirection == SwipeDirection.Right) {
                if(userRepository.getCurrentUserProfile().likedByList.contains(LikedBy(profileImageUrl = user.profileImageUrl.toString(), uid = user.uid))){
                    printDebug("its as match!")
                    onMatchFound()
                    updateLikes(user, true)
                }else {
                    updateLikes(user)
                }
            }
        }
    }

    fun updateLikes(user: UserProfile, isMatch: Boolean = false) = viewModelScope.launch {
        val result = userRepository.updateLikes(user, isMatch)

        result.let {
            when(it){
                is Response.Failure -> {
                    printDebug("error -> ${it.exception.message}")
                }
                Response.InitialValue -> {
//                    TODO()
                }
                Response.Loading -> {
//                    TODO()
                }
                is Response.Success -> {
                    printDebug("upload complete")
                }
            }
        }

    }

    fun moveCard(direction: SwipeDirection?) {
        _moveCard.value = direction
    }

    // Function to fetch a specific profile by ID
    fun fetchProfileById(profileId: String) = viewModelScope.launch {
        try {
            val profiles = discoverRepository.fetchProfiles()
            if (profiles is Response.Success) {
                val profile = profiles.result.find { it.firstName == profileId }
                _selectedProfile.value = profile
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function to select a random profile from the available cards
    fun selectRandomProfile() {
        val current = _cardsList.value
        if (current is Response.Success && current.result.isNotEmpty()) {
            _selectedProfile.value = current.result.random()
        }
    }
}