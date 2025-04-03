package com.hestabit.sparkmatch.screens.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.common.DefaultIconButton
import com.hestabit.sparkmatch.data.CardData
import com.hestabit.sparkmatch.screens.chat.MessageScreen
import com.hestabit.sparkmatch.screens.discover.DiscoverScreen
import com.hestabit.sparkmatch.screens.match.MatchScreen
import com.hestabit.sparkmatch.screens.profile.ProfileScreen
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String, CardData?) -> Unit) {

    val annotatedText = buildAnnotatedString {
        // Add non-clickable text
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 24.sp))
        append(
            "Discover"
        )
        pop()
        pushStyle(
            SpanStyle(
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
        )
        append("\nDelhi, IN")
        pop()
    }

    val matchesTextHeading = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 34.sp))
        append("Matches")
        pop()
    }

    val messageTextHeading = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 34.sp))
        append("Messages")
        pop()
    }

    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    var selectedItem by remember { mutableIntStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = White,
        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(White),
                modifier = Modifier,
                title = {
                    if (selectedItem == 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.spark_match_logo),
                                tint = HotPink,
                                contentDescription = "Profile Screen Logo",
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Spark Match",
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = HotPink,
                                modifier = Modifier
                            )
                        }
                    } else {
                        Text(
                            if (selectedItem == 0) annotatedText else if (selectedItem == 1) matchesTextHeading else messageTextHeading,
                            textAlign = TextAlign.Start,
                            style = TextStyle(),
                            modifier = Modifier.padding(start = 25.dp)
                        )
                    }
                },
                actions = {
                    if (selectedItem != 3) {
                        DefaultIconButton(
                            if (selectedItem == 0) R.drawable.setting_config else R.drawable.sort,
                            modifier = Modifier.padding(end = 40.dp)
                        ) {}
                    }
                }
            )
        },
        bottomBar = {
            CustomBottomAppBar(selectedItem) { index ->
                selectedItem = index
                coroutineScope.launch {
                    pagerState.animateScrollToPage(
                        index,
                        animationSpec = tween(durationMillis = 300, easing = EaseInOutQuad)
                    )
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            modifier = Modifier.padding(paddingValues),
            state = pagerState,
            beyondViewportPageCount = 4,
            userScrollEnabled = false
        ) { page ->
            when (page) {
                0 -> DiscoverScreen(onNavigate = onNavigate)
                1 -> MatchScreen(onNavigate = onNavigate)
                2 -> MessageScreen(onNavigate = { onNavigate(it, null) })
                3 -> ProfileScreen(onNavigate = { onNavigate(it, null) })
            }
        }
    }
}

@Composable
fun CustomBottomAppBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Cards", "Favorites", "Messages", "Profile")
    val icons = listOf(
        R.drawable.discover_active,
        R.drawable.match_active_badge,
        R.drawable.chat_active,
        R.drawable.profile_active
    )
    val iconsUnselected = listOf(
        R.drawable.discover_inactive,
        R.drawable.match_inactive_badge,
        R.drawable.chat_inactive,
        R.drawable.profile_inactive
    )

    var isLeftMoving by remember { mutableStateOf(true) }

    val leftDivider = animateFloatAsState(
        targetValue = if (selectedItem == 0) 0.001f else selectedItem / 4f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = if (!isLeftMoving) 200 else 0,
            easing = EaseInOutQuad
        )
    )
    val rightDivider = animateFloatAsState(
        targetValue = if (selectedItem == 3) 0.001f else (3 - selectedItem) / 4f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = if (isLeftMoving) 200 else 0,
            easing = EaseInOutQuad
        )
    )

    BottomAppBar(containerColor = Color(0xffF3F3F3), modifier = Modifier.height(82.dp)) {
        Column(verticalArrangement = Arrangement.Top) {
            Row {
                HorizontalDivider(
                    thickness = 0.dp,
                    color = Color.Unspecified,
                    modifier = Modifier.weight(leftDivider.value)
                )
                Box(modifier = Modifier.weight(1 / 4f), contentAlignment = Alignment.Center) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth(0.65f)
                    )
                }

                HorizontalDivider(
                    thickness = 0.dp,
                    color = Color.Unspecified,
                    modifier = Modifier.weight(rightDivider.value)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                items.forEachIndexed { index, title ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null  // Disables ripple effect
                            ) {
                                if (selectedItem > index) {
                                    printDebug("moving left")
                                    isLeftMoving = true
                                } else {
                                    printDebug("moving right")
                                    isLeftMoving = false
                                }
                                onItemSelected(index)
                            }
                            .fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = if (selectedItem == index) icons[index] else iconsUnselected[index]),
                            contentDescription = title,
                            tint = if (selectedItem == 3 && index == 3) Color.Red else Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}