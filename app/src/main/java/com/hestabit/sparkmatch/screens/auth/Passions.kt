package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.Utils.hobbyOptions
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.PassionSelectionButton
import com.hestabit.sparkmatch.data.PassionList
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.AuthRoute.PassionType
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel

private const val TAG = "PassionsScreen"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Passions(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel: ProfileDetailsViewModel = viewModel()

    // Store the number of selected passions
    var selectedCount by remember {
        mutableIntStateOf(hobbyOptions.count { it.isSelected })
    }

    // Convert selected hobbies to PassionType objects
    val updateViewModelPassions = {
        val selectedHobbies = hobbyOptions.filter { it.isSelected }
        Log.d(TAG, "Selected hobbies: ${selectedHobbies.joinToString { it.name }}")

        // Convert hobbyOptions to PassionType objects
        val passionTypes = selectedHobbies.mapNotNull { hobby ->
            // Find matching PassionType by name (case-insensitive)
            val passionType = PassionType.entries.find {
                it.title.equals(hobby.name, ignoreCase = true)
            }

            if (passionType == null) {
                Log.w(TAG, "Could not find PassionType for hobby: ${hobby.name}")
            }

            passionType
        }

        Log.d(TAG, "Setting passions: ${passionTypes.joinToString { it.title }}")
        viewModel.updatePassions(passionTypes)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Your interests",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
            )

            Text(
                text = "Select a few of your interests and let everyone know what you're passionate about.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(hobbyOptions) { hobby ->
                PassionSelectionButton(
                    passionList = hobby,
                    selectionCount = selectedCount,
                ) { count ->
                    selectedCount = count
                    Log.d(TAG, "Selection count updated: $count")
                    // Update viewModel passions when selection changes
                    updateViewModelPassions()
                }
            }
        }

        Text(
            "$selectedCount/5",
            fontFamily = modernist,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Continue",
            onClick = {
                // Ensure passions are updated
                updateViewModelPassions()

                // Use the specialized save function for passions
                viewModel.savePassions { success ->
                    Log.d(TAG, "Passions save result: $success")
                    if (success) {
                        onNavigate(AuthRoute.Friends.route)
                    } else {
                        Log.e(TAG, "Failed to save passions")
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PassionsPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Passions {}
    }
}