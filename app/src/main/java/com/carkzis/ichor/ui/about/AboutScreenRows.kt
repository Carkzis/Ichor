package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
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

@Composable
internal fun DeleteAllRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_delete_all_row_tag))
    ) {
        AboutDeleteAllIcon()
        AboutScreenDeleteAllText(modifier)
    }
}

@Composable
internal fun AboutSamplingSpeedsRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_sampling_speeds_row_tag))
    ) {
        AboutSamplingSpeedIcon()
        AboutScreenSamplingSpeedsColumn(modifier)
    }
}

@Composable
internal fun AboutScreenSlowSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_slow_sampling_row_tag))
    ) {
        AboutSlowSamplingSpeedIcon()
        AboutScreenSlowSamplingDescriptionText(modifier)
    }
}

@Composable
internal fun AboutScreenDefaultSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_default_sampling_row_tag))
    ) {
        AboutDefaultSamplingSpeedIcon()
        AboutScreenDefaultSamplingDescriptionText(modifier)
    }
}

@Composable
internal fun AboutScreenFastSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_fast_sampling_row_tag))
    ) {
        AboutFastSamplingSpeedIcon()
        AboutScreenFastSamplingDescriptionText(modifier)
    }
}