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
import androidx.compose.ui.tooling.preview.Preview
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

private const val TAG = "InterestPreferenceScreen"

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InterestPreference(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val viewModel: ProfileDetailsViewModel = hiltViewModel()
    val interestPreference by viewModel.interestPreference.collectAsState()
    var selectedOption by remember { mutableStateOf("Everyone") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val interestOptions = listOf(
        "Men", "Women", "Everyone", "Non-binary people", "Agender people", "Androgynes",
        "Androgynous people", "Bigender people", "Cis men", "Cis women", "Gender fluid people",
        "Gender neutral people", "Gender nonconforming people", "Gender questioning people",
        "Gender variant people", "Genderqueer people", "Intersex people", "Non-binary men",
        "Non-binary women", "Pangender people", "Polygender people", "Trans men", "Trans women",
        "Transgender people", "Transsexual people", "Two-spirit people"
    )

    val filteredOptions = if (searchText.isEmpty()) {
        interestOptions
    } else {
        interestOptions.filter { it.contains(searchText, ignoreCase = true) }
    }

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
                    text = "Who are you interested in?",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = HotPink,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search preferences") },
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
                    items(filteredOptions) { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedOption = option
                                    scope.launch {
                                        bottomSheetState.hide()
                                        showBottomSheet = false
                                    }
                                    viewModel.updateInterestPreference(option)
                                    Log.d(TAG, "Selected interest preference: $option")
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = option,
                                fontFamily = modernist,
                                fontSize = 16.sp,
                                fontWeight = if (selectedOption == option) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedOption == option) HotPink else Color.Black
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
                text = "I am interested in",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }

        Column(modifier = Modifier.padding(top = 16.dp)) {
            GenderSelectionButton(text = "Male", isSelected = selectedOption == "Male") {
                selectedOption = "Male"
                 viewModel.updateInterestPreference("Male")
                Log.d(TAG, "Updated interest preference to Male")
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenderSelectionButton(text = "Female", isSelected = selectedOption == "Female") {
                selectedOption = "Female"
                viewModel.updateInterestPreference("Female")
                Log.d(TAG, "Updated interest preference to Female")
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenderSelectionButton(text = "Everyone", isSelected = selectedOption == "Everyone") {
                selectedOption = "Everyone"
                 viewModel.updateInterestPreference("Everyone")
                Log.d(TAG, "Updated interest preference to Everyone")
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenderSelectionButton(
                text = if (interestOptions.contains(selectedOption) &&
                    selectedOption != "Men" &&
                    selectedOption != "Women" &&
                    selectedOption != "Everyone")
                    selectedOption else "More options",
                isSelected = interestOptions.contains(selectedOption) &&
                        selectedOption != "Men" &&
                        selectedOption != "Women" &&
                        selectedOption != "Everyone"
            ) {
                showBottomSheet = true
                Log.d(TAG, "Opening bottom sheet for interest preference selection")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Continue",
            onClick = {
                viewModel.updateInterestPreference(selectedOption)
                viewModel.saveInterestPreference { success ->
                    if (success) {
                        onNavigate(AuthRoute.About.route)
                    } else {
                        Log.e(TAG, "Failed to save Interest Preference")
                    }
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun InterestPreferencePreview() {
    InterestPreference(onNavigate = {})
}