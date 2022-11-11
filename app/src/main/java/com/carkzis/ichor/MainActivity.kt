package com.carkzis.ichor

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.proto.DataProto
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.theme.IchorTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.sql.Time
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

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun IchorUI(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val heartRatePermission = rememberPermissionState(Manifest.permission.BODY_SENSORS)
    // Note: Reset permissions on an emulator using the command "adb shell pm reset-permissions".
    // TODO: DELETE ITEM functionality (SwipeToDismiss).
    // TODO: DELETE ALL functionality (with dialog?)
    // TODO: Change sampling time and show current sampling time (in settings view/dialog?)
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        val heartRates by viewModel.latestHeartRateList.collectAsState()
        val shouldInitiateDataCollection by remember { mutableStateOf(AtomicBoolean(true)) }

        ScalingLazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState
        ) {
            // APP ICON
            item {
                Icon(
                    imageVector = Icons.Rounded.MonitorHeart,
                    contentDescription = "Main heartbeat icon for app.",
                    tint = IchorColorPalette.secondary
                )
            }

            // TITLE
            item {
                IchorText(
                    modifier = modifier,
                    style = IchorTypography.title1,
                    stringResourceId = R.string.app_name
                )
            }

            // AVAILABILITY
            item {
                IchorStatefulText(
                    state = viewModel.latestAvailability,
                    modifier = modifier,
                    style = IchorTypography.body2,
                    prefix = "Availability: "
                )
            }

            // HEARTRATES
            if (heartRatePermission.hasPermission) {
                if (shouldInitiateDataCollection.get()) {
                    shouldInitiateDataCollection.getAndSet(false)
                    viewModel.initiateDataCollection()
                }
                item {
                    IchorStatefulText(
                        state = viewModel.latestHeartRate,
                        modifier = modifier.padding(bottom = 8.dp),
                        suffix = " bpm"
                    )
                }
                items(
                    items = heartRates,
                    key = { it.pk }
                ) { currentHeartRateData ->
                    val dismissState = rememberDismissState {
                        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                            viewModel.deleteHeartRate(currentHeartRateData.pk)
                        }
                        true
                    }

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
            } else if (!heartRatePermission.permissionRequested) {
                item { IchorButton(onClick = { heartRatePermission.launchPermissionRequest() }) }
            } else {
                item {
                    IchorText(
                        modifier = modifier,
                        style = IchorTypography.body2,
                        stringResourceId = R.string.app_permission_was_denied
                    )
                }
            }
        }
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
fun IchorUIPreview() {
    IchorUI(viewModel = viewModel())
}