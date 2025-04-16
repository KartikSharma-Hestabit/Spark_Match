package com.hestabit.sparkmatch.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hestabit.sparkmatch.utils.PermissionHandler

@Composable
fun RequestPermissions(
    onPermissionsResult: (Map<String, Boolean>) -> Unit
) {
    var permissionsRequested by remember { mutableStateOf(false) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        onPermissionsResult(permissions)
    }

    DisposableEffect(Unit) {
        if (!permissionsRequested) {
            permissionsLauncher.launch(PermissionHandler.getRequiredPermissions())
            permissionsRequested = true
        }

        onDispose { }
    }
}