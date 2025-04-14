package com.hestabit.sparkmatch.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.ui.theme.HotPink

@Composable
fun PhotoFullscreen(navController: NavController){
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .padding(start = 40.dp, top = 40.dp)
                    .fillMaxWidth()
            ) {
                BackButton(navController, HotPink)
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

            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

        }
    }
}