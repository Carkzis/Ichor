package com.carkzis.ichor

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.ScalingLazyColumn
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.theme.IchorTypography
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IchorTheme {
                IchorUI()
            }
        }
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IchorUI(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val heartRatePermission = rememberPermissionState(Manifest.permission.BODY_SENSORS)
    // Note: Reset permissions on an emulator using the command "adb shell pm reset-permissions".

    ScalingLazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        autoCentering = AutoCenteringParams(itemIndex = 0)
    ) {
        item { IchorText(modifier = Modifier, style = IchorTypography.title1, stringResourceId = R.string.app_name) }
        if (heartRatePermission.hasPermission) {
            viewModel.initiateDataCollection()
            item { IchorStatefulText(state = viewModel.latestHeartRate) }
        } else {
            item { IchorButton(onClick = { heartRatePermission.launchPermissionRequest() }) }
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
    IchorUI()
}