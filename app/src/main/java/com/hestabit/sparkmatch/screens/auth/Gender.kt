package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.GenderSelectionButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.launch

private const val TAG = "GenderScreen"

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Gender(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel: ProfileDetailsViewModel = hiltViewModel()
    val currentGender by viewModel.gender.collectAsState()
    var selectedOption by remember { mutableStateOf(currentGender.ifEmpty { "Man" }) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Updated list of all gender options
    val otherGenders = listOf(
        "Agender", "Androgyne", "Androgynes", "Androgynous", "Asexual", "Bigender",
        "Cis", "Cis Female", "Cis Male", "Cis Man", "Cis Woman", "Cisgender",
        "Cisgender Female", "Cisgender Male", "Cisgender Man", "Cisgender Woman",
        "F2M", "Female to Male", "Female to Male Trans Man", "Female to Male Transgender Man",
        "Female to Male Transsexual Man", "FTM", "Gender Fluid", "Gender Neutral",
        "Gender Nonconforming", "Gender Questioning", "Gender Variant", "Genderqueer",
        "Hermaphrodite", "Intersex", "Intersex Man", "Intersex Person", "Intersex Woman",
        "M2F", "Male to Female", "Male to Female Trans Woman", "Male to Female Transgender Woman",
        "Male to Female Transsexual Woman", "MTF", "Neither", "Neutrois", "Non-binary",
        "Other", "Pangender", "Polygender", "T* Man", "T* Woman", "Trans", "Trans Female",
        "Trans Male", "Trans Man", "Trans Person", "Trans*Female", "Trans*Male", "Trans*Man",
        "Trans*Person", "Trans*Woman", "Transexual", "Transexual Female", "Transexual Male",
        "Transexual Man", "Transexual Person", "Transexual Woman", "Transgender Female",
        "Transgender Person", "Transmasculine", "Two* Person", "Two-Spirit", "Two-Spirit Person"
    )

    // Filter genders based on search text
    val filteredGenders = if (searchText.isEmpty()) {
        otherGenders
    } else {
        otherGenders.filter { it.contains(searchText, ignoreCase = true) }
    }

    // Log for debugging
    Log.d(TAG, "Current gender from viewModel: $currentGender")
    Log.d(TAG, "Initial selectedOption: $selectedOption")

    // Bottom Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Select Gender",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = HotPink,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search gender identity") },
                    shape = RoundedCornerShape(15.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OffWhite,
                        focusedBorderColor = Gray,
                        unfocusedLabelColor = OffWhite,
                        focusedLabelColor = Gray,
                        cursorColor = Gray
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "Search",
                            tint = Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.height(400.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredGenders) { gender ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = gender
                                    viewModel.updateGender(gender)
                                    scope.launch {
                                        bottomSheetState.hide()
                                        showBottomSheet = false
                                    }
                                    Log.d(TAG, "Selected gender: $gender")
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = gender,
                                fontFamily = modernist,
                                fontSize = 16.sp,
                                fontWeight = if (selectedOption == gender) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedOption == gender) HotPink else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(40.dp)
            .verticalScroll(scrollState),
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

            Spacer(modifier = Modifier.height(8.dp))

            // Custom selection for "Others" that shows what was selected from bottom sheet
            GenderSelectionButton(
                text = if (otherGenders.contains(selectedOption)) selectedOption else "Choose another",
                isSelected = otherGenders.contains(selectedOption)
            ) {
                showBottomSheet = true
                Log.d(TAG, "Opening bottom sheet for gender selection")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Continue",
            onClick = {
                // Make sure we're using the selected option
                viewModel.updateGender(selectedOption)
                Log.d(TAG, "Saving profile with gender: $selectedOption")

                // Use the specialized save function for gender
                viewModel.saveGenderSelection { success ->
                    Log.d(TAG, "Gender save result: $success")
                    if (success) {
                        onNavigate(AuthRoute.InterestPreference.route)
                    } else {
                        Log.e(TAG, "Failed to save gender")
                    }
                }
            }
        )
    }
}