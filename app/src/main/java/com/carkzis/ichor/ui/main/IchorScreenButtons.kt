package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.R
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.ui.IchorButton
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.NarrowedPermissionStateAdapter
import com.carkzis.ichor.utils.SamplingSpeed

@Composable
internal fun PermissionButton(heartRatePermissionProvider: NarrowedPermissionStateAdapter) {
    IchorButton(
        contentDescription = stringResource(R.string.ichor_permission_button),
        onClick = { heartRatePermissionProvider.launchPermissionRequest() })
}

@Composable
internal fun DeleteAllButton(
    viewModel: MainViewModel,
    modifier: Modifier
) {
    val deleteAlertRequired = remember { mutableStateOf(false) }

    IchorButton(
        modifier = modifier.size(24.dp),
        onClick = { deleteAlertRequired.value = true },
        iconImage = Icons.Rounded.Delete,
        contentDescription = stringResource(R.string.ichor_delete_all_button)
    )

    Dialog(
        showDialog = deleteAlertRequired.value,
        onDismissRequest = {
            deleteAlertRequired.value = false
        },
        content = {
            DeleteAllDialogContent(deleteAlertRequired, modifier, viewModel)
        }
    )
}

@Composable
fun SamplingSpeedChangeButton(viewModel: MainViewModel, modifier: Modifier) {
    val samplingSpeedAlertRequired = remember { mutableStateOf(false) }
    val currentSamplingSpeed by viewModel.currentSamplingSpeed.collectAsState()

    IchorButton(
        modifier = modifier
            .size(24.dp)
            .padding(all = 0.dp),
        onClick = { samplingSpeedAlertRequired.value = true },
        iconImage = Icons.Rounded.Speed,
        contentDescription = stringResource(R.string.ichor_sampling_speed_change)
    )

    Dialog(
        showDialog = samplingSpeedAlertRequired.value,
        onDismissRequest = {
            samplingSpeedAlertRequired.value = false
        },
        content = {
            SamplingSpeedChangeDialogContent(
                samplingSpeedAlertRequired,
                modifier,
                viewModel,
                currentSamplingSpeed
            )
        }
    )
}

@Composable
internal fun AboutButton(modifier: Modifier, onClickAbout: () -> Unit) {
    IchorButton(
        modifier = modifier
            .size(24.dp)
            .padding(all = 0.dp),
        onClick = onClickAbout,
        iconImage = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(R.string.ichor_about_button)
    )
}

@Composable
internal fun DeleteAllRejectButton(deleteAlertRequired: MutableState<Boolean>) {
    IchorButton(
        iconImage = Icons.Rounded.Close,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_delete_all_reject)
    ) {
        deleteAlertRequired.value = false
    }
}

@Composable
internal fun DeleteAllConfirmButton(
    viewModel: MainViewModel,
    deleteAlertRequired: MutableState<Boolean>
) {
    IchorButton(
        iconImage = Icons.Rounded.Done,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_delete_all_confirm)
    ) {
        viewModel.deleteAllHeartRates()
        deleteAlertRequired.value = false
    }
}

@Composable
internal fun FastSamplingSpeedButton(
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>
) {
    IchorButton(
        iconImage = Icons.Rounded.DirectionsBike,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_fast_sampling_speed)
    ) {
        viewModel.changeSampleRate(SamplingSpeed.FAST)
        samplingSpeedAlertRequired.value = false
    }
}

@Composable
internal fun DefaultSamplingSpeedButton(
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>
) {
    IchorButton(
        iconImage = Icons.Rounded.DirectionsRun,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_default_sampling_speed)
    ) {
        viewModel.changeSampleRate(SamplingSpeed.DEFAULT)
        samplingSpeedAlertRequired.value = false
    }
}

@Composable
internal fun SlowSamplingSpeedButton(
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>
) {
    IchorButton(
        iconImage = Icons.Rounded.DirectionsWalk,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_slow_sampling_speed)
    ) {
        viewModel.changeSampleRate(SamplingSpeed.SLOW)
        samplingSpeedAlertRequired.value = false
    }
}

@Composable
internal fun DeleteOneRejectButton(deleteAlertRequired: MutableState<Boolean>) {
    IchorButton(
        iconImage = Icons.Rounded.Close,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_delete_single_reject)
    ) {
        deleteAlertRequired.value = false
    }
}

@Composable
internal fun DeleteOneConfirmButton(
    viewModel: MainViewModel,
    currentHeartRateData: DomainHeartRate
) {
    IchorButton(
        iconImage = Icons.Rounded.Done,
        modifier = Modifier.size(32.dp),
        contentDescription = stringResource(R.string.ichor_delete_single_confirm)
    ) {
        viewModel.deleteHeartRate(currentHeartRateData.pk)
    }
}