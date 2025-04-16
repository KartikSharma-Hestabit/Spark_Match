package com.hestabit.sparkmatch.screens.discover

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.utils.Utils.printDebug
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.SwipeDirection
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.viewmodel.DiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DiscoverScreen(onNavigate: (String, UserProfile?) -> Unit) {

    val viewModel: DiscoverViewModel = hiltViewModel()
    val cardsResponse by viewModel.cardsList.collectAsState()

    var cards = remember { mutableStateOf<List<UserProfile>>(emptyList()) }
    var canClick by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    cardsResponse.let {
        when (it) {
            is Response.Failure -> {
                printDebug("${it.exception.message}")
            }

            Response.InitialValue -> {
                printDebug("initial value")
            }

            Response.Loading -> {
                printDebug("data loading")
            }

            is Response.Success<List<UserProfile>> -> {
                printDebug("${it.result}")
                cards.value = it.result
            }
        }
    }

    Column {
        CardStack(
            cards = cards.value,
            onRemoveCard = viewModel::removeCard,
            modifier = Modifier.weight(3.5f),
            onNavigate = onNavigate,
            onReload = { viewModel.reloadData() }
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .background(color = Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallFloatingActionButton(
                onClick = {
                    if (canClick) {
                        canClick = false
                        viewModel.moveCard(SwipeDirection.Left)
                        scope.launch {
                            delay(500)
                            canClick = true
                        }
                    }
                },
                modifier = Modifier
                    .size(78.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp
                ),
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xffF27121)
                )
            }

            SmallFloatingActionButton(
                onClick = {
                    if (canClick) {
                        canClick = false
                        viewModel.moveCard(SwipeDirection.Right)
                        scope.launch {
                            delay(500)
                            canClick = true
                            //TODO : for testing purpose of match found screen
                            onNavigate(Routes.MATCH_FOUND_SCREEN, null)
                        }
                    }

                },
                modifier = Modifier
                    .size(100.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 15.dp,
                    pressedElevation = 10.dp
                ),
                containerColor = Color(0xffE94057)
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_like),
                    contentDescription = "",
                    modifier = Modifier.size(51.dp),
                    tint = Color.White
                )
            }

            SmallFloatingActionButton(
                onClick = { viewModel.moveCard(SwipeDirection.Up) },
                modifier = Modifier
                    .size(78.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 5.dp
                ),
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_superlike),
                    contentDescription = "",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xff8A2387)
                )
            }
        }
    }
}