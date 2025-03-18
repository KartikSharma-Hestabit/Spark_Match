package com.hestabit.sparkmatch.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.hestabit.sparkmatch.R
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthdayPicker(
    initialDate: LocalDate = LocalDate.now(),
    onSave: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentYear by remember { mutableIntStateOf(initialDate.year) }
    val currentMonth by remember { mutableIntStateOf(initialDate.monthValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Birthday", fontSize = 18.sp, fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentYear-- }) {
                Icon(painter = painterResource(R.drawable.arrow_left), contentDescription = "")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentYear",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD24D57)
                )
                Text(
                    text = LocalDate.of(currentYear, currentMonth, 1).month.name,
                    color = Color(0xFFD24D57),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = { currentYear++ }) {
                Icon(painter = painterResource(R.drawable.arrow_right), contentDescription = "")
            }
        }

        Spacer(Modifier.height(8.dp))

        val daysInMonth = YearMonth.of(currentYear, currentMonth).lengthOfMonth()
        val firstDayOfWeek = LocalDate.of(currentYear, currentMonth, 1).dayOfWeek.value % 7

        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(text = day, fontSize = 14.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }

            Spacer(Modifier.height(4.dp))

            for (row in 0..5) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (col in 0..6) {
                        val day = row * 7 + col - firstDayOfWeek + 1
                        if (day in 1..daysInMonth) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        selectedDate = LocalDate.of(currentYear, currentMonth, day)
                                    }
                                    .background(
                                        if (selectedDate.dayOfMonth == day &&
                                            selectedDate.monthValue == currentMonth &&
                                            selectedDate.year == currentYear
                                        ) Color(0xFFD24D57) else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (selectedDate.dayOfMonth == day &&
                                        selectedDate.monthValue == currentMonth &&
                                        selectedDate.year == currentYear
                                    ) Color.White else Color.Black
                                )
                            }
                        } else {
                            Box(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSave(selectedDate) },
            colors = ButtonDefaults.buttonColors(Color(0xFFD24D57)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save", color = Color.White, fontSize = 16.sp)
        }
    }
}