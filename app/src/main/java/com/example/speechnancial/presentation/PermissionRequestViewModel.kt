package com.example.speechnancial.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PermissionRequestViewModel: ViewModel() {

    private val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}