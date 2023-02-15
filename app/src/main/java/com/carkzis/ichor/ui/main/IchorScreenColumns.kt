package com.carkzis.ichor.ui.main

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    item { PermissionsInstructions(modifier) }
    item { AboutButton(modifier = modifier, onClickAbout) }
}

internal fun ScalingLazyListScope.ColumnComponentsWherePermissionsNeedRequesting(
    heartRatePermissionProvider: PermissionFacade,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item {
        IchorButton(
            contentDescription = stringResource(R.string.ichor_permission_button),
            onClick = { heartRatePermissionProvider.launchPermissionRequest() })
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