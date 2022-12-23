package com.carkzis.ichor.utils

import kotlinx.coroutines.flow.MutableStateFlow

interface PermissionFacade {
    fun getPermission(): MutableStateFlow<Boolean>
    fun getPermissionRequested(): MutableStateFlow<Boolean>
    fun launchPermissionRequest()
}