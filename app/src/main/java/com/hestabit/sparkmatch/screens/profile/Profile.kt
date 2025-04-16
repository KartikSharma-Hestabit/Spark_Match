package com.hestabit.sparkmatch.screens.profile

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.ProfileContent
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.viewmodel.UserProfileViewModel

private const val TAG = "ProfileScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Profile(
    navController: NavController,
    userProfile: UserProfile? = null,
    userId: String? = null,
    profileViewModel: UserProfileViewModel = hiltViewModel()
) {
    val viewModelProfile by profileViewModel.profileData.collectAsState()
    val viewModelOtherUserProfile by profileViewModel.otherUserProfile.collectAsState()

    Log.d(TAG, "Profile screen called with - userProfile: ${userProfile?.firstName}, userId: $userId")

    val isOtherUserProfile = userProfile != null || userId != null

    DisposableEffect(Unit) {
        onDispose {
            Log.d(TAG, "Profile screen disposed")
        }
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            Log.d(TAG, "Fetching profile for userId: $userId")
            profileViewModel.fetchUserProfile(userId)
        } else if (!isOtherUserProfile && viewModelProfile is Response.InitialValue) {
            Log.d(TAG, "Fetching current user profile")
            profileViewModel.fetchCurrentUserProfile()
        }
    }

    val interests = listOf("Travelling", "Books", "Music", "Dancing", "Modeling")
    val selectedInterests = remember { mutableStateListOf("Travelling", "Books") }

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp)
            ) {
                BackButton(navController, HotPink)
            }
        }
    ) { paddingValues ->
        when {
            userProfile != null -> {
                Log.d(TAG, "Displaying directly passed profile: ${userProfile.firstName} ${userProfile.lastName}")
                ProfileContent(
                    profile = userProfile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            // User ID was passed and we're loading that profile
            userId != null && viewModelOtherUserProfile is Response.Loading -> {
                Log.d(TAG, "Loading profile for userId: $userId")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HotPink)
                }
            }

            // User ID was passed and we successfully loaded the profile
            userId != null && viewModelOtherUserProfile is Response.Success -> {
                val profile = (viewModelOtherUserProfile as Response.Success<UserProfile>).result
                Log.d(TAG, "Successfully loaded profile for userId: $userId - ${profile.firstName} ${profile.lastName}")
                ProfileContent(
                    profile = profile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            // User ID was passed but we failed to load the profile
            userId != null && viewModelOtherUserProfile is Response.Failure -> {
                Log.e(TAG, "Failed to load profile for userId: $userId")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load profile",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (userId != null) {
                                    profileViewModel.fetchUserProfile(userId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HotPink)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            // We're loading current user's profile
            viewModelProfile is Response.Loading -> {
                Log.d(TAG, "Loading current user profile")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HotPink)
                }
            }

            // Successfully loaded current user's profile
            viewModelProfile is Response.Success -> {
                val profile = (viewModelProfile as Response.Success<UserProfile>).result
                Log.d(TAG, "Successfully loaded current user profile: ${profile.firstName} ${profile.lastName}")
                ProfileContent(
                    profile = profile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            // Failed to load current user's profile
            viewModelProfile is Response.Failure -> {
                Log.e(TAG, "Failed to load current user profile")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load profile",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { profileViewModel.fetchCurrentUserProfile() },
                            colors = ButtonDefaults.buttonColors(containerColor = HotPink)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            // Initial state
            else -> {
                Log.d(TAG, "Initial profile loading state")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HotPink)
                }
            }
        }
    }
}