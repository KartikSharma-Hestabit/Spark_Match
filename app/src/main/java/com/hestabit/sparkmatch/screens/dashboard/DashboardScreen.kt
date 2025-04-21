package com.hestabit.sparkmatch.screens.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.DefaultIconButton
import com.hestabit.sparkmatch.common.OptimizedBottomSheet
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.screens.chat.MessageScreen
import com.hestabit.sparkmatch.screens.discover.DiscoverScreen
import com.hestabit.sparkmatch.screens.discover.FilterScreen
import com.hestabit.sparkmatch.screens.match.MatchScreen
import com.hestabit.sparkmatch.screens.profile.ProfileScreen
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.LocationViewModel.Companion.userCurrentAddress
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onNavigate: (String, UserProfile?, String?) -> Unit) {
    val address by userCurrentAddress.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val annotatedText = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 24.sp))
        append("Discover")
        pop()
        pushStyle(SpanStyle(
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        ))
        append("\n$address")
        pop()
    }

    val matchesTextHeading = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 34.sp))
        append("Matches")
        pop()
    }

    val messageTextHeading = buildAnnotatedString {
        pushStyle(SpanStyle(fontFamily = modernist, fontWeight = FontWeight.Bold, fontSize = 34.sp))
        append("Messages")
        pop()
    }

    val pagerState = rememberPagerState(initialPage = 0) { 4 }
    var selectedItem by remember { mutableIntStateOf(pagerState.currentPage) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // Create a state to track if we need to show permission UI
    var showPermissionRequest by remember { mutableStateOf(false) }

    // Check if we have the required permissions
    val hasLocationPermission by remember {
        derivedStateOf {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    val hasStoragePermission by remember {
        derivedStateOf {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    val hasContactsPermission by remember {
        derivedStateOf {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    val hasNotificationPermission by remember {
        derivedStateOf {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true // No notification permission needed before Android 13
            }
        }
    }

    val allPermissionsGranted by remember {
        derivedStateOf {
            hasLocationPermission && hasStoragePermission &&
                    hasContactsPermission && hasNotificationPermission
        }
    }

    // Only show permission UI when we need to
    LaunchedEffect(Unit) {
        // Check on initial launch and set flag if permissions are needed
        if (!allPermissionsGranted) {
            showPermissionRequest = true
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // When permissions result comes back, hide the request UI
        showPermissionRequest = false
    }

    // The permissions we need to request
    val permissionsToRequest = remember {
        buildList {
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            add(Manifest.permission.ACCESS_FINE_LOCATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
                add(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            add(Manifest.permission.READ_CONTACTS)
        }.toTypedArray()
    }

    // Show permission request UI if needed
    if (showPermissionRequest) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.spark_match_logo),
                    contentDescription = null,
                    tint = HotPink,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    "SparkMatch Permissions",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = HotPink
                )

                Text(
                    "We need a few permissions to help you find your perfect match!",
                    fontFamily = modernist,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                DefaultButton(
                    text = "Grant Permissions",
                    onClick = {
                        // Launch permission request
                        permissionLauncher.launch(permissionsToRequest)
                    }
                )

                TextButton(
                    onClick = {
                        // Skip permissions for now, but they'll be limited
                        showPermissionRequest = false
                    }
                ) {
                    Text(
                        "Skip for now",
                        fontFamily = modernist,
                        color = Color.Gray
                    )
                }
            }
        }
    } else {
        // Main app UI - now separate from permission handling
        Scaffold(
            containerColor = White,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(White),
                    modifier = Modifier,
                    title = {
                        if (selectedItem == 3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.spark_match_logo),
                                    tint = HotPink,
                                    contentDescription = "Profile Screen Logo",
                                    modifier = Modifier.size(32.dp)
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = "Spark Match",
                                    fontFamily = modernist,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = HotPink,
                                    modifier = Modifier
                                )
                            }
                        } else {
                            Text(
                                if (selectedItem == 0) annotatedText else if (selectedItem == 1) matchesTextHeading else messageTextHeading,
                                textAlign = TextAlign.Start,
                                style = TextStyle(),
                                modifier = Modifier.padding(start = 25.dp)
                            )
                        }
                    },
                    actions = {
                        if (selectedItem != 3) {
                            DefaultIconButton(
                                if (selectedItem == 0) R.drawable.setting_config else R.drawable.sort,
                                modifier = Modifier.padding(end = 40.dp)
                            ) {
                                if (selectedItem == 0) {
                                    showFilterSheet = true
                                    coroutineScope.launch {
                                        sheetState.show()
                                    }
                                }
                            }
                        }
                    }
                )
            },
            bottomBar = {
                CustomBottomAppBar(selectedItem) { index ->
                    selectedItem = index
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            index,
                            animationSpec = tween(durationMillis = 300, easing = EaseInOutQuad)
                        )
                    }
                }
            }
        ) { paddingValues ->
            HorizontalPager(
                modifier = Modifier.padding(paddingValues),
                state = pagerState,
                beyondViewportPageCount = 4,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> DiscoverScreen(onNavigate = { route, userProfile, userId ->
                        onNavigate(route, userProfile, userId)
                    })
                    1 -> MatchScreen(onNavigate = { route, userProfile, userId ->
                        onNavigate(route, userProfile, userId)
                    })
                    2 -> MessageScreen(onNavigate = { route ->
                        onNavigate(route, null, null)
                    })
                    3 -> ProfileScreen(onNavigate = { route ->
                        onNavigate(route, null, null)
                    })
                }
            }

            if (showFilterSheet) {
                OptimizedBottomSheet(
                    onDismiss = {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                        showFilterSheet = false
                    },
                    sheetState = sheetState
                ) {
                    FilterScreen {
                        showFilterSheet = false
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomBottomAppBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Cards", "Favorites", "Messages", "Profile")
    val icons = listOf(
        R.drawable.discover_active,
        R.drawable.match_active_badge,
        R.drawable.chat_active,
        R.drawable.profile_active
    )
    val iconsUnselected = listOf(
        R.drawable.discover_inactive,
        R.drawable.match_inactive_badge,
        R.drawable.chat_inactive,
        R.drawable.profile_inactive
    )

    var isLeftMoving by remember { mutableStateOf(true) }

    val leftDivider = animateFloatAsState(
        targetValue = if (selectedItem == 0) 0.001f else selectedItem / 4f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = if (!isLeftMoving) 200 else 0,
            easing = EaseInOutQuad
        )
    )
    val rightDivider = animateFloatAsState(
        targetValue = if (selectedItem == 3) 0.001f else (3 - selectedItem) / 4f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = if (isLeftMoving) 200 else 0,
            easing = EaseInOutQuad
        )
    )

    BottomAppBar(containerColor = Color(0xffF3F3F3), modifier = Modifier.height(82.dp)) {
        Column(verticalArrangement = Arrangement.Top) {
            Row {
                HorizontalDivider(
                    thickness = 0.dp,
                    color = Color.Unspecified,
                    modifier = Modifier.weight(leftDivider.value)
                )
                Box(modifier = Modifier.weight(1 / 4f), contentAlignment = Alignment.Center) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth(0.65f)
                    )
                }

                HorizontalDivider(
                    thickness = 0.dp,
                    color = Color.Unspecified,
                    modifier = Modifier.weight(rightDivider.value)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                items.forEachIndexed { index, title ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isLeftMoving = selectedItem > index
                                onItemSelected(index)
                            }
                            .fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = if (selectedItem == index) icons[index] else iconsUnselected[index]),
                            contentDescription = title,
                            tint = if (selectedItem == 3 && index == 3) Color.Red else Color.Unspecified,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}