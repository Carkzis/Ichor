package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.Availability
import androidx.wear.compose.material.Text
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorStatefulText
import com.carkzis.ichor.ui.IchorText
import com.carkzis.ichor.ui.MainViewModel
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
internal fun TitleText(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.title1,
        stringResourceId = R.string.app_name
    )
}

@Composable
internal fun AvailabilityText(modifier: Modifier, state: StateFlow<Availability>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        style = IchorTypography.body2,
        prefix = stringResource(R.string.ichor_availability_prefix)
    )
}

@Composable
internal fun LatestHeartRateText(modifier: Modifier, state: StateFlow<Double>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        suffix = stringResource(R.string.ichor_bpm_suffix)
    )
}

@Composable
internal fun SamplingSpeedText(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    IchorStatefulText(
        modifier = modifier,
        style = IchorTypography.body2,
        state = viewModel.currentSamplingSpeed,
        prefix = stringResource(R.string.ichor_sample_speed_prefix)
    )
}

@Composable
internal fun DeleteAllFinalChanceText(modifier: Modifier) {
    Text(
        style = IchorTypography.body2,
        modifier = modifier.padding(start = 36.dp, end = 36.dp),
        textAlign = TextAlign.Center,
        text = stringResource(R.string.ichor_delete_all_final)
    )
}

@Composable
internal fun SamplingSpeedChangeQuestionText(modifier: Modifier) {
    Text(
        style = IchorTypography.body2,
        modifier = modifier.padding(start = 36.dp, end = 36.dp),
        textAlign = TextAlign.Center,
        text = stringResource(R.string.ichor_change_sampling_speed_question)
    )
}