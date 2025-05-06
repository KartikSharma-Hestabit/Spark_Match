package com.hestabit.sparkmatch.screens.match

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.ui.theme.modernist

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchScreen(onNavigate: (String, UserProfile?, String?) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 30.dp)
    ) {

        Text(
            "This is a list of people who have liked you and your matches.",
            fontFamily = modernist,
            fontSize = 16.sp
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            Text(
                "Likes",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            )

            LikedList()

            Text(
                "Matches",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp
            )

            MatchingCardList(onNavigate = onNavigate)
        }

    }
}