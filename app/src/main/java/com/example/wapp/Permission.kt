package com.example.wapp

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.protobuf.ExperimentalApi

@ExperimentalPermissionsApi
@Composable
fun RequestPermissions(permission: String, onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(permission)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    }
}