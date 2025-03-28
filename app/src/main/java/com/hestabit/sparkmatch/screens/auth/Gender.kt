package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.GenderSelectionButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Gender(navController: NavController, paddingValues: PaddingValues) {
    var selectedOption by remember { mutableStateOf("Man") }

    Column(
        modifier = Modifier.fillMaxSize().background(White).padding(paddingValues).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "I am a",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            GenderSelectionButton(text = "Man", isSelected = selectedOption == "Man") {
                selectedOption = "Man"
            }
            Spacer(modifier = Modifier.height(8.dp))
            GenderSelectionButton(text = "Woman", isSelected = selectedOption == "Woman") {
                selectedOption = "Woman"
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "Continue",
            onClick = {
                navController.navigate(AuthRoute.Passions.route)
            }
        )
    }
}

