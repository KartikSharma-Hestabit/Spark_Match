package com.hestabit.sparkmatch.screens.match

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.viewmodel.MatchViewModel

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