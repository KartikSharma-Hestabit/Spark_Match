package com.hestabit.sparkmatch.screens.onboard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.utils.printDebug
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen(onNavigate: (route: String) -> Unit) {

    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }
    val viewModel: OnboardingViewModel = hiltViewModel()

    val pageData = viewModel.onboardingData()
    val pageCount = pageData.size

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

                val actualPage = page % pageCount

                ElevatedCard(modifier = Modifier.graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    val scale = lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    scaleX = scale
                    scaleY = scale

                }) {
                    Image(
                        painter = painterResource(pageData[actualPage].img),
                        contentDescription = "Page $actualPage",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            val currentPage = pageData[pagerState.currentPage % pageCount]

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 70.dp)
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                Text(currentPage.title, color = Red, fontWeight = FontWeight.Bold, fontSize = 24.sp)

                Text(
                    currentPage.description,
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