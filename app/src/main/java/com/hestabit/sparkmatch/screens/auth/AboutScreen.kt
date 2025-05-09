package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.ProfileDetailsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AboutScreen(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    val viewModel: ProfileDetailsViewModel = hiltViewModel()
    val profession by viewModel.profession.collectAsState()
    val aboutText by viewModel.about.collectAsState()
    val professionError by viewModel.professionError.collectAsState()
    val aboutError by viewModel.aboutError.collectAsState()
    val savingError by viewModel.savingError.collectAsState()

    Column(
        modifier = modifier.padding(40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Your Story Matters",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                text = "Help us get to know the person behind the screen!",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = profession,
                onValueChange = { viewModel.updateProfession(it) },
                label = { Text("Profession") },
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (professionError.isEmpty()) OffWhite else Color.Red,
                    focusedBorderColor = if (professionError.isEmpty()) Gray else Color.Red,
                    unfocusedLabelColor = if (professionError.isEmpty()) OffWhite else Color.Red,
                    focusedLabelColor = if (professionError.isEmpty()) Gray else Color.Red,
                    cursorColor = Gray,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = professionError.isNotEmpty(),
                supportingText = {
                    if (professionError.isNotEmpty()) {
                        Text(
                            text = professionError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = modernist
                        )
                    } else {
                        Text(
                            text = "Minimum 2 characters",
                            color = Gray,
                            fontSize = 12.sp,
                            fontFamily = modernist
                        )
                    }
                }
            )

            OutlinedTextField(
                value = aboutText,
                onValueChange = {
                    if (it.length <= 150) {
                        viewModel.updateAbout(it)
                    }
                },
                label = { Text("About ${aboutText.trim().length}/150") },
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = if (aboutError.isEmpty()) OffWhite else Color.Red,
                    focusedBorderColor = if (aboutError.isEmpty()) Gray else Color.Red,
                    unfocusedLabelColor = if (aboutError.isEmpty()) OffWhite else Color.Red,
                    focusedLabelColor = if (aboutError.isEmpty()) Gray else Color.Red,
                    cursorColor = Gray,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                isError = aboutError.isNotEmpty(),
                supportingText = {
                    if (aboutError.isNotEmpty()) {
                        Text(
                            text = aboutError,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = modernist
                        )
                    } else {
                        Text(
                            text = "Minimum 50 characters",
                            color = Gray,
                            fontSize = 12.sp,
                            fontFamily = modernist
                        )
                    }
                }
            )
        }

        // Display general saving error if present
        if (!savingError.isNullOrEmpty()) {
            Text(
                text = savingError ?: "",
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = modernist
            )
        }

        Spacer(modifier = modifier.weight(1f))

        DefaultButton(
            text = "Continue",
            onClick = {
                viewModel.saveAboutDetails { success ->
                    if (success) {
                        onNavigate(AuthRoute.PhotoUpload.route)
                    } else {
                        Log.e("AboutScreen", "Failed to save about details")
                    }
                }
            }
        )
    }
}