package com.hestabit.sparkmatch.screens.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import com.hestabit.sparkmatch.utils.Utils.getAgeFromBirthday
import com.hestabit.sparkmatch.viewmodel.MatchViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchingCard(
    cardData: MatchUser,
    modifier: Modifier,
    onNavigate: (String, UserProfile?, String?) -> Unit
) {
    val scaleFactor = remember { Animatable(1f) }
    val context = LocalContext.current
    val imageLoader = createImageLoader(context)
    val viewModel: MatchViewModel = hiltViewModel()

    ElevatedCard(
        onClick = {
            onNavigate(Routes.PROFILE, null, "")
        },
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleFactor.value),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
            if (cardData.profileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(cardData.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("android.resource://${context.packageName}/${R.drawable.spark_match_logo}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .fillMaxSize(),
                )
            }

            Column {
                Text(
                    "${cardData.firstName}, ${getAgeFromBirthday(cardData.birthday)}",
                    fontFamily = modernist,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    color = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 4.dp)
                )

                Row {
                    Button(
                        onClick = {
                            viewModel.removeLikedUser(cardData.uid)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.5.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    ),
                                    100.0f, 0.0f
                                )
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            tint = White,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 0.5.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.2f)
                                    ),
                                    100.0f, 0.0f
                                )
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.messages),
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(R.drawable.profile_like),
                contentDescription = "",
                tint = HotPink,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(color = White)
                    .rotate(12f)
                    .size(40.dp)
                    .padding(10.dp)
            )
        }
    }
}