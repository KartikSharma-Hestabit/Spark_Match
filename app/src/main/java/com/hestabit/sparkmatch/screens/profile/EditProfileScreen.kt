package com.hestabit.sparkmatch.screens.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.DefaultIconButton
import com.hestabit.sparkmatch.common.NetworkImage
import com.hestabit.sparkmatch.common.OptimizedBottomSheet
import com.hestabit.sparkmatch.common.PassionSelectionButton
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.HotPinkDisabled
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.utils.Utils.createImageLoader
import com.hestabit.sparkmatch.utils.Utils.getAgeFromBirthday
import com.hestabit.sparkmatch.utils.Utils.hobbyOptions
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    val viewModel: ProfileDetailsViewModel = hiltViewModel()
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

    // Image state from ViewModel
    val profileImage by viewModel.profileImage.collectAsState()
    val isUploadingImage by viewModel.isUploadingImage.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    val galleryImages by viewModel.galleryImages.collectAsState()
    var pendingGalleryUploads by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val profileImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.uploadProfileImage(uri)
            }
        }
    )

    val galleryImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(6),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                val availableSlots = 6 - galleryImages.size
                val filteredUris = uris.take(availableSlots)
                if (filteredUris.isNotEmpty()) {
                    pendingGalleryUploads = filteredUris
                    viewModel.uploadGalleryImages(filteredUris)
                }
            }
        }
    )

    // UI state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var hometown by remember { mutableStateOf("") }
    var profession by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        isLoading = true
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                val response = userRepo.getUserProfile(currentUser.uid)
                if (response is Response.Success) {
                    val profile = response.result
                    userProfile = profile
                    firstName = profile.firstName
                    lastName = profile.lastName
                    email = currentUser.email ?: ""
                    phone = currentUser.phoneNumber ?: ""
                    age = if (profile.birthday.isNotEmpty()) {
                        getAgeFromBirthday(profile.birthday)
                    } else {
                        ""
                    }
                    hometown = profile.homeTown
                    profession = profile.profession
                    about = profile.about

                    // Initialize gallery images
                    viewModel.setGalleryImages(profile.galleryImages)

                    // Initialize profile image if exists
                    if (!profile.profileImageUrl.isNullOrEmpty()) {
                        viewModel.setProfileImageUrl(profile.profileImageUrl)
                    }

                    // Set passions
                    val profilePassionsList = profile.passions
                    hobbyOptions.forEach { hobby ->
                        hobby.isSelected = false
                    }
                    profilePassionsList.forEach { passionType ->
                        hobbyOptions.find { hobby ->
                            hobby.passionType?.id == passionType
                        }?.isSelected = true
                    }
                } else {
                    errorMessage = "Failed to load profile: ${(response as? Response.Failure)?.exception?.message ?: "Unknown error"}"
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
        derivedStateOf { 1f - (scrollOffset / 500f).coerceIn(0f, 0.5f) }
    }

    // Calculate Y Offset (move upwards)
    val yOffset by remember {
        derivedStateOf { -scrollOffset * 1.5f }
    }

    // Change alpha based on scroll
    val circularImageAlpha by remember {
        derivedStateOf { 1f - (scrollOffset / 300f).coerceIn(0f, 1f) }
    }

    // Background Image Alpha (Fades In on Scroll Up, Reverse of Circular Image)
    val backgroundImageAlpha by remember {
        derivedStateOf { (scrollOffset / 500f).coerceIn(0f, 1f) }
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

    var passionsList by remember { mutableStateOf(hobbyOptions.filter { it.isSelected }) }

    val localContext = LocalContext.current

    val customTextSelectionColors = TextSelectionColors(
        handleColor = HotPink,
        backgroundColor = HotPink.copy(alpha = 0.4f)
    )

    val authViewModel = hiltViewModel<AuthViewModel>()

    LaunchedEffect(userProfile) {
        if (userProfile != null) {
            genderSelectedText = userProfile?.gender ?: "Male"
            interestSelectedText = userProfile?.interestPreference ?: "Female"
        }
    }
    var hasNotificationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var hasContactPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasContactPermission = isGranted
        contactSyncing = isGranted
        if (isGranted) {
            Toast.makeText(context, "Contact sync enabled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Contact permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        notificationSyncing = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notifications enabled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        hasContactPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        contactSyncing = hasContactPermission

        hasNotificationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        notificationSyncing = hasNotificationPermission
    }

    Box(
        modifier = modifier
            .padding()
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = listOf(HotPink, White, White))),
        contentAlignment = Alignment.TopCenter
    ) {
        NetworkImage(
            url = userProfile?.profileImageUrl ?: "",
            contentDescription = "Profile image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight / 2.5f)
                .graphicsLayer(
                    alpha = backgroundImageAlpha.coerceIn(0f, 1f)
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
                            trailingIcon = {
                                TextButton(
                                    onClick = {

                                    },
                                    enabled = isEditing
                                ) {
                                    Text("Verify", color = if(isEditing) HotPink else Gray)
                                }
                            },
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
                            trailingIcon = {
                                TextButton(
                                    onClick = {

                                    },
                                    enabled = isEditing
                                ) {
                                    Text("Verify", color = if(isEditing) HotPink else Gray)
                                }
                            },
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
                            onExpandedChange = { if (isEditing) genderExpanded = !genderExpanded }
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
                            value = hometown,
                            onValueChange = { hometown = it },
                            label = { Text("HomeTown") },
                            enabled = false,
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
                            onExpandedChange = { if (isEditing) interestExpanded = !interestExpanded }
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
                        passionsList.forEach {
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
                            onCheckedChange = { isChecked ->
                                if (isChecked && !hasContactPermission) {
                                    contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                } else {
                                    contactSyncing = isChecked
                                }
                            },
                            enabled = true,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = HotPink,
                                checkedBorderColor = HotPink,
                                uncheckedThumbColor = HotPink,
                                uncheckedBorderColor = HotPink,
                                uncheckedTrackColor = HotPink.copy(0.15f)
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
                            onCheckedChange = { isChecked ->
                                if (isChecked && !hasNotificationPermission) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        notificationSyncing = isChecked
                                    }
                                } else {
                                    notificationSyncing = isChecked
                                }
                            },
                            enabled = true,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = White,
                                checkedTrackColor = HotPink,
                                checkedBorderColor = HotPink,
                                uncheckedThumbColor = HotPink,
                                uncheckedBorderColor = HotPink,
                                uncheckedTrackColor = HotPink.copy(0.15f)
                            )
                        )
                    }

                    DefaultButton(
                        modifier = Modifier.padding(top = 40.dp, bottom = 50.dp),
                        text = if(
                            isEditing
                        ){ "Save" } else {
                            "Log Out"
                        }
                    ) {
                        if (isEditing) {
                            coroutineScope.launch {
                                val currentUser = auth.currentUser
                                if (currentUser != null && userProfile != null) {
                                    val selectedPassions = hobbyOptions
                                        .filter { it.isSelected }
                                        .mapNotNull { it.passionType }

                                    val updatedProfile = UserProfile(
                                        firstName = firstName,
                                        lastName = lastName,
                                        profileImageUrl = userProfile?.profileImageUrl,
                                        birthday = userProfile?.birthday ?: "",
                                        homeTown = hometown,
                                        gender = genderSelectedText,
                                        interestPreference = interestSelectedText,
                                        profession = profession,
                                        about = about,
                                        passionsObject = selectedPassions,
                                        galleryImages = galleryImages
                                    )

                                    viewModel.updateProfileDetails(
                                        updatedProfile = updatedProfile,
                                        originalProfile = userProfile!!,
                                        onComplete = { success ->
                                            if (success) {
                                                Toast.makeText(localContext, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val errorMessage = viewModel.savingError.value ?: "Failed to update profile"
                                                Toast.makeText(localContext, errorMessage, Toast.LENGTH_SHORT).show()
                                            }
                                            isEditing = !isEditing
                                        }
                                    )
                                } else {
                                    isEditing = !isEditing
                                }
                            }
                        }
                        else {
                            authViewModel.signOut()
                            onNavigate(Routes.ONBOARDING_SCREEN)
                        }
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
                    Toast.makeText(localContext, "Discard changes", Toast.LENGTH_SHORT).show()
                } else onNavigate(Routes.POP)
            }

            DefaultIconButton(
                if (!isEditing) R.drawable.edit else R.drawable.save,
                White,
                modifier = Modifier.padding(end = 40.dp)
            ) {
                if (isEditing) {
                    coroutineScope.launch {
                        val currentUser = auth.currentUser
                        if (currentUser != null && userProfile != null) {
                            val selectedPassions = hobbyOptions
                                .filter { it.isSelected }
                                .mapNotNull { it.passionType }

                            val updatedProfile = UserProfile(
                                firstName = firstName,
                                lastName = lastName,
                                profileImageUrl = userProfile?.profileImageUrl,
                                birthday = userProfile?.birthday ?: "",
                                homeTown = hometown,
                                gender = genderSelectedText,
                                interestPreference = interestSelectedText,
                                profession = profession,
                                about = about,
                                passionsObject = selectedPassions,
                                galleryImages = galleryImages
                            )

                            viewModel.updateProfileDetails(
                                updatedProfile = updatedProfile,
                                originalProfile = userProfile!!,
                                onComplete = { success ->
                                    if (success) {
                                        Toast.makeText(localContext, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val errorMessage = viewModel.savingError.value ?: "Failed to update profile"
                                        Toast.makeText(localContext, errorMessage, Toast.LENGTH_SHORT).show()
                                    }
                                    isEditing = !isEditing
                                }
                            )
                        } else {
                            isEditing = !isEditing
                        }
                    }
                } else {
                    isEditing = true
                }
            }
        }

        // Show profile image with upload option if in edit mode
        Box(
            modifier = Modifier
                .padding(top = 200.dp)
                .graphicsLayer(
                    scaleX = scale.coerceIn(0.5f, 1f),
                    scaleY = scale.coerceIn(0.5f, 1f),
                    translationY = yOffset.coerceIn(-400f, 0f),
                    alpha = circularImageAlpha.coerceIn(0f, 1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Profile image
            if (isUploadingImage) {
                // Show loading indicator while uploading
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = HotPink,
                        progress = { uploadProgress / 100f },
                        modifier = Modifier.size(50.dp)
                    )
                }
            } else if (profileImage != null) {
                // Show current local URI (for newly selected image)
                AsyncImage(
                    model = profileImage,
                    contentDescription = "Profile image",
                    imageLoader = imageLoader,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable(enabled = isEditing) {
                            profileImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
            } else if (userProfile?.profileImageUrl != null) {
                NetworkImage(
                    url = userProfile?.profileImageUrl ?: "",
                    contentDescription = "Profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable(enabled = isEditing) {
                            profileImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
            } else {
                // Show placeholder
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable(enabled = isEditing) {
                            profileImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_insert_photo_24),
                        contentDescription = "Add profile photo",
                        tint = White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            // Add a camera button if in edit mode
            if (isEditing) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(HotPink)
                        .clickable {
                            profileImagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Change profile photo",
                        tint = White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (showPassionBottomSheet) {
            OptimizedBottomSheet(
                onDismiss = {
                    passionsList = hobbyOptions.filter { it.isSelected }
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    showPassionBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Passions {
                    passionsList = hobbyOptions.filter { it.isSelected }
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    showPassionBottomSheet = false
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = HotPink,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        "Loading profile...",
                        color = White,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (!isLoading && errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.default_close),
                        contentDescription = "Error",
                        tint = HotPink,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        errorMessage ?: "Unknown error occurred",
                        color = HotPink,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    DefaultButton(
                        modifier = Modifier.padding(top = 40.dp, bottom = 50.dp),
                        text = if(
                            isEditing
                        ){ "Save" } else {
                            "Log Out"
                        }
                    ) {
                        if (isEditing) {
                            coroutineScope.launch {
                                val currentUser = auth.currentUser
                                if (currentUser != null && userProfile != null) {
                                    val selectedPassions = hobbyOptions
                                        .filter { it.isSelected }
                                        .mapNotNull { it.passionType }

                                    val updatedProfile = UserProfile(
                                        firstName = firstName,
                                        lastName = lastName,
                                        profileImageUrl = userProfile?.profileImageUrl,
                                        birthday = userProfile?.birthday ?: "",
                                        homeTown = hometown,
                                        gender = genderSelectedText,
                                        interestPreference = interestSelectedText,
                                        profession = profession,
                                        about = about,
                                        passionsObject = selectedPassions,
                                        galleryImages = galleryImages
                                    )

                                    viewModel.updateProfileDetails(
                                        updatedProfile = updatedProfile,
                                        originalProfile = userProfile!!,
                                        onComplete = { success ->
                                            if (success) {
                                                Toast.makeText(localContext, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val errorMessage = viewModel.savingError.value ?: "Failed to update profile"
                                                Toast.makeText(localContext, errorMessage, Toast.LENGTH_SHORT).show()
                                            }
                                            isEditing = !isEditing
                                        }
                                    )
                                } else {
                                    isEditing = !isEditing
                                }
                            }
                        }
                        else {
                            authViewModel.signOut()
                            onNavigate(Routes.ONBOARDING_SCREEN)
                        }
                    }

                    DefaultButton(
                        text = "Go Back",
                        btnColor = HotPink.copy(alpha = 0.2f),
                        txtColor = HotPink,
                        onClick = { onNavigate(Routes.POP) }
                    )
                }
            }
        }
    }
}