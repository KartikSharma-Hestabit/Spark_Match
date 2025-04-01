package com.hestabit.sparkmatch.screens.match

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.createImageLoader
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.data.CardData
import com.hestabit.sparkmatch.data.cardDataList
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun MatchScreen(onNavigate: (String, CardData) -> Unit) {
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

@Composable
fun MatchingCardList(cards: List<CardData>, onNavigate: (String, CardData) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(12) { index ->
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

@Composable
fun MatchingCard(cardData: CardData, modifier: Modifier, onNavigate: (String, CardData) -> Unit) {

    val scaleFactor = remember { Animatable(1f) }
    val context = LocalContext.current
    val imageLoader = createImageLoader(context)


    ElevatedCard(
        onClick = {
            onNavigate(Routes.PROFILE, cardData)
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
                    .data("android.resource://${context.packageName}/${cardData.imageRes}")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                imageLoader = (imageLoader),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

//            Image(
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = "",
//                painter = painterResource(cardData.imageRes),
//                contentScale = ContentScale.Crop
//            )

            Column {
                Text(
                    "${cardData.name}, ${cardData.age}",
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
