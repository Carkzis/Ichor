package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ScalingLazyListScope
import androidx.wear.compose.material.items
import com.carkzis.ichor.R
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.ui.IchorButton
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.PermissionFacade
import java.util.concurrent.atomic.AtomicBoolean

internal fun ScalingLazyListScope.InvariantColumnComponents(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    item { MainIcon() }
    item { TitleText(modifier = modifier) }
    item {
        AvailabilityText(modifier = modifier, state = viewModel.latestAvailability)
    }
}

internal fun ScalingLazyListScope.IchorVariantColumnComponents(
    hasPermission: Boolean,
    shouldInitiateDataCollection: AtomicBoolean,
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit,
    heartRates: List<DomainHeartRate>,
    permissionRequested: Boolean,
    heartRatePermissionProvider: PermissionFacade
) {
    if (hasPermission) {
        ColumnComponentsWherePermissionGranted(
            shouldInitiateDataCollection,
            viewModel,
            modifier,
            onClickAbout,
            heartRates
        )
    } else if (!permissionRequested) {
        ColumnComponentsWherePermissionsNeedRequesting(
            heartRatePermissionProvider,
            modifier,
            onClickAbout
        )
    } else {
        ColumnComponentsWherePermissionsDenied(modifier, onClickAbout)
    }
}

internal fun ScalingLazyListScope.ColumnComponentsWherePermissionsDenied(
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item { PermissionsInstructionsText(modifier) }
    item { AboutButton(modifier = modifier, onClickAbout) }
}

internal fun ScalingLazyListScope.ColumnComponentsWherePermissionsNeedRequesting(
    heartRatePermissionProvider: PermissionFacade,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item {
        PermissionButton(heartRatePermissionProvider)
    }
    item { AboutButton(modifier = modifier, onClickAbout) }
}

internal fun ScalingLazyListScope.ColumnComponentsWherePermissionGranted(
    shouldInitiateDataCollection: AtomicBoolean,
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit,
    heartRates: List<DomainHeartRate>
) {
    initiateDataCollectionOnce(shouldInitiateDataCollection, viewModel)
    item { SamplingSpeedRow(modifier, viewModel) }
    item {
        LatestHeartRateText(modifier = modifier, state = viewModel.latestHeartRate)
    }
    item { ButtonsRow(viewModel, modifier, onClickAbout) }
    items(
        items = heartRates,
        key = { it.pk }
    ) { currentHeartRateData ->
        HeartRateItem(viewModel, currentHeartRateData, modifier)
    }
}

@Composable
internal fun DeleteAllDialogContentColumn(
    modifier: Modifier,
    viewModel: MainViewModel,
    deleteAlertRequired: MutableState<Boolean>
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DeleteHeartbeatIcon()
        DeleteAllFinalChanceText(modifier)
        DeleteAllDialogButtonsRow(viewModel, deleteAlertRequired)
    }
}

@Composable
internal fun SamplingSpeedChangeDialogContentColumn(
    modifier: Modifier,
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>,
    currentSamplingSpeed: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChangeSamplingSpeedIcon()
        SamplingSpeedChangeQuestionText(modifier)
        Spacer(modifier = Modifier.height(8.dp))
        SamplingSpeedChangeChoicesColumn(
            modifier,
            viewModel,
            samplingSpeedAlertRequired,
            currentSamplingSpeed
        )
    }
}

@Composable
internal fun ColumnScope.SamplingSpeedChangeChoicesColumn(
    modifier: Modifier,
    viewModel: MainViewModel,
    samplingSpeedAlertRequired: MutableState<Boolean>,
    currentSamplingSpeed: String
) {
    Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
        SlowSamplingChoiceRow(modifier, viewModel, samplingSpeedAlertRequired, currentSamplingSpeed)
        DefaultSamplingChoiceRow(
            modifier,
            viewModel,
            samplingSpeedAlertRequired,
            currentSamplingSpeed
        )
        FastSamplingChoiceRow(modifier, viewModel, samplingSpeedAlertRequired, currentSamplingSpeed)
    }
}

@Composable
internal fun DeleteOneDialogContentColumn(
    currentHeartRateData: DomainHeartRate,
    viewModel: MainViewModel,
    deleteAlertRequired: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DeleteHeartbeatIcon()
        DeleteOneRecordQueryText(currentHeartRateData)
        DeleteOneDialogButtonsRow(viewModel, currentHeartRateData, deleteAlertRequired)
    }
}