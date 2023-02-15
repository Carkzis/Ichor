package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.carkzis.ichor.R
import com.carkzis.ichor.ui.IchorButton
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.SamplingSpeed

@Composable
internal fun ButtonsRow(
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    Row {
        SamplingSpeedChangeButton(viewModel = viewModel, modifier = modifier)
        Spacer(modifier = Modifier.width(8.dp))
        AboutButton(modifier = modifier, onClickAbout)
        Spacer(modifier = Modifier.width(8.dp))
        DeleteAllButton(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
internal fun SamplingSpeedRow(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    Row {
        SamplingSpeedText(modifier, viewModel)
    }
}

@Composable
internal fun ColumnScope.DeleteAllDialogButtonsRow(viewModel: MainViewModel,
                                          deleteAlertRequired: MutableState<Boolean>
) {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        DeleteAllConfirmButton(viewModel, deleteAlertRequired)
        DeleteAllRejectButton(deleteAlertRequired)
    }
}

@Composable
internal fun FastSamplingChoiceRow(
    modifier: Modifier,
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>,
    currentSamplingSpeed: String
) {
    Row(modifier = modifier.testTag(stringResource(R.string.ichor_fast_sampling_row_tag))) {
        FastSamplingSpeedButton(viewModel, samplingSpeedAlertRequired)
        if (currentSamplingSpeed == SamplingSpeed.FAST.toString()) {
            TickIcon()
        }
    }
}



@Composable
internal fun DefaultSamplingChoiceRow(
    modifier: Modifier,
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>,
    currentSamplingSpeed: String
) {
    Row(modifier = modifier.testTag(stringResource(R.string.ichor_default_sampling_row_tag))) {
        DefaultSamplingSpeedButton(viewModel, samplingSpeedAlertRequired)
        if (currentSamplingSpeed == SamplingSpeed.DEFAULT.toString()) {
            TickIcon()
        }
    }
}



@Composable
internal fun SlowSamplingChoiceRow(
    modifier: Modifier,
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>,
    currentSamplingSpeed: String
) {
    Row(modifier = modifier.testTag(stringResource(R.string.ichor_slow_sampling_row_tag))) {
        SlowSamplingSpeedButton(viewModel, samplingSpeedAlertRequired)
        if (currentSamplingSpeed == SamplingSpeed.SLOW.toString()) {
            TickIcon()
        }
    }
}
