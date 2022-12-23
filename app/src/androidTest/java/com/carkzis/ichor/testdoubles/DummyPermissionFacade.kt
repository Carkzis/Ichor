package com.carkzis.ichor.testdoubles

import com.carkzis.ichor.utils.PermissionFacade
import kotlinx.coroutines.flow.MutableStateFlow

class DummyPermissionFacade(
    hasPermissionAlready: Boolean = false,
    private var willGivePermission: Boolean = false,
    private var permissionPreviouslyDenied: Boolean = false
) : PermissionFacade {
    private var dummyHasPermission = MutableStateFlow(hasPermissionAlready)
    private var dummyPermissionRequested = MutableStateFlow(permissionPreviouslyDenied)

    override fun getPermission(): MutableStateFlow<Boolean> = dummyHasPermission
    override fun getPermissionRequested(): MutableStateFlow<Boolean> = dummyPermissionRequested
    override fun launchPermissionRequest() {
        // Cannot give permission if we previously denied permission.
        willGivePermission = if (permissionPreviouslyDenied) false else willGivePermission
        dummyHasPermission.value = willGivePermission
        // If we will give permission, we haven't previously denied permission.
        permissionPreviouslyDenied = !willGivePermission
        dummyPermissionRequested.value = permissionPreviouslyDenied
    }
}