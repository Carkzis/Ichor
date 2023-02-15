package com.carkzis.ichor.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.health.services.client.data.Availability
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorStatefulText
import com.carkzis.ichor.ui.IchorText
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun PermissionsInstructionsText(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.body2,
        stringResourceId = R.string.app_permission_was_denied
    )
}

@Composable
fun TitleText(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.title1,
        stringResourceId = R.string.app_name
    )
}

@Composable
fun AvailabilityText(modifier: Modifier, state: StateFlow<Availability>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        style = IchorTypography.body2,
        prefix = stringResource(R.string.ichor_availability_prefix)
    )
}

@Composable
fun LatestHeartRateText(modifier: Modifier, state: StateFlow<Double>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        suffix = stringResource(R.string.ichor_bpm_suffix)
    )
}