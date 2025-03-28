package com.hestabit.sparkmatch.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.screens.auth.Friends
import com.hestabit.sparkmatch.screens.auth.Gender
import com.hestabit.sparkmatch.screens.auth.Notifications
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.screens.auth.SignUp

import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.modernist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test(navController: NavController){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {  },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BackButton(navController, HotPink)
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = {  }) {
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
                modifier = Modifier.padding(20.dp)
            )
        }
    ){ paddingValues ->
        SignUp(navController, paddingValues)
    }
}