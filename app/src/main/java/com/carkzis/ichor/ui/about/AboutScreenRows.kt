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
            AboutAboutText(modifier)
        }
    }
}

internal fun ScalingLazyListScope.PermissionsRow(modifier: Modifier) {
    item {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.about_permissions_row_tag))
        ) {
            PermissionIcon()
            PermissionsText(modifier)
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
        DeleteAllIcon()
        DeleteAllText(modifier)
    }
}

@Composable
internal fun AboutSamplingSpeedsRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_sampling_speeds_row_tag))
    ) {
        SamplingSpeedIcon()
        AboutScreenSamplingSpeedsColumn(modifier)
    }
}

@Composable
internal fun SlowSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_slow_sampling_row_tag))
    ) {
        SlowSamplingSpeedIcon()
        SlowSamplingDescriptionText(modifier)
    }
}

@Composable
internal fun DefaultSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_default_sampling_row_tag))
    ) {
        DefaultSamplingSpeedIcon()
        DefaultSamplingDescriptionText(modifier)
    }
}

@Composable
internal fun FastSamplingRow(modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(R.string.about_fast_sampling_row_tag))
    ) {
        FastSamplingSpeedIcon()
        FastSamplingDescriptionText(modifier)
    }
}