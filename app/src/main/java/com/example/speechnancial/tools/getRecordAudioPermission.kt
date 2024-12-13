package com.example.speechnancial.tools

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.viewmodel.PermissionRequestViewModel

@Composable
fun getRecordAudioPermission() : ManagedActivityResultLauncher<String, Boolean> {
    val viewModel = viewModel<PermissionRequestViewModel>()

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.RECORD_AUDIO,
                isGranted = isGranted
            )
        }
    )
}