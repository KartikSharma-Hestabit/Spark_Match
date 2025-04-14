package com.hestabit.sparkmatch.common

import android.os.Build
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

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
            shape = CutoutShape(),
            containerColor = Color.White,
            dragHandle = null,
            properties = ModalBottomSheetProperties(),
            scrimColor = Color.Black.copy(alpha = 0.6f)
        ) {
            content()
        }
    }
}

@Stable
class CutoutShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(
            Path().apply {
                val cornerRadius = size.width * 0.1f
                val notchWidth = size.width * 0.1f
                val notchHeight = size.height * 0.01f
                val centerX = size.width / 2

                moveTo(0f, cornerRadius)
                arcTo(
                    rect = Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(centerX - notchWidth / 2, 0f)

                arcTo(
                    rect = Rect(
                        centerX - notchWidth / 2, -notchHeight,
                        centerX + notchWidth / 2, notchHeight
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )

                lineTo(size.width - cornerRadius, 0f)
                arcTo(
                    rect = Rect(size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptimizedBirthdayPicker(
    viewModel: ProfileDetailsViewModel = hiltViewModel(),
    scope: CoroutineScope,
    onSave: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val currentYearMonth by viewModel.currentYearMonth.collectAsState()
    val initialDate = selectedDate ?: LocalDate.now().minusYears(18)
    val localSelectedDate = remember { mutableStateOf(initialDate) }
    var showYearPicker by remember { mutableStateOf(false) }
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
                    if (showYearPicker) {
                        viewModel.updateCurrentYearMonth(currentYearMonth.minusYears(10), scope)
                    } else {
                        viewModel.updateCurrentYearMonth(currentYearMonth.minusMonths(1), scope)
                    }
                    scope.launch {
                        delay(15)
                        transitionState.value = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_left),
                    contentDescription = if (showYearPicker) "Previous Decade" else "Previous Month",
                    tint = HotPink
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { showYearPicker = !showYearPicker }
                    .padding(8.dp)
            ) {
                if (showYearPicker) {
                    val startYear = (currentYearMonth.year / 10) * 10
                    Text(
                        text = "$startYear - ${startYear + 9}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = HotPink
                    )
                } else {
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
            }

            IconButton(
                onClick = {
                    transitionState.value = true
                    if (showYearPicker) {
                        viewModel.updateCurrentYearMonth(currentYearMonth.plusYears(10), scope)
                    } else {
                        viewModel.updateCurrentYearMonth(currentYearMonth.plusMonths(1), scope)
                    }
                    scope.launch {
                        delay(15)
                        transitionState.value = false
                    }
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_right),
                    contentDescription = if (showYearPicker) "Next Decade" else "Next Month",
                    tint = HotPink
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!showYearPicker) {
            Row(
                modifier = Modifier.wrapContentWidth(),
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .wrapContentSize()
                .alpha(alpha.value)
        ) {
            if (showYearPicker) {
                YearPickerGrid(
                    baseYear = (currentYearMonth.year / 10) * 10,
                    selectedYear = localSelectedDate.value.year,
                    onYearSelected = { year ->
                        val newDate = localSelectedDate.value.withYear(year)
                        localSelectedDate.value = newDate

                        viewModel.updateCurrentYearMonth(
                            YearMonth.of(year, currentYearMonth.month),
                            scope
                        )

                        showYearPicker = false
                    }
                )
            } else {
                // Month view (calendar days)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    state = gridState,
                    modifier = Modifier.wrapContentSize(),
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
        }

        Spacer(modifier = Modifier.height(16.dp))

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
fun YearPickerGrid(
    baseYear: Int,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display first 2 years in the decade
            for (i in 0..3) {
                val year = baseYear + i
                val isSelected = year == selectedYear

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(width = 80.dp, height = 54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> HotPink
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onYearSelected(year) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year.toString(),
                        color = when {
                            isSelected -> Color.White
                            else -> Color.Black
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 4..7) {
                val year = baseYear + i
                val isSelected = year == selectedYear

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(width = 80.dp, height = 54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> HotPink
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onYearSelected(year) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year.toString(),
                        color = when {
                            isSelected -> Color.White
                            else -> Color.Black
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (i in 8..9) {
                val year = baseYear + i
                val isSelected = year == selectedYear

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(width = 80.dp, height = 54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                isSelected -> HotPink
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onYearSelected(year) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year.toString(),
                        color = when {
                            isSelected -> Color.White
                            else -> Color.Black
                        },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
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