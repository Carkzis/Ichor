@file:OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)

package com.carkzis.ichor

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.Availability
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.theme.IchorTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()

        setContent {
            IchorTheme {
                IchorUI(viewModel = viewModel)
            }
        }
    }

}

@Composable
fun IchorUI(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Note: Reset permissions on an emulator using the command "adb shell pm reset-permissions".

    // TODO: Change sampling time and show current sampling time (in settings view/dialog?)
    // TODO: Look into constant recomposing when opening dialog.

    val heartRatePermission = rememberPermissionState(Manifest.permission.BODY_SENSORS)
    val listState = rememberScalingLazyListState()
    val heartRates by viewModel.latestHeartRateList.collectAsState()
    val shouldInitiateDataCollection by remember { mutableStateOf(AtomicBoolean(true)) }

    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        DisplayUIItems(
            modifier,
            listState,
            viewModel,
            heartRatePermission,
            shouldInitiateDataCollection,
            heartRates
        )
    }
}

@Composable
private fun DisplayUIItems(
    modifier: Modifier,
    listState: ScalingLazyListState,
    viewModel: MainViewModel,
    heartRatePermission: PermissionState,
    shouldInitiateDataCollection: AtomicBoolean,
    heartRates: List<DomainHeartRate>
) {
    ScalingLazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
        autoCentering = AutoCenteringParams(itemIndex = 0),
        state = listState
    ) {
        item { DisplayMainIcon() }
        item { DisplayTitle(modifier = modifier) }
        item {
            DisplayAvailability(modifier = modifier, state = viewModel.latestAvailability)
        }
        item {
            Row {
                IchorStatefulText(
                    modifier = modifier,
                    style = IchorTypography.body2,
                    state = viewModel.currentSamplingSpeed,
                    prefix = "Sampling Speed: "
                )
            }

        }
        if (heartRatePermission.hasPermission) {
            initiateDataCollectionOnce(shouldInitiateDataCollection, viewModel)
            item {
                DisplayLatestHeartRate(modifier = modifier, state = viewModel.latestHeartRate)
            }
            item {
                Row {
                    DisplaySamplingSpeedChangeButton(viewModel = viewModel, modifier = modifier)
                    Spacer(modifier = Modifier.width(8.dp))
                    DisplayDeleteAllButton(viewModel = viewModel, modifier = modifier)
                }
            }
            items(
                items = heartRates,
                key = { it.pk }
            ) { currentHeartRateData ->
                DisplayHeartRateItem(viewModel, currentHeartRateData, modifier)
            }
        } else if (!heartRatePermission.permissionRequested) {
            item { IchorButton(onClick = { heartRatePermission.launchPermissionRequest() }) }
        } else {
            item { DisplayPermissionsInstructions(modifier) }
        }
    }
}

@Composable
private fun DisplayPermissionsInstructions(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.body2,
        stringResourceId = R.string.app_permission_was_denied
    )
}

@Composable
private fun DisplayDeleteAllButton(
    viewModel: MainViewModel,
    modifier: Modifier
) {
    var deleteAlertRequired by remember { mutableStateOf(false) }

    IchorButton(
        modifier = modifier.size(24.dp),
        onClick = { deleteAlertRequired = true },
        iconImage = Icons.Rounded.Delete
    )

    Dialog(
        showDialog = deleteAlertRequired,
        onDismissRequest = {
            deleteAlertRequired = false
        },
        content = {
            Timber.e("Dialog for deleting all items raised: $deleteAlertRequired")
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DeleteHeartbeatIcon()
                Text(
                    style = IchorTypography.body2,
                    modifier = modifier.padding(start = 36.dp, end = 36.dp),
                    textAlign = TextAlign.Center,
                    text = "Delete all your heartbeats? This cannot be undone."
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IchorButton(iconImage = Icons.Rounded.Done, modifier = Modifier.size(32.dp)) {
                        viewModel.deleteAllHeartRates()
                        deleteAlertRequired = false
                    }
                    IchorButton(iconImage = Icons.Rounded.Close, modifier = Modifier.size(32.dp)) {
                        deleteAlertRequired = false
                    }
                }
            }
        }
    )
}

@Composable
fun DisplaySamplingSpeedChangeButton(viewModel: MainViewModel, modifier: Modifier) {
    var samplingSpeedAlertRequired by remember { mutableStateOf(false) }
    val currentSamplingSpeed by viewModel.currentSamplingSpeed.collectAsState()

    IchorButton(
        modifier = modifier
            .size(24.dp)
            .padding(all = 0.dp),
        onClick = { samplingSpeedAlertRequired = true },
        iconImage = Icons.Rounded.Speed
    )

    Dialog(
        showDialog = samplingSpeedAlertRequired,
        onDismissRequest = {
            samplingSpeedAlertRequired = false
        },
        content = {
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
                    text = "Change sampling speed?"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Row {
                        IchorButton(iconImage = Icons.Rounded.DirectionsWalk, modifier = Modifier.size(32.dp)) {
                            viewModel.changeSampleRate(SamplingSpeed.SLOW)
                            samplingSpeedAlertRequired = false
                        }
                        if (currentSamplingSpeed == SamplingSpeed.SLOW.toString()) {
                            TickIcon()
                        }
                    }
                    Row {
                        IchorButton(iconImage = Icons.Rounded.DirectionsRun, modifier = Modifier.size(32.dp)) {
                            viewModel.changeSampleRate(SamplingSpeed.DEFAULT)
                            samplingSpeedAlertRequired = false
                        }
                        if (currentSamplingSpeed == SamplingSpeed.DEFAULT.toString()) {
                            TickIcon()
                        }
                    }
                    Row {
                        IchorButton(iconImage = Icons.Rounded.DirectionsBike, modifier = Modifier.size(32.dp)) {
                            viewModel.changeSampleRate(SamplingSpeed.FAST)
                            samplingSpeedAlertRequired = false
                        }
                        if (currentSamplingSpeed == SamplingSpeed.FAST.toString()) {
                            TickIcon()
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DisplayHeartRateItem(
    viewModel: MainViewModel,
    currentHeartRateData: DomainHeartRate,
    modifier: Modifier
) {
    var deleteAlertRequired by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState {
        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
            deleteAlertRequired = true
        }
        true
    }
    if (!deleteAlertRequired) {
        LaunchedEffect(Unit) {
            dismissState.reset()
        }
    }

    Timber.e("Delete item raised?: $deleteAlertRequired")
    Dialog(
        showDialog = deleteAlertRequired,
        onDismissRequest = {
            deleteAlertRequired = false
        },
        content = {
            Timber.e("Dialog for deleting item raised: $deleteAlertRequired")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DeleteHeartbeatIcon()
                Text(
                    style = IchorTypography.body2,
                    modifier = Modifier.padding(start = 36.dp, end = 36.dp),
                    text = "Delete your heartbeat record of ${currentHeartRateData.value} bpm dated ${currentHeartRateData.date}?",
                    textAlign = TextAlign.Center
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IchorButton(iconImage = Icons.Rounded.Done, modifier = Modifier.size(32.dp)) {
                        viewModel.deleteHeartRate(currentHeartRateData.pk)
                    }
                    IchorButton(iconImage = Icons.Rounded.Close, modifier = Modifier.size(32.dp)) {
                        deleteAlertRequired = false
                    }
                }
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = { Box(modifier = modifier.fillMaxSize()) },
        dismissContent = {
            IchorCard(
                time = currentHeartRateData.date,
                content = {
                    Text(
                        "${currentHeartRateData.value} bpm",
                        color = IchorColorPalette.onSecondary
                    )
                }
            )
        })
}

private fun initiateDataCollectionOnce(
    shouldInitiateDataCollection: AtomicBoolean,
    viewModel: MainViewModel
) {
    if (shouldInitiateDataCollection.get()) {
        shouldInitiateDataCollection.getAndSet(false)
        viewModel.initiateDataCollection()
    }
}

@Composable
fun DisplayMainIcon() {
    Icon(
        imageVector = Icons.Rounded.MonitorHeart,
        contentDescription = "Main heartbeat icon for app.",
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun DeleteHeartbeatIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Delete,
        contentDescription = "Delete heartbeat icon for app.",
        tint = IchorColorPalette.secondary
    )
}


@Composable
fun ChangeSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Speed,
        contentDescription = "Change heartbeat sampling speed.",
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun TickIcon() {
    Icon(
        modifier = Modifier.size(32.dp),
        imageVector = Icons.Rounded.Done,
        contentDescription = "Affirmation icon.",
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun DisplayTitle(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.title1,
        stringResourceId = R.string.app_name
    )
}

@Composable
fun DisplayAvailability(modifier: Modifier, state: StateFlow<Availability>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        style = IchorTypography.body2,
        prefix = "Availability: "
    )
}

@Composable
fun DisplayLatestHeartRate(modifier: Modifier, state: StateFlow<Double>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        suffix = " bpm"
    )
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
fun IchorUIPreview() {
    IchorUI(viewModel = viewModel())
}