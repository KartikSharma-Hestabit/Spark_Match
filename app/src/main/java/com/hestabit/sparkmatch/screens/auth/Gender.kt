package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.GenderSelectionButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel

private const val TAG = "GenderScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Gender(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel: ProfileDetailsViewModel = viewModel()
    val currentGender by viewModel.gender.collectAsState()
    var selectedOption by remember { mutableStateOf(currentGender.ifEmpty { "Man" }) }

    // Log for debugging
    Log.d(TAG, "Current gender from viewModel: $currentGender")
    Log.d(TAG, "Initial selectedOption: $selectedOption")

    Column(
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
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
                viewModel.updateGender("Man")
                Log.d(TAG, "Updated gender to Man")
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenderSelectionButton(text = "Woman", isSelected = selectedOption == "Woman") {
                selectedOption = "Woman"
                viewModel.updateGender("Woman")
                Log.d(TAG, "Updated gender to Woman")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Continue",
            onClick = {
                // Make sure we're using the selected option (not the string "selectedOption")
                viewModel.updateGender(selectedOption)
                Log.d(TAG, "Saving profile with gender: $selectedOption")

                // Use the specialized save function for gender
                viewModel.saveGenderSelection { success ->
                    Log.d(TAG, "Gender save result: $success")
                    if (success) {
                        onNavigate(AuthRoute.Passions.route)
                    } else {
                        Log.e(TAG, "Failed to save gender")
                    }
                }
            }
        )
    }
}