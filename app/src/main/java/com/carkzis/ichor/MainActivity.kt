package com.carkzis.ichor

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.theme.IchorTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.sql.Time

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()

        Timber.e("CREATIONS")
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
    val heartRatePermission = rememberPermissionState(Manifest.permission.BODY_SENSORS)
    // Note: Reset permissions on an emulator using the command "adb shell pm reset-permissions".
    // TODO: Need to show availability.
    // TODO: DELETE ITEM functionality.
    // TODO: DELETE ALL functionality (with dialog?)
    // TODO: Change sampling time and show current sampling time (in settings view/dialog?)
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        val heartRates by viewModel.latestHeartRateList.collectAsState()
        var shouldInitiateDataCollection by remember { mutableStateOf(true) }

        ScalingLazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = ScalingLazyListState()
        ) {

            // TITLE
            item {
                IchorText(
                    modifier = Modifier,
                    style = IchorTypography.title1,
                    stringResourceId = R.string.app_name
                )
            }

            // CURRENT HEARTRATE
            if (heartRatePermission.hasPermission) {
                if (shouldInitiateDataCollection) {
                    // TODO: Make this atomic? Still seeing some double initiations.
                    shouldInitiateDataCollection = false
                    viewModel.initiateDataCollection()
                }
                item { IchorStatefulText(state = viewModel.latestHeartRate) }
            } else {
                item { IchorButton(onClick = { heartRatePermission.launchPermissionRequest() }) }
            }

            Timber.e("NEW HEARTRATE LIST SIZE IS: ${heartRates.size}")

            // LIST OF HEARTRATES
            items(
                items = heartRates
            ) { currentHeartRateData ->
                IchorCard(
                    time = currentHeartRateData.date,
                    mainInfo = currentHeartRateData.value.toString()
                )
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