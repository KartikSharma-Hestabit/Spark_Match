package com.hestabit.sparkmatch.screens.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.auth.User
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import com.hestabit.sparkmatch.utils.Utils.getAgeFromBirthday
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.MatchViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchScreen(onNavigate: (String, UserProfile?, String?) -> Unit) {


//    val cardDataList = listOf(
//        UserProfile(firstName = "Jessica", lastName = "Parker", birthday = "1998-05-15"),
//        UserProfile(firstName = "Emma", lastName = "Johnson", birthday = "1997-07-22"),
//        UserProfile(firstName = "Sophia", lastName = "Williams", birthday = "1999-03-11"),
//        UserProfile(firstName = "Olivia", lastName = "Smith", birthday = "1996-11-30"),
//        UserProfile(firstName = "Ava", lastName = "Brown", birthday = "2000-01-25"),
//        UserProfile(firstName = "Isabella", lastName = "Taylor", birthday = "1995-09-18"),
//        UserProfile(firstName = "Mia", lastName = "Anderson", birthday = "1998-12-03"),
//        UserProfile(firstName = "Charlotte", lastName = "Thomas", birthday = "1997-04-07"),
//        UserProfile(firstName = "Amelia", lastName = "Jackson", birthday = "1999-08-14"),
//        UserProfile(firstName = "Harper", lastName = "White", birthday = "1996-06-29"),
//        UserProfile(firstName = "Evelyn", lastName = "Harris", birthday = "2000-02-12"),
//        UserProfile(firstName = "Abigail", lastName = "Martin", birthday = "1995-10-05")
//    )

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
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

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            Text(
                "Likes",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            )

            LikedList()

            Text(
                "Matches",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            )

            MatchingCardList(onNavigate = onNavigate)
        }

    }
}

@Composable
fun LikedList() {

    val viewModel: MatchViewModel = hiltViewModel()

    val likedList = viewModel.likedByList.collectAsState()

    val context = LocalContext.current

    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(likedList.value) { user ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(user.profileImageUrl)
                    .transformations()
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xff8A2387),
                                Color(0xffE94057),
                                Color(0xffF27121)
                            ),
                            start = Offset(Float.POSITIVE_INFINITY, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        ),
                        CircleShape
                    )
                    .blur(2.dp),
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.Low
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchingCardList(
    onNavigate: (String, UserProfile?, String?) -> Unit
) {
    val viewModel: MatchViewModel = hiltViewModel()

    val matchs = viewModel.matchList.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(matchs.value) { user ->
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

    ElevatedCard(
        onClick = {
            // Use userId approach for navigation
            onNavigate(Routes.PROFILE, null, "")
        },
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(scaleFactor.value),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("android.resource://${context.packageName}/${R.drawable.img_4}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                imageLoader = (imageLoader),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(5.dp),
            )


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