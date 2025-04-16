package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.data.CardData
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.SwipeDirection
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.repository.DiscoverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(private val discoverRepository: DiscoverRepository) :
    ViewModel() {

    private val _cardsList: MutableStateFlow<Response<List<UserProfile>>> = MutableStateFlow(
        Response.InitialValue
    )
    val cardsList: StateFlow<Response<List<UserProfile>>> = _cardsList

    private val _moveCard = MutableStateFlow<SwipeDirection?>(null)
    val moveCard = _moveCard.asStateFlow()

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
        if (_cardsList.value !is Response.Loading) {
            fetchUsers()
        }
    }

    fun removeCard(user: UserProfile) {
        val current = _cardsList.value
        if (current is Response.Success) {
            _cardsList.value = Response.Loading
            val updatedList = current.result.toMutableList().apply {
                remove(user)
            }
            _cardsList.value = Response.Success(updatedList)
            printDebug("removed -> $user, size -> ${updatedList.size}")
        }
    }

    fun moveCard(direction: SwipeDirection?) {
        _moveCard.value = direction
    }
}