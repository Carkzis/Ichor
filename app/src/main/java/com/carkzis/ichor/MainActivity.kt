package com.carkzis.ichor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import com.carkzis.ichor.theme.IchorTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.ScalingLazyColumn
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IchorTheme {
                IchorUI()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun IchorUI(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    val heartRatePermission = rememberPermissionState(android.Manifest.permission.BODY_SENSORS)
    when (heartRatePermission.hasPermission) {
        true -> {
            viewModel.initiateDataCollection()
            val heartState by viewModel.latestHeartRate.collectAsState()
            ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), autoCentering = AutoCenteringParams(itemIndex = 0)) {
                item { IchorText(stringResourceId = R.string.hello_world) }
                item { IchorStatefulText(text = heartState.toString()) }
            }
        }
        false -> {
            ScalingLazyColumn(modifier = Modifier.fillMaxWidth(), autoCentering = AutoCenteringParams(itemIndex = 0)) {
                item { IchorText(stringResourceId = R.string.hello_world) }
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
    IchorUI()
}