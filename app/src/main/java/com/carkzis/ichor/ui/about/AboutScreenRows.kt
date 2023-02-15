package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.ScalingLazyListScope
import com.carkzis.ichor.R

internal fun ScalingLazyListScope.AboutAboutRow(modifier: Modifier) {
    item {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.about_about_row_tag))
        ) {
            AboutAboutIcon()
            AboutScreenAboutText(modifier)
        }
    }
}

internal fun ScalingLazyListScope.AboutPermissionsRow(modifier: Modifier) {
    item {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.about_permissions_row_tag))
        ) {
            AboutPermissionIcon()
            AboutScreenPermissionsText(modifier)
        }
    }
}