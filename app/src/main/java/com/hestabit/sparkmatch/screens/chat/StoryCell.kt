package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.data.Story
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun StoryList(stories: List<Story>, onNavigate: (String) -> Unit) {
    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stories.size) { index ->
            StoryCell(story = stories[index], onNavigate = onNavigate)
        }
    }
}

@Composable
fun StoryCell(story: Story, onNavigate: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        if (story.story){
            Image(
                painter = painterResource(story.imageRes),
                contentDescription = story.name,
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        2.dp,
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
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable { onNavigate(Routes.STORIES) },
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(story.imageRes),
                contentDescription = story.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable { onNavigate(Routes.STORIES) },
                contentScale = ContentScale.Crop
            )
        }

        Text(story.name, fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 14.sp)
    }
}