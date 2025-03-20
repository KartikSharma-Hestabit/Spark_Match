package com.hestabit.sparkmatch.screens.auth

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomTextField
import com.hestabit.sparkmatch.common.ProfileImagePicker
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(navController: NavController) {
    val viewModel: ProfileDetailsViewModel = viewModel()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        result.value = it
    }

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp, top = 40.dp)
                    .fillMaxWidth()
            ) {
                BackButton(navController, HotPink)
                TextButton(onClick = { navController.navigate(Routes.PASSIONS) }) {
                    Text(
                        text = "Skip",
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = HotPink
                    )
                }
            }
        },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {
                OptimizedButton(
                    text = "Confirm",
                    onClick = {
                        navController.navigate(Routes.GENDER)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 40.dp, vertical = 40.dp)
                .verticalScroll(scrollState),
        ) {
            Text(
                text = "Profile details",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
            )

            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileImagePicker(
                    imageUri = null,
                    onImageClick = {
                        launcher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OptimizedTextField(
                    label = "First name",
                    value = firstName,
                    onValueChange = { viewModel.updateFirstName(it) }
                )
                OptimizedTextField(
                    label = "Last name",
                    value = lastName,
                    onValueChange = { viewModel.updateLastName(it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OptimizedDateButton(
                selectedDate = selectedDate,
                onClick = { viewModel.showBottomSheet() }
            )
        }
    }

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) {
            try {
                sheetState.expand()
            } catch (e: Exception) {
                println("Error on profile details")
            }
        }
    }

    if (isBottomSheetVisible) {
        OptimizedBottomSheet(
            onDismiss = { viewModel.hideBottomSheet() },
            sheetState = sheetState
        ) {
            OptimizedBirthdayPicker(
                viewModel = viewModel,
                scope = scope,
                onSave = { date ->
                    viewModel.updateSelectedDate(date)
                    scope.launch {
                        try {
                            sheetState.hide()
                            delay(15)
                            viewModel.hideBottomSheet()
                        } catch (e: Exception) {
                            viewModel.hideBottomSheet()
                        }
                    }
                },
                onDismiss = {
                    scope.launch {
                        try {
                            sheetState.hide()
                            delay(15)
                            viewModel.hideBottomSheet()
                        } catch (e: Exception) {
                            viewModel.hideBottomSheet()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun OptimizedButton(text: String, onClick: () -> Unit) {
    var isClickable by remember { mutableStateOf(true) }

    Button(
        onClick = {
            if (isClickable) {
                isClickable = false
                onClick()
                kotlinx.coroutines.MainScope().launch {
                    delay(30)
                    isClickable = true
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = HotPink),
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun OptimizedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var localValue by remember { mutableStateOf(value) }
    val scope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(value) {
        if (value != localValue) {
            localValue = value
        }
    }

    CustomTextField(
        label = label,
        value = localValue,
        onValueChange = { newValue ->
            localValue = newValue
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(5)
                onValueChange(newValue)
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptimizedDateButton(selectedDate: LocalDate?, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AE94057), contentColor = White),
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.calender),
                contentDescription = "Calendar Icon",
                tint = HotPink,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = selectedDate?.let { "Birthday: ${it.month.name} ${it.dayOfMonth}, ${it.year}" }
                    ?: "Choose birthday date",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = HotPink
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimizedBottomSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    content: @Composable () -> Unit
) {

    key(true) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.LightGray)
                        .padding(vertical = 16.dp)
                )
            }
        ) {
            content()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptimizedBirthdayPicker(
    viewModel: ProfileDetailsViewModel,
    scope: CoroutineScope,
    onSave: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()

    val initialDate = selectedDate ?: LocalDate.now().minusYears(18)
    val localSelectedDate = remember { mutableStateOf(initialDate) }

    val daysInMonth by remember(currentYearMonth) {
        derivedStateOf { getDaysInMonth(currentYearMonth) }
    }

    val gridState = rememberLazyGridState()

    val transitionState = remember { mutableStateOf(false) }
    val alpha = animateFloatAsState(
        targetValue = if (transitionState.value) 0.3f else 1f,
        animationSpec = tween(150),
        label = "Calendar Fade"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Birthday",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = modernist,
            color = HotPink
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    transitionState.value = true
                    viewModel.updateCurrentYearMonth(currentYearMonth.minusMonths(1), scope)
                    scope.launch {
                        delay(15)
                        transitionState.value = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = "Previous Month",
                    tint = HotPink
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentYearMonth.month.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = HotPink
                )
                Text(
                    text = currentYearMonth.year.toString(),
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = {
                    transitionState.value = true
                    viewModel.updateCurrentYearMonth(currentYearMonth.plusMonths(1), scope)
                    scope.launch {
                        delay(15)
                        transitionState.value = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = "Next Month",
                    tint = HotPink
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .height(240.dp)
                .alpha(alpha.value)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                state = gridState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                userScrollEnabled = false
            ) {
                val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value % 7
                items(firstDayOfMonth) {
                    Box(modifier = Modifier.size(40.dp))
                }

                items(
                    items = daysInMonth,
                    key = { it }
                ) { day ->
                    val date = currentYearMonth.atDay(day)
                    val isSelected = date.equals(localSelectedDate.value)

                    CalendarDay(
                        day = day,
                        isSelected = isSelected,
                        onClick = { localSelectedDate.value = date }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEEEEEE),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = { onSave(localSelectedDate.value) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = HotPink),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Save",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CalendarDay(
    day: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> HotPink
                    else -> Color.Transparent
                }
            )
            .clickable(
                onClick = {
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            color = when {
                isSelected -> Color.White
                else -> Color.Black
            },
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getDaysInMonth(yearMonth: YearMonth): List<Int> {
    return (1..yearMonth.lengthOfMonth()).toList()
}