package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.R
import com.carkzis.ichor.ui.IchorButton
import com.carkzis.ichor.ui.MainViewModel

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
            IchorDeleteAllDialogContent(deleteAlertRequired, modifier, viewModel)
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