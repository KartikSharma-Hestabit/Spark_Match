package com.hestabit.sparkmatch.screens.profile

import android.app.LocaleConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.createImageLoader
import com.hestabit.sparkmatch.common.DefaultIconButton
import com.hestabit.sparkmatch.common.PassionSelectionButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import java.util.Arrays

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val imageLoader = createImageLoader(context)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val listState = rememberLazyListState()

    // Get the scroll offset
    val scrollOffset by remember {
        derivedStateOf { listState.firstVisibleItemScrollOffset.toFloat() }
    }

    // Calculate Scale (between 1f to 0.5f)
    val scale by remember {
        derivedStateOf { 1f - (scrollOffset / 500f) }
    }

    // Calculate Y Offset (move upwards)
    val yOffset by remember {
        derivedStateOf { -scrollOffset * 1.5f }
    }

    // Change alpha based on scroll
    val circularImageAlpha by remember {
        derivedStateOf { 1f - (scrollOffset / 300f) }
    }

    // Background Image Alpha (Fades In on Scroll Up, Reverse of Circular Image)
    val backgroundImageAlpha by remember {
        derivedStateOf { (scrollOffset / 500f) }
    }

    var genderExpanded by remember { mutableStateOf(false) }
    var genderSelectedText by remember { mutableStateOf("Male") }

    val genderOptions = listOf("Male", "Female")

    var interestExpanded by remember { mutableStateOf(false) }
    var interestSelectedText by remember { mutableStateOf("Female") }

    Box(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = listOf(HotPink, White, White))),
        contentAlignment = Alignment.TopCenter
    ) {
        // Background Image
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("android.resource://${context.packageName}/${R.drawable.img_2}")
                .crossfade(true)
                .build(),
            contentDescription = "Background image",
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 2.5f)
                .graphicsLayer(
                    alpha = backgroundImageAlpha.coerceIn(0f, 1f) // Reverse fade effect
                )
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 300.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 50.dp,
                                topEnd = 50.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                        .background(White)
                        .padding(horizontal = 40.dp)
                        .padding(top = 80.dp)

                ) {
                    OutlinedTextField(
                        value = "Karitk",
                        onValueChange = {},
                        label = { Text("First Name") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    OutlinedTextField(
                        value = "Sharma",
                        onValueChange = {},
                        label = { Text("Last Name") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    OutlinedTextField(
                        value = "Karitk@gmail.com",
                        onValueChange = {},
                        label = { Text("Email") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )


                    OutlinedTextField(
                        value = "+91 8929652267",
                        onValueChange = {},
                        label = { Text("Phone") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        trailingIcon = { Icon(Icons.Default.Phone, "") }
                    )


                    OutlinedTextField(
                        value = "24",
                        onValueChange = {},
                        label = { Text("Age") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        trailingIcon = { Icon(Icons.Default.DateRange, "Calender") }
                    )



                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = genderSelectedText,
                            enabled = false,
                            onValueChange = { genderSelectedText = it },
                            label = { Text("Gender") },
                            singleLine = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )

                        ExposedDropdownMenu(
                            containerColor = White,
                            shape = RoundedCornerShape(15.dp),
                            expanded = false,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        genderSelectedText = option
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }


                    OutlinedTextField(
                        value = "Noida, IN",
                        onValueChange = {},
                        label = { Text("Location") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        trailingIcon = { Icon(Icons.Default.LocationOn, "Location On") }
                    )


                    ExposedDropdownMenuBox(
                        expanded = interestExpanded,
                        onExpandedChange = { interestExpanded = !interestExpanded }
                    ) {
                        OutlinedTextField(
                            value = interestSelectedText,
                            enabled = false,
                            onValueChange = { interestSelectedText = it },
                            label = { Text("Interest") },
                            singleLine = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = interestExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )

                        ExposedDropdownMenu(
                            containerColor = White,
                            shape = RoundedCornerShape(15.dp),
                            expanded = false,
                            onDismissRequest = { interestExpanded = false }
                        ) {
                            (genderOptions + listOf("Both")).forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        interestSelectedText = option
                                        interestExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = "Professional model",
                        onValueChange = {},
                        label = { Text("Profession") },
                        enabled = false,
                        singleLine = true,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                    OutlinedTextField(
                        value = "My name is Jessica Parker and I enjoy meeting new people and finding ways to help them have an uplifting experience. I enjoy reading..",
                        onValueChange = {},
                        label = { Text("About") },
                        enabled = false,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )

                }

                FlowRow {
                    repeat(5) {
//                        PassionSelectionButton(options[it], true) { }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
                .graphicsLayer(
                    translationY = 2 * yOffset.coerceIn(-400f, 0f), // Move up but limit the range
                    alpha = circularImageAlpha.coerceIn(0f, 1f) // Fade effect
                ), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefaultIconButton(
                R.drawable.round_arrow_back_ios_24,
                White,
                modifier = Modifier.padding(start = 40.dp)
            )
            DefaultIconButton(R.drawable.edit, White, modifier = Modifier.padding(end = 40.dp))
        }

        // Circular Image with scaling and movement on scroll
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("android.resource://${context.packageName}/${R.drawable.img_2}")
                .crossfade(true)
                .build(),
            contentDescription = "circular image",
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 200.dp)
                .graphicsLayer(
                    scaleX = scale.coerceIn(0.5f, 1f), // Prevent excessive shrinking
                    scaleY = scale.coerceIn(0.5f, 1f),
                    translationY = yOffset.coerceIn(-400f, 0f), // Move up but limit the range
                    alpha = circularImageAlpha.coerceIn(0f, 1f) // Fade effect
                )
                .size(150.dp)
                .clip(CircleShape)

        )
    }
}

