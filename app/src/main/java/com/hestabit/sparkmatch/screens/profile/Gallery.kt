package com.hestabit.sparkmatch.screens.profile

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.NetworkImage
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun Gallery(navController: NavController, userProfile: UserProfile? = null) {
    val galleryImages = userProfile?.galleryImages ?: emptyList()

    // Handle the case when there are no images
    if (galleryImages.isEmpty()) {
        EmptyGalleryView(navController)
        return
    }

    val pageCount = galleryImages.size
    val loopingCount = pageCount * 100
    val startIndex = loopingCount / 2

    val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { loopingCount })
    val thumbnailPagerState = rememberPagerState(pageCount = { pageCount })

    val coroutineScope = rememberCoroutineScope()

    val currentMappedPage by remember {
        derivedStateOf {
            pageMapper(pagerState.currentPage, startIndex, pageCount)
        }
    }

    LaunchedEffect(currentMappedPage) {
        if (thumbnailPagerState.currentPage != currentMappedPage) {
            thumbnailPagerState.scrollToPage(currentMappedPage)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 40.dp)
            ) {
                BackButton(navController, HotPink)

                Text(
                    text = "Gallery",
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main image pager taking most of the screen
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    key = { index -> index }
                ) { index ->
                    val page = pageMapper(index, startIndex, pageCount)
                    // Use NetworkImage to display images from Firestore URLs
                    NetworkImage(
                        url = galleryImages[page],
                        contentDescription = "Gallery image ${page + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(90.dp)
            ) {
                CenterSnapPager(
                    galleryImages = galleryImages,
                    pagerState = pagerState,
                    thumbnailPagerState = thumbnailPagerState,
                    startIndex = startIndex,
                    pageCount = pageCount,
                    onPageSelected = { page ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(startIndex + page)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyGalleryView(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 40.dp)
            ) {
                BackButton(navController, HotPink)

                Text(
                    text = "Gallery",
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_insert_photo_24),
                    contentDescription = "No Photos",
                    tint = Color.Gray,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "No photos available",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CenterSnapPager(
    galleryImages: List<String>,
    pagerState: PagerState,
    thumbnailPagerState: PagerState,
    startIndex: Int,
    pageCount: Int,
    onPageSelected: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Sync main pager when thumbnail pager scrolls
    LaunchedEffect(thumbnailPagerState.currentPage, thumbnailPagerState.currentPageOffsetFraction) {
        val mappedPage = startIndex + thumbnailPagerState.currentPage
        // Only trigger if there's a meaningful difference to avoid ping-pong effects
        val currentMappedPage = pageMapper(pagerState.currentPage, startIndex, pageCount)
        if (currentMappedPage != thumbnailPagerState.currentPage) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(mappedPage)
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val pageSize = 70.dp

        HorizontalPager(
            state = thumbnailPagerState,
            pageSize = PageSize.Fixed(pageSize),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            contentPadding = PaddingValues(
                start = (maxWidth - pageSize) / 2,
                end = (maxWidth - pageSize) / 2
            ),
            flingBehavior = PagerDefaults.flingBehavior(
                state = thumbnailPagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(30),
                snapAnimationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ),
            key = { page -> page }
        ) { page ->
            ThumbnailItem(
                imageUrl = galleryImages[page],
                pagerState = thumbnailPagerState,
                page = page,
                onPageSelected = { onPageSelected(page) }
            )
        }
    }
}

@Composable
fun ThumbnailItem(
    imageUrl: String,
    pagerState: PagerState,
    page: Int,
    onPageSelected: () -> Unit,
) {
    // Calculate transformation values once and reuse
    val pageOffset = remember(pagerState.currentPage, pagerState.currentPageOffsetFraction, page) {
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue.coerceIn(0f, 1f)
    }

    val scale = remember(pageOffset) {
        lerp(start = 0.85f, stop = 1f, fraction = 1f - pageOffset)
    }

    val alpha = remember(pageOffset) {
        lerp(start = 0.25f, stop = 1f, fraction = 1f - pageOffset)
    }

    val isSelected = pagerState.currentPage == page

    Column(modifier = Modifier
        .clickable { onPageSelected() }
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            this.alpha = alpha
        }
    ) {
        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(White)
                )
            }

            // Use NetworkImage for thumbnails
            NetworkImage(
                url = imageUrl,
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        Text(
            "Image ${page + 1}",
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Extract function for reuse
private fun pageMapper(index: Int, startIndex: Int, pageCount: Int): Int {
    return (index - startIndex).floorMod(pageCount)
}

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}