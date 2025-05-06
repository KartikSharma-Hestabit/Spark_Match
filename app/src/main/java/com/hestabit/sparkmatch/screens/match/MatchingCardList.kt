package com.hestabit.sparkmatch.screens.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.viewmodel.MatchViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchingCardList(
    onNavigate: (String, UserProfile?, String?) -> Unit
) {
    val viewModel: MatchViewModel = hiltViewModel()
    val matches = viewModel.matchList.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(matches.value) { user ->
            MatchingCard(
                cardData = user,
                modifier = Modifier
                    .height(200.dp)
                    .padding(8.dp),
                onNavigate = onNavigate
            )
        }
    }
}