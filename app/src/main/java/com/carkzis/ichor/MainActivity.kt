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
import androidx.compose.material.icons.rounded.MonitorHeart
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
import androidx.wear.compose.material.dialog.Alert
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IchorUI(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Note: Reset permissions on an emulator using the command "adb shell pm reset-permissions".

    // TODO: DELETE ITEM functionality (SwipeToDismiss).
    // TODO: DELETE ALL functionality (with dialog?)
    // TODO: Change sampling time and show current sampling time (in settings view/dialog?)

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
        if (heartRatePermission.hasPermission) {
            initiateDataCollectionOnce(shouldInitiateDataCollection, viewModel)
            item {
                DisplayLatestHeartRate(modifier = modifier, state = viewModel.latestHeartRate)
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
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Delete your heartbeat record of ${currentHeartRateData.value} bpm dated ${currentHeartRateData.date}?", textAlign = TextAlign.Center)
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IchorButton {
                        viewModel.deleteHeartRate(currentHeartRateData.pk)
                    }
                    IchorButton {
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
        modifier = modifier.padding(bottom = 8.dp),
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