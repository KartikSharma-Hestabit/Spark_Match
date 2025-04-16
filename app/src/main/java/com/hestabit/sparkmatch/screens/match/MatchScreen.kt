package com.hestabit.sparkmatch.screens.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import com.hestabit.sparkmatch.utils.Utils.getAgeFromBirthday
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchScreen(onNavigate: (String, UserProfile?, String?) -> Unit) {

    val cardDataList = listOf(
        UserProfile(firstName = "Jessica", lastName = "Parker", birthday = "1998-05-15"),
        UserProfile(firstName = "Emma", lastName = "Johnson", birthday = "1997-07-22"),
        UserProfile(firstName = "Sophia", lastName = "Williams", birthday = "1999-03-11"),
        UserProfile(firstName = "Olivia", lastName = "Smith", birthday = "1996-11-30"),
        UserProfile(firstName = "Ava", lastName = "Brown", birthday = "2000-01-25"),
        UserProfile(firstName = "Isabella", lastName = "Taylor", birthday = "1995-09-18"),
        UserProfile(firstName = "Mia", lastName = "Anderson", birthday = "1998-12-03"),
        UserProfile(firstName = "Charlotte", lastName = "Thomas", birthday = "1997-04-07"),
        UserProfile(firstName = "Amelia", lastName = "Jackson", birthday = "1999-08-14"),
        UserProfile(firstName = "Harper", lastName = "White", birthday = "1996-06-29"),
        UserProfile(firstName = "Evelyn", lastName = "Harris", birthday = "2000-02-12"),
        UserProfile(firstName = "Abigail", lastName = "Martin", birthday = "1995-10-05")
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 30.dp)
    ) {
        Text(
            "This is a list of people who have liked you and your matches.",
            fontFamily = modernist,
            fontSize = 16.sp
        )
        MatchingCardList(cards = cardDataList, onNavigate = onNavigate)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchingCardList(cards: List<UserProfile>, onNavigate: (String, UserProfile?, String?) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(cards.size) { index ->
            MatchingCard(
                cardData = cards[index],
                modifier = Modifier
                    .height(200.dp)
                    .padding(8.dp),
                onNavigate = onNavigate
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchingCard(cardData: UserProfile, modifier: Modifier, onNavigate: (String, UserProfile?, String?) -> Unit) {

    val scaleFactor = remember { Animatable(1f) }
    val context = LocalContext.current
    val imageLoader = createImageLoader(context)

    ElevatedCard(
        onClick = {
            // Use userId approach for navigation
            val userId = "${cardData.firstName}_${cardData.lastName}"
            onNavigate(Routes.PROFILE, null, userId)
        },
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleFactor.value),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {

            // Display profile image if available, otherwise use a placeholder
            if (cardData.profileImageUrl != null && cardData.profileImageUrl.isNotEmpty()) {
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
                        .data("android.resource://${context.packageName}/${R.drawable.img_4}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    imageLoader = (imageLoader),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
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
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.5.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
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
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 0.5.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    )
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.5f)
                                    ),
                                    100.0f, 0.0f
                                )
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.profile_like),
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