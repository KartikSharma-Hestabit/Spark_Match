package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.PassionSelectionButton
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Passions(modifier: Modifier = Modifier, onNavigate:(String) -> Unit) {

    val options = listOf(
        PassionList("Photography", R.drawable.photography),
        PassionList("Shopping", R.drawable.weixin_market),
        PassionList("Karaoke", R.drawable.voice),
        PassionList("Yoga", R.drawable.viencharts),
        PassionList("Cooking", R.drawable.noodles),
        PassionList("Tennis", R.drawable.tennis),
        PassionList("Run", R.drawable.sport),
        PassionList("Swimming", R.drawable.ripple),
        PassionList("Art", R.drawable.platte),
        PassionList("Traveling", R.drawable.outdoor),
        PassionList("Extreme", R.drawable.parachute),
        PassionList("Music", R.drawable.music),
        PassionList("Drink", R.drawable.goblet_full),
        PassionList("Video games", R.drawable.game_handle)
    )
    var selectedOptions by remember { mutableStateOf(setOf<PassionList>()) }

    Column(
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
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
        }

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
                                passionList = hobby,
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

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "Continue",
            onClick = {
                onNavigate(AuthRoute.Friends.route)
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PassionsPreview(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Passions{}
    }
}