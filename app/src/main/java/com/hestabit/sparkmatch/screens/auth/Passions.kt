package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.data.Hobby
import com.hestabit.sparkmatch.data.options
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Passions(navController: NavController) {
    var selectedOptions by remember { mutableStateOf(setOf<Hobby>()) }

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
                TextButton(onClick = { navController.navigate(Routes.FRIENDS) }) {
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
                    text = "Continue",
                    onClick = {
                        navController.navigate(Routes.FRIENDS)
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 40.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Your interests",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
            )

            Text(
                text = "Select a few of your interests and let everyone know what you’re passionate about.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                options.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { hobby ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                            ) {
                                PassionSelectionButton(
                                    hobby = hobby,
                                    isSelected = selectedOptions.contains(hobby)
                                ) {
                                    selectedOptions = if (selectedOptions.contains(hobby)) {
                                        selectedOptions - hobby
                                    } else {
                                        if (selectedOptions.size < 5) selectedOptions + hobby else selectedOptions
                                    }
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun PassionSelectionButton(
    hobby: Hobby,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(20.dp), ambientColor = HotPink)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) HotPink else White)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = hobby.iconRes),
                contentDescription = hobby.name,
                tint = if (isSelected) White else HotPink,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = hobby.name,
                fontSize = 14.sp,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}
