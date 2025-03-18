package com.hestabit.sparkmatch.screens.Onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.hestabit.sparkmatch.Components.DefaultButton
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Router.Routes
import com.hestabit.sparkmatch.utils.printDebug
import org.jetbrains.annotations.Async
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen(onNavigate: (route: String) -> Unit) {

    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }

    printDebug("recompiled outer")

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HorizontalPager(
                state = pagerState,
                pageSpacing = 5.dp,
                contentPadding = PaddingValues(horizontal = 70.dp),
                modifier = Modifier.weight(1f)
            ) { page ->

                val actualPage = page % 3 // Only 3 pages

                ElevatedCard(modifier = Modifier.graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    // Scale effect: Reduce size for side pages
                    val scale = lerp(
                        start = 0.85f, // Minimum scale for side pages
                        stop = 1f,     // Full size for the current page
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    scaleX = scale
                    scaleY = scale

                }) {
                    Image(
                        painter = when (actualPage) {
                            0 -> painterResource(R.drawable.img_1)
                            1 -> painterResource(R.drawable.img_2)
                            else -> painterResource(R.drawable.img_3)
                        },
                        contentDescription = "Page $actualPage",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 70.dp)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Text("Algorithm", color = Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)

                Text(
                    "Users going through a vetting process to ensure you never match with bots.",
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center
                )

                PagerIndicator(3, pagerState, modifier = Modifier.padding(top = 25.dp))

                DefaultButton(
                    modifier = Modifier.padding(top = 25.dp),
                    text = "Create an account"
                ) { onNavigate(Routes.AUTH_SCREEN) }

                Row {
                    Text("Already have an account?", fontSize = 14.sp, fontWeight = FontWeight.W400)
                    Text(
                        " Sign In",
                        color = Red,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onNavigate(Routes.AUTH_SCREEN) })
                }

            }

        }
    }

}

@Composable
fun PagerIndicator(
    size: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {

    printDebug("recompiled ")

    var currentPage = pagerState.currentPage % 3

    val selectedIcon = animateDpAsState(targetValue = (currentPage * 17).dp, label = "")

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Row(
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .padding(start = 6.dp)
        ) {
            Spacer(modifier = Modifier.width(selectedIcon.value))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Red)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(0.15f)) {
            repeat(size - 1) { index ->
                val width by animateDpAsState(
                    targetValue = if (currentPage == index) 22.dp else 8.dp,
                    label = ""
                )
                Spacer(modifier = Modifier.width(width))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(LightGray)
                )
            }
        }
    }
}