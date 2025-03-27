package com.hestabit.sparkmatch.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Stories(navController: NavController) {

    val imageList = listOf(
        Filter("Jessica", R.drawable.stories_template),
        Filter("Jessica", R.drawable.stories_template_2),
        Filter("Jessica", R.drawable.stories_template),
        Filter("Jessica", R.drawable.stories_template_2),
        Filter("Jessica", R.drawable.stories_template),
        Filter("Jessica", R.drawable.stories_template_2)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        val pageCount = imageList.size
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageList.size })
        val coroutineScope = rememberCoroutineScope()

        var progress by remember { mutableFloatStateOf(0f) }
        val isPaused by remember { mutableStateOf(false) }

        LaunchedEffect(pagerState.currentPage, isPaused) {
            progress = 0f
            while (progress < 1f) {
                delay(50L)
                if (!isPaused) {
                    progress += 0.01f
                }
            }

            if (pagerState.currentPage < pageCount - 1) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            val screenWidth = size.width
                            coroutineScope.launch {
                                if (tapOffset.x < screenWidth / 2 && pagerState.currentPage > 0) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                } else if (tapOffset.x >= screenWidth / 2 && pagerState.currentPage < imageList.size - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    )
                }
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                Image(
                    painter = painterResource(id = imageList[index].imagePreview),
                    contentDescription = imageList[index].name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(40.dp)
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = HotPink,
                    trackColor = White,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jessica_main),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = imageList[pagerState.currentPage].name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}