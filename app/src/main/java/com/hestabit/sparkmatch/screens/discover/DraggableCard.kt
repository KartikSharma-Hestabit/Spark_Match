package com.hestabit.sparkmatch.screens.discover

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.createImageLoader
import com.hestabit.sparkmatch.Utils.getAgeFromBirthday
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.data.CardData
import com.hestabit.sparkmatch.data.SwipeDirection
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.DiscoverViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun DraggableCard(
    userProfile: UserProfile,
    onSwiped: (direction: SwipeDirection) -> Unit,
    modifier: Modifier = Modifier,
    imageAlpha: Float = 1f,
    isTopCard: Boolean = false,
    onNavigate: (String, UserProfile) -> Unit
) {

    val cardOffsetX = remember { Animatable(0f) }
    val cardOffsetY = remember { Animatable(0f) }
    val rotationAngle = remember { derivedStateOf { (cardOffsetX.value / 75).coerceIn(-25f, 25f) } }

    val scope = rememberCoroutineScope()

    var likeIconId by remember { mutableStateOf(0) }
    var dislikeIconId by remember { mutableStateOf(0) }
    var starIconId by remember { mutableStateOf(0) }

    val swipeThreshold = 300f // Distance to remove the card
    val swipeUpThreshold = -1000f // Negative for upward swipe
    val iconThreshold = 100f

    val viewModel: DiscoverViewModel = hiltViewModel()

    val triggerSwipe by viewModel.moveCard.collectAsState()

    LaunchedEffect(triggerSwipe) {
        triggerSwipe?.let { direction ->
            if (isTopCard) {
                when (direction) {
                    SwipeDirection.Right -> {
                        likeIconId = R.drawable.like
                        cardOffsetX.animateTo(1500f, tween(550, easing = EaseInOutQuad))
                    }

                    SwipeDirection.Left -> {
                        likeIconId = R.drawable.dislike
                        cardOffsetX.animateTo(-1500f, tween(550, easing = EaseInOutQuad))
                    }

                    SwipeDirection.Up -> {
                        likeIconId = R.drawable.star
                        cardOffsetY.animateTo(-2500f, tween(550, easing = EaseInOutQuad))
                    }
                }
                onSwiped(direction)
                viewModel.moveCard(null) // Reset trigger
            }
        }
    }

    Box(
        modifier = if (isTopCard) modifier
            .offset { IntOffset(cardOffsetX.value.roundToInt(), cardOffsetY.value.roundToInt()) }
            .graphicsLayer(rotationZ = rotationAngle.value)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            cardOffsetX.value > swipeThreshold -> {
                                scope.launch {
                                    cardOffsetX.animateTo(1500f, tween(200, easing = EaseInOutQuad))
                                    onSwiped(SwipeDirection.Right)
                                }
                            }

                            cardOffsetX.value < -swipeThreshold -> {
                                scope.launch {
                                    cardOffsetX.animateTo(
                                        -1500f,
                                        tween(200, easing = EaseInOutQuad)
                                    )
                                    onSwiped(SwipeDirection.Left)
                                }
                            }

                            cardOffsetY.value < swipeUpThreshold -> { // Swipe up condition
                                scope.launch {
                                    cardOffsetY.animateTo(
                                        -2500f,
                                        tween(200, easing = EaseInOutQuad)
                                    )
                                    onSwiped(SwipeDirection.Up)
                                }
                            }

                            else -> {
                                printDebug("No action performed")
                                likeIconId = 0
                                dislikeIconId = 0
                                starIconId = 0
                                scope.launch {
                                    cardOffsetX.animateTo(0f, tween(200, easing = EaseInOutQuad))
                                    cardOffsetY.animateTo(0f, tween(200, easing = EaseInOutQuad))
                                }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (cardOffsetX.value > iconThreshold) {
                            printDebug("Like Icon")
                            likeIconId = R.drawable.like
                        } else if (cardOffsetX.value < iconThreshold) {
                            printDebug("Like Icon")
                            likeIconId = 0
                        }

                        if (cardOffsetX.value < -iconThreshold) {
                            printDebug("Dislike Icon")
                            dislikeIconId = R.drawable.dislike
                        } else if (cardOffsetX.value > -iconThreshold) {
                            printDebug("Dislike Icon")
                            dislikeIconId = 0
                        }

                        if (cardOffsetY.value < -iconThreshold) {
                            printDebug("Supper Like Icon")
                            starIconId = R.drawable.star
                        } else if (cardOffsetY.value > -iconThreshold) {
                            printDebug("Supper Like Icon")
                            starIconId = 0
                        }

                        scope.launch {
                            cardOffsetX.snapTo(cardOffsetX.value + dragAmount.x)
                            cardOffsetY.snapTo(cardOffsetY.value + dragAmount.y)
                        }
                    }
                )
            } else modifier
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = true, onClick = { onNavigate(Routes.PROFILE, userProfile) })
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                val context = LocalContext.current
                val imageLoader = createImageLoader(context)

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/${R.drawable.img_2}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = imageAlpha
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 20.dp, start = 16.dp)
                        .clip(
                            RoundedCornerShape(7.dp)
                        )
                        .background(color = Color.White.copy(alpha = 0.15f))
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile_location),
                        contentDescription = "Location Icon",
                        tint = White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "1 km",
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = White
                    )
                }

                // Vertical Indicator Dots (Right Side)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .background(color = Color.White.copy(0.15f))
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .alpha(if (index == 0) 1f else 0.5f)
                                .background(
                                    Color.White,
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                            )
                        )
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.2f)),
                                100.0f, 0.0f
                            )
                        )
                        .padding(horizontal = 16.dp)
                        .padding(top = 6.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "${userProfile.firstName}, ${getAgeFromBirthday(userProfile.birthday)}",
                        color = Color.White,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = userProfile.profession,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Normal,
                    )
                }


                if (likeIconId != 0) {
                    Icon(
                        painter = painterResource(likeIconId),
                        tint = Color.Unspecified,
                        contentDescription = "",
                    )
                }

                if (dislikeIconId != 0) {
                    Icon(
                        painter = painterResource(dislikeIconId),
                        tint = Color.Unspecified,
                        contentDescription = "",
                    )
                }
                if (starIconId != 0) {
                    Icon(
                        painter = painterResource(starIconId),
                        tint = Color.Unspecified,
                        contentDescription = "",
                    )
                }
            }
        }

    }
}
