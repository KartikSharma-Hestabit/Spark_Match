package com.hestabit.sparkmatch.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hestabit.sparkmatch.R
import kotlin.math.absoluteValue

@Composable
fun HorizontalPagerWithGateTransition(modifier: Modifier = Modifier) {
    val imageList = listOf(
        R.drawable.jessica_main,
        R.drawable.jessica_1,
        R.drawable.jessica_2,
        R.drawable.jessica_3,
        R.drawable.jessica_4,
        R.drawable.jessica_5,
    )
    val pagerState = rememberPagerState(pageCount = { imageList.size })
    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState,
        beyondViewportPageCount = 2
    ) { page ->
        Box(
            Modifier
                .pagerGateTransition(page, pagerState)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(imageList[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
            )
        }
    }
}

fun Modifier.pagerGateTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset = pagerState.getOffsetDistanceInPages(page)
    translationX = pageOffset * 0

    if (pageOffset < -1f) {
        alpha = 0f
    } else if (pageOffset <= 0) {
        alpha = 1f
        transformOrigin = TransformOrigin(1f, 0.5f)
        rotationY = -45f * pageOffset.absoluteValue
    } else if (pageOffset <= 1) {
        alpha = 1f
        transformOrigin = TransformOrigin(0f, 0.5f)
        rotationY = 45f * pageOffset.absoluteValue
    } else {
        alpha = 0f
    }
}