@file:OptIn(ExperimentalPermissionsApi::class)

package com.carkzis.ichor.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow

class NarrowedPermissionStateAdapterImpl(private val permissionState: PermissionState) : NarrowedPermissionStateAdapter {

    private var hasPermission = MutableStateFlow(false)
    private var permissionRequested = MutableStateFlow(false)

    override fun getPermission(): MutableStateFlow<Boolean> {
        hasPermission.value = permissionState.hasPermission
        return hasPermission
    }

    override fun getPermissionRequested(): MutableStateFlow<Boolean> {
        permissionRequested.value = permissionState.permissionRequested
        return permissionRequested
    }

    override fun launchPermissionRequest() {
        permissionState.launchPermissionRequest()
    }

}