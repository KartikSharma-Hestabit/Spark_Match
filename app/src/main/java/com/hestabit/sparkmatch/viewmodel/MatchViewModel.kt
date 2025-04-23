package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.hestabit.sparkmatch.data.LikedBy
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _likedByList = MutableStateFlow<List<LikedBy>>(emptyList())
    val likedByList = _likedByList.asStateFlow()

    private val _matchList = MutableStateFlow<List<MatchUser>>(emptyList())
    val matchList = _matchList.asStateFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    private val _likeErrorMsg = MutableStateFlow("")
    val likeErrorMsg = _likeErrorMsg.asStateFlow()

    private val _matchErrorMsg = MutableStateFlow("")
    val matchErrorMsg = _matchErrorMsg.asStateFlow()

    init {
        fetchMatchData()
    }

    fun fetchMatchData() = viewModelScope.launch {

        userRepository.getUserProfile(firebaseAuth.currentUser!!.uid)

        userRepository.listenUserUpdates(firebaseAuth.currentUser!!.uid).collect{ data ->
            _likedByList.emit(data.likedByList)
            _matchList.emit(data.matchList)
        }

    }

}