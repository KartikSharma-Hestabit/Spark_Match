package com.hestabit.sparkmatch.common

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hestabit.sparkmatch.R

@Composable
fun ProfileImagePicker(
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = if (imageUri != null) rememberAsyncImagePainter(imageUri)
            else painterResource(id = R.drawable.baseline_insert_photo_24),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(0.5.dp, Color(0xFF525252), RoundedCornerShape(24.dp))
        )

        IconButton(
            onClick = onImageClick,
            modifier = Modifier
                .size(32.dp)
                .background(Color.Red, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.camera),
                contentDescription = "Pick Image",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}