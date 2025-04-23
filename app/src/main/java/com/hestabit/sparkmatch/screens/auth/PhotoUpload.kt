package com.hestabit.sparkmatch.screens.auth

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PhotoUpload(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel: ProfileDetailsViewModel = hiltViewModel()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val selectedImages = remember { mutableStateListOf<Uri>() }
    var isUploading by remember { mutableStateOf(false) }
    val uploadProgress by viewModel.uploadProgress.collectAsState()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(6),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                val availableSlots = 6 - selectedImages.size
                if (availableSlots > 0) {
                    val newImages = uris.take(availableSlots)
                    selectedImages.addAll(newImages)
                }
            }
        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                if (selectedImages.size < 6) {
                    selectedImages.add(it)
                }
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(40.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add your photos",
            textAlign = TextAlign.Start,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add at least 2 photos to continue. High-quality photos increase your chances of finding a match!",
            textAlign = TextAlign.Start,
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Photo grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(300.dp),
            userScrollEnabled = false
        ) {
            // Add selected images
            items(selectedImages) { uri ->
                PhotoItem(
                    uri = uri,
                    onDelete = {
                        selectedImages.remove(uri)
                    }
                )
            }

            // Add upload button if less than 6 photos
            if (selectedImages.size < 6) {
                item {
                    AddPhotoButton(
                        onClick = {
                            if (selectedImages.size < 6) {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        }
                    )
                }
            }

            // Add placeholder items to maintain grid
            val placeholdersNeeded = (3 - (selectedImages.size + 1) % 3) % 3
            repeat(placeholdersNeeded) {
                if (selectedImages.size < 6) {
                    item {
                        Box(modifier = Modifier.size(100.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add multiple photos button
        Button(
            onClick = {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x1AE94057),
                contentColor = HotPink
            ),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add Multiple Photos",
                    tint = HotPink,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add multiple photos",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Upload progress indicator
        if (isUploading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                CircularProgressIndicator(
                    progress = { uploadProgress / 100f },
                    color = HotPink,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Uploading photos... $uploadProgress%",
                    fontFamily = modernist,
                    fontSize = 14.sp,
                    color = HotPink
                )
            }
        }

        // Continue button
        DefaultButton(
            text = "Continue",
            onClick = {
                if (selectedImages.size >= 2) {
                    isUploading = true
                    viewModel.uploadGalleryImages(
                        selectedImages.toList(),
                        onComplete = { success, message ->
                            if (success) {
                                onNavigate(AuthRoute.Passions.route)
                            } else {
                                Toast.makeText(context, message ?: "Upload failed", Toast.LENGTH_SHORT).show()
                                isUploading = false
                            }
                        }
                    )
                } else {
                    Toast.makeText(context, "Please add at least 2 photos", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = selectedImages.size >= 2 && !isUploading
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PhotoItem(uri: Uri, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, OffWhite, RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Selected photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Delete button
        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(28.dp)
                .padding(4.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete photo",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AddPhotoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, OffWhite, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add photo",
                tint = HotPink,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Add Photo",
                fontFamily = modernist,
                fontSize = 12.sp,
                color = HotPink
            )
        }
    }
}