package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.utils.Utils.printDebug
import com.hestabit.sparkmatch.data.CardData
import com.hestabit.sparkmatch.data.SwipeDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {

    private val _cardsList: MutableStateFlow<List<CardData>> = MutableStateFlow(
        mutableListOf(
            CardData(1, "Camila Snow", 23, "Marketer", R.drawable.ava, distance = 1),
            CardData(2, "Bred Jackson", 25, "Photographer", R.drawable.img_2, distance = 2),
            CardData(3, "Jessica Parker", 24, "Model", R.drawable.sophia, distance = 3),
        )
    )

    val cardsList = _cardsList.asStateFlow()


    private val _moveCard = MutableStateFlow<SwipeDirection?>(null)
    val moveCard = _moveCard.asStateFlow()


    fun reloadData(){
        _cardsList.value =  mutableListOf(
            CardData(1, "Camila Snow", 23, "Marketer", R.drawable.ava, distance = 1),
            CardData(2, "Bred Jackson", 25, "Photographer", R.drawable.img_2, distance = 2),
            CardData(3, "Jessica Parker", 24, "Model", R.drawable.sophia, distance = 3),
        )
    }

    fun removeCard(album: CardData) {
        if (_cardsList.value.isNotEmpty()) {
            _cardsList.value -= album
            printDebug("removed -> $album, size -> ${cardsList.value.size}")
        }
    }

    fun moveCard(direction: SwipeDirection?){
        _moveCard.value = direction
    }
}