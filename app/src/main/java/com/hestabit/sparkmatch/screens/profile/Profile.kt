package com.hestabit.sparkmatch.screens.profile

import android.os.Build
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.viewmodel.UserProfileViewModel

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
    val isOtherUserProfile = userProfile != null || userId != null
    val interests = listOf("Travelling", "Books", "Music", "Dancing", "Modeling")
    val selectedInterests = remember { mutableStateListOf("Travelling", "Books") }

    LaunchedEffect(userId) {
        if (userId != null) {
            profileViewModel.fetchUserProfile(userId)
        } else if (!isOtherUserProfile && viewModelProfile is Response.InitialValue) {
            profileViewModel.fetchCurrentUserProfile()
        }
    }

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
                ProfileContent(
                    profile = userProfile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            userId != null && viewModelOtherUserProfile is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HotPink)
                }
            }

            userId != null && viewModelOtherUserProfile is Response.Success -> {
                val profile = (viewModelOtherUserProfile as Response.Success<UserProfile>).result
                ProfileContent(
                    profile = profile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            userId != null && viewModelOtherUserProfile is Response.Failure -> {
                val error = (viewModelOtherUserProfile as Response.Failure).exception.message ?: "Unknown error"
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load profile: $error",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (true) {
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

            viewModelProfile is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = HotPink)
                }
            }

            viewModelProfile is Response.Success -> {
                val profile = (viewModelProfile as Response.Success<UserProfile>).result
                ProfileContent(
                    profile = profile,
                    interests = interests,
                    selectedInterests = selectedInterests,
                    paddingValues = paddingValues,
                    navController = navController
                )
            }

            viewModelProfile is Response.Failure -> {
                val error = (viewModelProfile as Response.Failure).exception.message ?: "Unknown error"
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load profile: $error",
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

            else -> {
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