package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carkzis.ichor.ui.MainViewModel

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
fun ColumnScope.DeleteAllDialogButtonsRow(viewModel: MainViewModel,
                                          deleteAlertRequired: MutableState<Boolean>
) {
    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        DeleteAllConfirmButton(viewModel, deleteAlertRequired)
        DeleteAllRejectButton(deleteAlertRequired)
    }
}