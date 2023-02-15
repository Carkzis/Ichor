package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.carkzis.ichor.R
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorButton
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.SamplingSpeed
import timber.log.Timber

@Composable
internal fun IchorDeleteAllDialogContent(
    deleteAlertRequired: MutableState<Boolean>,
    modifier: Modifier,
    viewModel: MainViewModel
) {
    Timber.e("Dialog for deleting all items raised: $deleteAlertRequired")
    DeleteAllDialogContentColumn(modifier, viewModel, deleteAlertRequired)
}

@Composable
internal fun SamplingSpeedChangeDialogContent(
    samplingSpeedAlertRequired: MutableState<Boolean>,
    modifier: Modifier,
    viewModel: MainViewModel,
    currentSamplingSpeed: String
) {
    Timber.e("Dialog for changing sampling speed: $samplingSpeedAlertRequired")
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChangeSamplingSpeedIcon()
        Text(
            style = IchorTypography.body2,
            modifier = modifier.padding(start = 36.dp, end = 36.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.ichor_change_sampling_speed_question)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Row(modifier = modifier.testTag(stringResource(R.string.ichor_slow_sampling_row_tag))) {
                IchorButton(
                    iconImage = Icons.Rounded.DirectionsWalk,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(R.string.ichor_slow_sampling_speed)
                ) {
                    viewModel.changeSampleRate(SamplingSpeed.SLOW)
                    samplingSpeedAlertRequired.value = false
                }
                if (currentSamplingSpeed == SamplingSpeed.SLOW.toString()) {
                    TickIcon()
                }
            }
            Row(modifier = modifier.testTag(stringResource(R.string.ichor_default_sampling_row_tag))) {
                IchorButton(
                    iconImage = Icons.Rounded.DirectionsRun,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(R.string.ichor_default_sampling_speed)
                ) {
                    viewModel.changeSampleRate(SamplingSpeed.DEFAULT)
                    samplingSpeedAlertRequired.value = false
                }
                if (currentSamplingSpeed == SamplingSpeed.DEFAULT.toString()) {
                    TickIcon()
                }
            }
            Row(modifier = modifier.testTag(stringResource(R.string.ichor_fast_sampling_row_tag))) {
                IchorButton(
                    iconImage = Icons.Rounded.DirectionsBike,
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(R.string.ichor_fast_sampling_speed)
                ) {
                    viewModel.changeSampleRate(SamplingSpeed.FAST)
                    samplingSpeedAlertRequired.value = false
                }
                if (currentSamplingSpeed == SamplingSpeed.FAST.toString()) {
                    TickIcon()
                }
            }
        }
    }
}

@Composable
internal fun DeleteOneDialogContent(
    deleteAlertRequired: MutableState<Boolean>,
    currentHeartRateData: DomainHeartRate,
    viewModel: MainViewModel
) {
    Timber.e("Dialog for deleting item raised: ${deleteAlertRequired.value}")
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DeleteHeartbeatIcon()
        Text(
            style = IchorTypography.body2,
            modifier = Modifier.padding(start = 36.dp, end = 36.dp),
            text = "${stringResource(R.string.ichor_delete_record_part_1)}${currentHeartRateData.value}${
                stringResource(
                    R.string.ichor_delete_record_part_2
                )
            }${currentHeartRateData.date}?",
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            IchorButton(
                iconImage = Icons.Rounded.Done,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(R.string.ichor_delete_single_confirm)
            ) {
                viewModel.deleteHeartRate(currentHeartRateData.pk)
            }
            IchorButton(
                iconImage = Icons.Rounded.Close,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(R.string.ichor_delete_single_reject)
            ) {
                deleteAlertRequired.value = false
            }
        }
    }
}