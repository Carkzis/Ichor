package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import com.carkzis.ichor.R
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.MainViewModel
import timber.log.Timber

@Composable
internal fun DeleteAllDialogContent(
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
    SamplingSpeedChangeDialogContentColumn(
        modifier,
        viewModel,
        samplingSpeedAlertRequired,
        currentSamplingSpeed
    )
}

@Composable
internal fun DeleteOneDialogContent(
    deleteAlertRequired: MutableState<Boolean>,
    currentHeartRateData: DomainHeartRate,
    viewModel: MainViewModel
) {
    Timber.e("Dialog for deleting item raised: ${deleteAlertRequired.value}")
    DeleteOneDialogContentColumn(currentHeartRateData, viewModel, deleteAlertRequired)
}
