package com.hestabit.sparkmatch.screens.auth

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.common.OptimizedBirthdayPicker
import com.hestabit.sparkmatch.common.OptimizedBottomSheet
import com.hestabit.sparkmatch.common.ProfileImagePicker
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.*
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    val viewModel: ProfileDetailsViewModel = viewModel()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val result = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        result.value = it
    }

    LaunchedEffect(isBottomSheetVisible) {
        if (isBottomSheetVisible) sheetState.show()
    }

    if (isBottomSheetVisible) {
        OptimizedBottomSheet(
            onDismiss = { viewModel.hideBottomSheet() },
            sheetState = sheetState
        ) {
            OptimizedBirthdayPicker(
                viewModel = viewModel,
                scope = scope,
                onSave = { date ->
                    viewModel.updateSelectedDate(date)
                    scope.launch {
                        sheetState.hide()
                        delay(15)
                        viewModel.hideBottomSheet()
                    }
                },
                onDismiss = {
                    scope.launch {
                        sheetState.hide()
                        delay(15)
                        viewModel.hideBottomSheet()
                    }
                }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile details",
            textAlign = TextAlign.Start,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(90.dp))

        ProfileImagePicker(
            imageUri = result.value,
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
                value = firstName,
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
                value = lastName,
                onValueChange = { viewModel.updateLastName(it) }, // Fixed Issue Here
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
            onClick = { viewModel.showBottomSheet() },
            modifier = Modifier.fillMaxWidth(),
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
                    text = selectedDate?.let { "Birthday: ${it.month.name} ${it.dayOfMonth}, ${it.year}" }
                        ?: "Choose birthday date",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = HotPink
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Confirm",
            onClick = {
                onNavigate(AuthRoute.Gender.route)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ProfileDetailsPreview(){
    ProfileDetails(onNavigate = {})
}