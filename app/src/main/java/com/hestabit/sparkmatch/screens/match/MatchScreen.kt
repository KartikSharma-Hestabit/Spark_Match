package com.hestabit.sparkmatch.screens.match

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendModeColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.screens.discover.CardData
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MatchScreen(onNavigate: (String, CardData) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {
        Text(
            "This is a list of people who have liked you and your matches.",
            fontFamily = modernist,
            fontSize = 16.sp
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),// Fix the height
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(vertical = 25.dp)
        ) {


            items(10) { index ->
                MatchingCard(
                    CardData(
                        1,
                        "luis philip",
                        20,
                        "Fashion Designer",
                        R.drawable.img_1,
                        12
                    ), modifier = Modifier.height(200.dp),
                    onNavigate = onNavigate
                )
            }

        }
    }
}

@Composable
fun MatchingCard(cardData: CardData, modifier: Modifier, onNavigate: (String, CardData) -> Unit) {

    var scaleFactor = remember { Animatable(1f) }

    ElevatedCard(
        onClick = {
            onNavigate(Routes.PROFILE, cardData)
        },
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxSize()
            .scale(scaleFactor.value),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {

            Image(
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                painter = painterResource(cardData.imageRes),
                contentScale = ContentScale.Crop
            )

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
                                        Color.Black.copy(alpha = 0.5f)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CardPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MatchingCard(
            CardData(
                1,
                "luis philip",
                20,
                "Fashion Designer",
                R.drawable.img_1,
                12
            ), modifier = Modifier
                .height(200.dp)
                .width(150.dp),
            onNavigate = { _, _ -> }
        )
    }
}
