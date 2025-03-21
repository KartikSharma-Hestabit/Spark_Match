package com.hestabit.sparkmatch.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hestabit.sparkmatch.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

@Composable
fun HorizontalPagerLoopingIndicatorSample() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val imageList = listOf(
                R.drawable.jessica_main,
                R.drawable.jessica_1,
                R.drawable.jessica_2,
                R.drawable.jessica_3,
                R.drawable.jessica_4,
                R.drawable.jessica_5,
            )
            // Display 10 items
            val pageCount = imageList.size

            // We start the pager in the middle of the raw number of pages
            val loopingCount = Int.MAX_VALUE
            val startIndex = loopingCount / 2
            // Set the raw page count to a really large number
            val pagerState = rememberPagerState(initialPage = startIndex, pageCount = {loopingCount})

            fun pageMapper(index: Int): Int {
                return (index - startIndex).floorMod(pageCount)
            }

            HorizontalPager(
                state = pagerState,
                // Add some horizontal spacing between items
                pageSpacing = 4.dp,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                // We calculate the page from the given index
                val page = pageMapper(index)
                Image(
                    painter = painterResource(imageList[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp)),
                )
            }

            val loopState = remember {
                mutableStateOf(true)
            }


            var underDragging by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = Unit) {
                pagerState.interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> underDragging = true
                        is PressInteraction.Release -> underDragging = false
                        is PressInteraction.Cancel -> underDragging = false
                        is DragInteraction.Start -> underDragging = true
                        is DragInteraction.Stop -> underDragging = false
                        is DragInteraction.Cancel -> underDragging = false
                    }
                }
            }

            val looping = loopState.value
            if (underDragging.not() && looping) {
                LaunchedEffect(key1 = underDragging) {
                    try {
                        while (true) {
                            delay(1000L)
                            val current = pagerState.currentPage
                            val currentPos = pageMapper(current)
                            val nextPage = current + 1
                            if (underDragging.not()) {
                                val toPage = nextPage.takeIf { nextPage < pageCount } ?: (currentPos + startIndex + 1)
                                if (toPage > current) {
                                    pagerState.animateScrollToPage(toPage)
                                } else {
                                    pagerState.scrollToPage(toPage)
                                }
                            }
                        }
                    } catch (e: CancellationException) {
                        Log.i("page", "Launched paging cancelled")
                    }
                }
            }
        }
    }
}


private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}