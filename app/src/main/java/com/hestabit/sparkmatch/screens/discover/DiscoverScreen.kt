package com.hestabit.sparkmatch.screens.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.uiComponents.DefaultIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 40.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Discover", fontWeight = FontWeight.W700, fontSize = 24.sp)
                        Text("Delhi, IN", fontWeight = FontWeight.W400, fontSize = 12.sp)
                    }
                },
                navigationIcon = { DefaultIconButton(R.drawable.round_arrow_back_ios_24) },
                actions = { DefaultIconButton(R.drawable.setting_config) }
            )
        }
    ) { paddingValues ->


    }

}