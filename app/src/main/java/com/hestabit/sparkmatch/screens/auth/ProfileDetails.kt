package com.hestabit.sparkmatch.screens.auth

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.OptimizedBirthdayPicker
import com.hestabit.sparkmatch.common.OptimizedBottomSheet
import com.hestabit.sparkmatch.common.ProfileImagePicker
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.min

@SuppressLint("ConfigurationScreenWidthHeight")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    viewModel: ProfileDetailsViewModel = viewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val horizontalPadding = (screenWidth * 0.05f).dp
    val verticalPadding = (screenHeight * 0.03f).dp
    val titleFontSize = min(34f, screenWidth * 0.08f).sp
    val buttonHeight = min(56f, screenHeight * 0.07f).dp

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        viewModel.updateProfileImage(uri)
    }

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) sheetState.show()
    }

    // Show snackbar for errors
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    if (isBottomSheetVisible) {
        OptimizedBottomSheet(
            onDismiss = { isBottomSheetVisible = false },
            sheetState = sheetState
        ) {
            OptimizedBirthdayPicker(
                scope = scope,
                initialDate = uiState.birthDate ?: LocalDate.now().minusYears(18),
                onSave = { date ->
                    viewModel.updateBirthDate(date)
                    scope.launch {
                        sheetState.hide()
                        delay(15)
                        isBottomSheetVisible = false
                    }
                },
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        delay(15)
                        isBottomSheetVisible = false
                    }
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues)
                .padding(horizontal = horizontalPadding, vertical = verticalPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile details",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = titleFontSize,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(90.dp))

            ProfileImagePicker(
                imageUri = uiState.profileImageUri,
                onImageClick = {
                    launcher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.firstName,
                    onValueChange = { viewModel.updateFirstName(it) },
                    label = { Text("First name") },
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OffWhite,
                        focusedBorderColor = Gray,
                        unfocusedLabelColor = OffWhite,
                        focusedLabelColor = Gray,
                        cursorColor = Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.lastName,
                    onValueChange = { viewModel.updateLastName(it) },
                    label = { Text("Last name") },
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OffWhite,
                        focusedBorderColor = Gray,
                        unfocusedLabelColor = OffWhite,
                        focusedLabelColor = Gray,
                        cursorColor = Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { isBottomSheetVisible = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AE94057), contentColor = White),
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.calender),
                        contentDescription = "Calendar Icon",
                        tint = HotPink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = uiState.birthDate?.let { "Birthday: ${it.month.name} ${it.dayOfMonth}, ${it.year}" }
                            ?: "Choose birthday date",
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = min(16f, screenWidth * 0.04f).sp,
                        color = HotPink
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f, fill = true).heightIn(min = (screenHeight * 0.05f).dp))

            // Show loading indicator if data is being saved
            if (uiState.isLoading) {
                CircularProgressIndicator(color = HotPink)
                Spacer(modifier = Modifier.height(16.dp))
            }

            DefaultButton(
                text = "Confirm",
                onClick = {
                    if (viewModel.validateProfileDetails()) {
                        viewModel.saveProfileDetails {
                            onNavigate(AuthRoute.Gender.route)
                        }
                    }
                },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}