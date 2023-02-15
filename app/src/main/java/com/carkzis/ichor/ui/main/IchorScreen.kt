@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class,
    ExperimentalPermissionsApi::class
)

package com.carkzis.ichor.ui.main

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.Availability
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.*
import com.carkzis.ichor.R.string
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorCard
import com.carkzis.ichor.ui.IchorStatefulText
import com.carkzis.ichor.ui.IchorText
import com.carkzis.ichor.ui.MainViewModel
import com.carkzis.ichor.utils.DefaultPermissionFacade
import com.carkzis.ichor.utils.PermissionFacade
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicBoolean

@Composable
fun IchorScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    heartRatePermissionFacade: PermissionFacade = DefaultPermissionFacade(
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
            heartRatePermissionFacade,
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
    heartRatePermissionProvider: PermissionFacade,
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
    IchorScreen(viewModel = viewModel()) {}
}