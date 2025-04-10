package com.hestabit.sparkmatch.screens.profile

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.createImageLoader
import com.hestabit.sparkmatch.Utils.hobbyOptions
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.DefaultIconButton
import com.hestabit.sparkmatch.common.OptimizedBottomSheet
import com.hestabit.sparkmatch.common.PassionSelectionButton
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.HotPinkDisabled
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.viewmodel.EditProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditProfileScreen(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    val viewModel: EditProfileViewModel = hiltViewModel()
    val userRepo = viewModel.userRepository

    val context = LocalContext.current
    val imageLoader = createImageLoader(context)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    // User profile state
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // UI state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Noida, IN") }
    var profession by remember { mutableStateOf("Professional model") }
    var about by remember { mutableStateOf("") }

    // Fetch user profile data
    LaunchedEffect(Unit) {
        isLoading = true
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val profile = userRepo.getUserProfile(currentUser.uid)
                if (profile != null) {
                    userProfile = profile
                    firstName = profile.firstName
                    lastName = profile.lastName
                    email = currentUser.email ?: ""
                    phone = currentUser.phoneNumber ?: "+91 8929652267" // Fallback

                    // Extract age from birthday if available
                    if (profile.birthday.isNotEmpty()) {
                        try {
                            val birthYear = profile.birthday.split("-")[0].toInt()
                            val currentYear = java.time.Year.now().value
                            age = (currentYear - birthYear).toString()
                        } catch (e: Exception) {
                            age = "24" // Fallback
                        }
                    } else {
                        age = "24" // Fallback
                    }

                    about = "My name is ${profile.firstName} ${profile.lastName} and I enjoy meeting new people and finding ways to help them have an uplifting experience. I enjoy reading.."
                }
            } catch (e: Exception) {
                errorMessage = "Failed to load profile: ${e.message}"
            } finally {
                isLoading = false
            }
        } else {
            errorMessage = "User not authenticated"
            isLoading = false
        }
    }

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

    var contactSyncing by remember { mutableStateOf(false) }

    var notificationSyncing by remember { mutableStateOf(false) }

    var isEditing by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    var showPassionBottomSheet by remember { mutableStateOf(false) }

    var passionsList by remember { mutableStateOf(hobbyOptions.filterIndexed { index, hobby -> hobby.isSelected }) }

    val _context = LocalContext.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = HotPink,  // Drop indicator (caret handle) color
        backgroundColor = HotPink.copy(alpha = 0.4f) // Selection highlight color
    )

    val authViewModel = hiltViewModel<AuthViewModel>()

    LaunchedEffect(userProfile) {
        if (userProfile != null) {
            // Update gender based on profile
            genderSelectedText = userProfile?.gender ?: "Male"
        }
    }

    Box(
        modifier = modifier
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
                    Text(
                        "Personal Details",
                        fontFamily = modernist,
                        fontSize = 20.sp,
                        color = Black.copy(0.7f),
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    )

                    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First Name") },
                            enabled = isEditing,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last Name") },
                            enabled = isEditing,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            enabled = isEditing,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone") },
                            enabled = isEditing,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age") },
                            enabled = isEditing,
                            singleLine = true,
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.calender),
                                    "calender icon"
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink,
                                focusedTrailingIconColor = HotPink
                            ),
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = genderExpanded,
                            onExpandedChange = { genderExpanded = !genderExpanded }
                        ) {
                            OutlinedTextField(
                                value = genderSelectedText,
                                enabled = isEditing,
                                onValueChange = { genderSelectedText = it },
                                label = { Text("Gender") },
                                singleLine = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isEditing && genderExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HotPink.copy(0.5f),
                                    cursorColor = HotPink,
                                    focusedLabelColor = HotPink,
                                    focusedTrailingIconColor = HotPink
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp)
                            )

                            ExposedDropdownMenu(
                                containerColor = White,
                                shape = RoundedCornerShape(15.dp),
                                expanded = isEditing && genderExpanded,
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
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("Location") },
                            enabled = isEditing,
                            singleLine = true,
                            shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink,
                                focusedTrailingIconColor = HotPink
                            ),
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
                                enabled = isEditing,
                                onValueChange = { interestSelectedText = it },
                                label = { Text("Interest") },
                                singleLine = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isEditing && interestExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HotPink.copy(0.5f),
                                    cursorColor = HotPink,
                                    focusedLabelColor = HotPink,
                                    focusedTrailingIconColor = HotPink
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp)
                            )

                            ExposedDropdownMenu(
                                containerColor = White,
                                shape = RoundedCornerShape(15.dp),
                                expanded = isEditing && interestExpanded,
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
                            value = profession,
                            onValueChange = { profession = it },
                            label = { Text("Profession") },
                            enabled = isEditing,
                            singleLine = true,
                            shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )

                        OutlinedTextField(
                            value = about,
                            onValueChange = { about = it },
                            label = { Text("About") },
                            enabled = isEditing,
                            shape = RoundedCornerShape(15.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HotPink.copy(0.5f),
                                cursorColor = HotPink,
                                focusedLabelColor = HotPink,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    }

                    Text(
                        "Additional Details",
                        fontFamily = modernist,
                        fontSize = 20.sp,
                        color = Black.copy(0.7f),
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        passionsList
                            .forEach {
                                PassionSelectionButton(
                                    passionList = it,
                                    selectionCount = 5,
                                    isEnabled = isEditing,
                                    isClickEnabled = false
                                ) {}
                            }

                        DefaultIconButton(
                            R.drawable.add_icon,
                            iconTint = White,
                            containerColor = if (isEditing) HotPink else HotPinkDisabled
                        ) {
                            if (isEditing) {
                                coroutineScope.launch {
                                    sheetState.show()
                                }
                                showPassionBottomSheet = true
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 20.dp, end = 40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Contact Sync",
                            fontWeight = FontWeight.W600,
                            fontFamily = modernist,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Switch(
                            checked = contactSyncing,
                            { contactSyncing = !contactSyncing },
                            enabled = isEditing,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = HotPink,
                                checkedBorderColor = HotPink,
                                uncheckedThumbColor = HotPink,
                                uncheckedBorderColor = HotPink,
                                uncheckedTrackColor = HotPink.copy(0.15f),
                                disabledUncheckedThumbColor = HotPink.copy(0.5f),
                                disabledUncheckedBorderColor = HotPink.copy(0.25f),
                                disabledCheckedThumbColor = White.copy(1f),
                                disabledCheckedTrackColor = HotPink.copy(0.5f)
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 20.dp, end = 40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Notification",
                            fontWeight = FontWeight.W600,
                            fontFamily = modernist,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Switch(
                            checked = notificationSyncing,
                            { notificationSyncing = !notificationSyncing },
                            enabled = isEditing,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = HotPink,
                                checkedBorderColor = HotPink,
                                uncheckedThumbColor = HotPink,
                                uncheckedBorderColor = HotPink,
                                uncheckedTrackColor = HotPink.copy(0.15f),
                                disabledUncheckedThumbColor = HotPink.copy(0.5f),
                                disabledUncheckedBorderColor = HotPink.copy(0.25f),
                                disabledCheckedThumbColor = White.copy(1f),
                                disabledCheckedTrackColor = HotPink.copy(0.5f)
                            )
                        )
                    }

                    DefaultButton(
                        modifier = Modifier.padding(top = 40.dp, bottom = 50.dp),
                        text = "Logout"
                    ) {
                        authViewModel.signOut()
                        onNavigate(Routes.ONBOARDING_SCREEN)
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
                if (!isEditing) R.drawable.round_arrow_back_ios_24 else R.drawable.default_close,
                White,
                modifier = Modifier.padding(start = 40.dp)
            ) {
                if (isEditing) {
                    isEditing = false
                    Toast.makeText(_context, "Discard changes", Toast.LENGTH_SHORT).show()
                } else onNavigate(Routes.POP)
            }
            DefaultIconButton(
                if (!isEditing) R.drawable.edit else R.drawable.save,
                White,
                modifier = Modifier.padding(end = 40.dp)
            ) {
                if (isEditing) {
                    // Save changes to Firebase
                    coroutineScope.launch {
                        val currentUser = auth.currentUser
                        if (currentUser != null && userProfile != null) {
                            val updatedProfile = UserProfile(
                                firstName = firstName,
                                lastName = lastName,
                                profileImage = userProfile?.profileImage,
                                birthday = userProfile?.birthday ?: "",
                                gender = genderSelectedText,
                                passions = userProfile?.passions ?: emptyList()
                            )

                            try {
                                userRepo.saveUserProfile(currentUser.uid, updatedProfile)
                                Toast.makeText(_context, "Changes saved", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(_context, "Failed to save changes: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                isEditing = !isEditing
            }
        }

        // Circular Image with scaling and movement on scroll
        AsyncImage(
            model = userProfile?.profileImage ?: ImageRequest.Builder(context)
                .data("android.resource://${context.packageName}/${R.drawable.img_2}")
                .crossfade(true)
                .build(),
            contentDescription = "Profile image",
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

        if (showPassionBottomSheet) {
            OptimizedBottomSheet(
                onDismiss = {
                    passionsList = hobbyOptions.filterIndexed { index, hobby -> hobby.isSelected }
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    showPassionBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Passions {
                    passionsList = hobbyOptions.filterIndexed { index, hobby -> hobby.isSelected }
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    showPassionBottomSheet = false
                }
            }
        }
    }
}
