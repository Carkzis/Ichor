@file:OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)

package com.carkzis.ichor.ui.main

import android.Manifest
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.carkzis.ichor.*
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.NarrowedPermissionStateAdapterImpl
import com.carkzis.ichor.utils.NarrowedPermissionStateAdapter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.atomic.AtomicBoolean

@Composable
fun IchorScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    heartRatePermissionStateAdapter: NarrowedPermissionStateAdapter = NarrowedPermissionStateAdapterImpl(
        rememberPermissionState(Manifest.permission.BODY_SENSORS)
    ),
    onClickAbout: () -> Unit = {}
) {
    val listState = rememberScalingLazyListState()
    val heartRates by viewModel.latestHeartRateList.collectAsState()
    val shouldInitiateDataCollection by remember { mutableStateOf(AtomicBoolean(true)) }

    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        IchorBodyComponents(
            modifier,
            listState,
            viewModel,
            heartRatePermissionStateAdapter,
            shouldInitiateDataCollection,
            heartRates,
            onClickAbout
        )
    }
}

@Composable
private fun IchorBodyComponents(
    modifier: Modifier,
    listState: ScalingLazyListState,
    viewModel: MainViewModel,
    heartRatePermissionProvider: NarrowedPermissionStateAdapter,
    shouldInitiateDataCollection: AtomicBoolean,
    heartRates: List<DomainHeartRate>,
    onClickAbout: () -> Unit
) {
    val hasPermission by heartRatePermissionProvider.getPermission().collectAsState()
    val permissionRequested by heartRatePermissionProvider.getPermissionRequested().collectAsState()

    ScalingLazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        InvariantColumnComponents(modifier, viewModel)
        IchorVariantColumnComponents(
            hasPermission,
            shouldInitiateDataCollection,
            viewModel,
            modifier,
            onClickAbout,
            heartRates,
            permissionRequested,
            heartRatePermissionProvider
        )
    }
}
internal fun initiateDataCollectionOnce(
    shouldInitiateDataCollection: AtomicBoolean,
    viewModel: MainViewModel
) {
    if (shouldInitiateDataCollection.get()) {
        shouldInitiateDataCollection.getAndSet(false)
        viewModel.initiateDataCollection()
    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun IchorScreenPreview() {
    // Previews are not currently supported for this screen due to use of a ViewModel.
    IchorScreen(viewModel = viewModel()) {}
}