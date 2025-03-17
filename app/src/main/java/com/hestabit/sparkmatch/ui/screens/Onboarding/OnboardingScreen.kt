package com.hestabit.sparkmatch.ui.screens.Onboarding

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen() {

    val pagerState = rememberPagerState(initialPage = Int.MAX_VALUE / 2) { Int.MAX_VALUE }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            pageSpacing = 5.dp,
            contentPadding = PaddingValues(70.dp),
            modifier = Modifier.padding(top = 50.dp).weight(1f)
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

//                // We animate the alpha, between 50% and 100%
//                alpha = lerp(
//                    start = 0.5f,
//                    stop = 1f,
//                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
//                )

            }.weight(1f)) {
                AsyncImage(
                    model = when (actualPage) {
                        0 -> "https://s3-alpha-sig.figma.com/img/1fdb/de76/51f07ad5f337cca883a0fd75e816082b?Expires=1743379200&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=m-sYtP7TpaJbnYI63SbIgpxkrYLj7L-u4m5Hu8uEoDaxQ4WUWXCdZIKrHLivv44xHbJuxZYPw5p~eevsxnl5I3pC5ycAIlHnmBDU3FK16uGanrKTQVAC91400NgPSVUwzrkdZVZFwwHTTh3bLL-2u4Vdfo4HsJt-YTIMKwbW4np5tT-1Es0sOv5gXVUNdngAHytwfNgnNOp64I~Yba18-jO7yo3LwuAF6LgRi8EitGQ8arxQWwnTa1b08gIP3AGAs7Y~pGD56jYNPjJH66Rkx9gagQRCtK4ojF03Nl8AMmzHj1bocYx20GCJnrEEKu2Jcy9l~xDlRYJ8iHfNOgZzxQ__"
                        1 -> "https://s3-alpha-sig.figma.com/img/1943/faa0/2496076cb075871aae65e1767b206779?Expires=1743379200&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=Or2z~Wm7hI3v74c7MfO9XNeLQZ4jeatLUBrOnkA7b1~JZ~KcdXVwlR3H~JnXWNyHukxWL~UN1xLkoNfWpPvXpwhgQeLKJESY6foevoUtdaDcFtNqpAb31xdbYURaiPXRdaRia7wiCEAhPqfMUg7W4KNXeatPpjFqjLvG8Pq0r8WgPf3xzyC8Y886ko3udC9IylfrI4POu33cWWssbrPgICnJMKQ1SSt7u2qP06i2DpgS8FHBHIbY5LcsGFicpXxDvHPWRrndGJLvApE6n8Sgm5ErnyWsLzvEWLJiq5ETTkbC4071G3x6xV4nQgPGC9G1UJdzgPyys8OkRpETi9joSg__"
                        else -> "https://s3-alpha-sig.figma.com/img/ebc6/e431/4988bca6ae0d9adffd4a515ca1b1afc6?Expires=1743379200&Key-Pair-Id=APKAQ4GOSFWCW27IBOMQ&Signature=QbKQXSIdkm~AVbhy0xGlEqvRvTNZ9jme6t~QaHfRySSfWIGPnDHAQFZ11t7ZilEf9KF1D70l2MQnxtxifFVAIWa6UO7d7uomo~eNBx-RUA36z3rPJGc8yyUxSGRZ98-3~EvGCoRXIs6YS~gtkNACNJeBh60RkKv2MrPvFxUMSik6T5UyGrhueEs1VX2Utm~Bh-3tZglladDux68xn~5ggWqYVebR-05dvG3qDt1EF0f3nLyTSHfSQUXjSp8fTgomh8fe4CMHKFHQracfxwoc~-YEV9xWKanigT~u-w7QrV3agTHtRTfYF81W-Ed8wmUdwMH6g-3KcULGghHpy1ivAg__"
                    },
                    contentDescription = "Page $actualPage",
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {  }

    }

}