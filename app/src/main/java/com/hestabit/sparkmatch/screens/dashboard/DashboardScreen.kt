package com.hestabit.sparkmatch.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {

    val annotatedText = buildAnnotatedString {
        // Add non-clickable text
        pushStyle(SpanStyle(fontWeight = FontWeight.W700, fontSize = 24.sp))
        append(
            "Discover"
        )
        pop()
        pushStyle(SpanStyle(fontWeight = FontWeight.W400, fontSize = 12.sp))
        append("\nDelhi, IN")
        pop()
    }

    var selectedItem by remember { mutableStateOf(0) }

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
                        Text(annotatedText, textAlign = TextAlign.Center, style = TextStyle())
                    }
                },
                navigationIcon = { DefaultIconButton(R.drawable.round_arrow_back_ios_24) },
                actions = { DefaultIconButton(R.drawable.setting_config) }
            )
        },

        bottomBar = {

            CustomBottomAppBar(selectedItem) { index ->
                selectedItem = index
            }

        }

    ) { paddingValues ->


    }

}

@Composable
fun CustomBottomAppBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Cards", "Favorites", "Messages", "Profile")
    val icons = listOf(
        R.drawable.discover_active,   // Replace with actual drawable
        R.drawable.match_inactive_badge,   // Replace with actual drawable
        R.drawable.chat_inactive,    // Replace with actual drawable
        R.drawable.profile_inactive   // Replace with actual drawable
    )

    BottomAppBar(containerColor = Color(0xffF3F3F3)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {
            items.forEachIndexed { index, title ->
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = title,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
            }
        }
    }
}