@file:OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)

package com.carkzis.ichor.ui

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.services.client.data.Availability
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.*
import com.carkzis.ichor.R.*
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.utils.DefaultPermissionFacade
import com.carkzis.ichor.utils.PermissionFacade
import com.carkzis.ichor.utils.SamplingSpeed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
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
    // TODO: Look into constant recomposing when opening dialog.
    // TODO: Refactor Compose.

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
        IchorInvariantColumnComponents(modifier, viewModel)
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

private fun ScalingLazyListScope.IchorVariantColumnComponents(
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
        IchorBodyColumnComponentsWherePermissionGranted(
            shouldInitiateDataCollection,
            viewModel,
            modifier,
            onClickAbout,
            heartRates
        )
    } else if (!permissionRequested) {
        IchorBodyColumnComponentsWherePermissionsNeedRequesting(
            heartRatePermissionProvider,
            modifier,
            onClickAbout
        )
    } else {
       IchorBodyColumnComponentsWherePermissionsDenied(modifier, onClickAbout)
    }
}

private fun ScalingLazyListScope.IchorBodyColumnComponentsWherePermissionsDenied(
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item { PermissionsInstructions(modifier) }
    item { AboutButton(modifier = modifier, onClickAbout) }
}

private fun ScalingLazyListScope.IchorBodyColumnComponentsWherePermissionsNeedRequesting(
    heartRatePermissionProvider: PermissionFacade,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item {
        IchorButton(
            contentDescription = stringResource(string.ichor_permission_button),
            onClick = { heartRatePermissionProvider.launchPermissionRequest() })
    }
    item { AboutButton(modifier = modifier, onClickAbout) }
}

private fun ScalingLazyListScope.IchorBodyColumnComponentsWherePermissionGranted(
    shouldInitiateDataCollection: AtomicBoolean,
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit,
    heartRates: List<DomainHeartRate>
) {
    initiateDataCollectionOnce(shouldInitiateDataCollection, viewModel)
    IchorSamplingSpeedRow(modifier, viewModel)
    item {
        DisplayLatestHeartRate(modifier = modifier, state = viewModel.latestHeartRate)
    }
    IchorButtonsRow(viewModel, modifier, onClickAbout)
    IchorHeartRateHistoryList(heartRates, viewModel, modifier)
}

private fun ScalingLazyListScope.IchorHeartRateHistoryList(
    heartRates: List<DomainHeartRate>,
    viewModel: MainViewModel,
    modifier: Modifier
) {
    items(
        items = heartRates,
        key = { it.pk }
    ) { currentHeartRateData ->
        HeartRateItem(viewModel, currentHeartRateData, modifier)
    }
}

private fun ScalingLazyListScope.IchorButtonsRow(
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    item {
        Row {
            SamplingSpeedChangeButton(viewModel = viewModel, modifier = modifier)
            Spacer(modifier = Modifier.width(8.dp))
            AboutButton(modifier = modifier, onClickAbout)
            Spacer(modifier = Modifier.width(8.dp))
            DeleteAllButton(viewModel = viewModel, modifier = modifier)
        }
    }
}

private fun ScalingLazyListScope.IchorSamplingSpeedRow(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    item {
        Row {
            IchorStatefulText(
                modifier = modifier,
                style = IchorTypography.body2,
                state = viewModel.currentSamplingSpeed,
                prefix = stringResource(string.ichor_sample_speed_prefix)
            )
        }
    }
}

private fun ScalingLazyListScope.IchorInvariantColumnComponents(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    item { MainIcon() }
    item { TitleText(modifier = modifier) }
    item {
        DisplayAvailability(modifier = modifier, state = viewModel.latestAvailability)
    }
}

@Composable
private fun PermissionsInstructions(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.body2,
        stringResourceId = string.app_permission_was_denied
    )
}

@Composable
private fun DeleteAllButton(
    viewModel: MainViewModel,
    modifier: Modifier
) {
    val deleteAlertRequired = remember { mutableStateOf(false) }

    IchorButton(
        modifier = modifier.size(24.dp),
        onClick = { deleteAlertRequired.value = true },
        iconImage = Icons.Rounded.Delete,
        contentDescription = stringResource(string.ichor_delete_all_button)
    )

    Dialog(
        showDialog = deleteAlertRequired.value,
        onDismissRequest = {
            deleteAlertRequired.value = false
        },
        content = {
            IchorDeleteAllDialogContent(deleteAlertRequired, modifier, viewModel)
        }
    )
}

@Composable
private fun IchorDeleteAllDialogContent(
    deleteAlertRequired: MutableState<Boolean>,
    modifier: Modifier,
    viewModel: MainViewModel
) {
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
            text = stringResource(string.ichor_delete_all_final)
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            IchorButton(
                iconImage = Icons.Rounded.Done,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(string.ichor_delete_all_confirm)
            ) {
                viewModel.deleteAllHeartRates()
                deleteAlertRequired.value = false
            }
            IchorButton(
                iconImage = Icons.Rounded.Close,
                modifier = Modifier.size(32.dp),
                contentDescription = stringResource(string.ichor_delete_all_reject)
            ) {
                deleteAlertRequired.value = false
            }
        }
    }
}

@Composable
fun SamplingSpeedChangeButton(viewModel: MainViewModel, modifier: Modifier) {
    var samplingSpeedAlertRequired by remember { mutableStateOf(false) }
    val currentSamplingSpeed by viewModel.currentSamplingSpeed.collectAsState()

    IchorButton(
        modifier = modifier
            .size(24.dp)
            .padding(all = 0.dp),
        onClick = { samplingSpeedAlertRequired = true },
        iconImage = Icons.Rounded.Speed,
        contentDescription = stringResource(string.ichor_sampling_speed_change)
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
                    text = stringResource(string.ichor_change_sampling_speed_question)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Row(modifier = modifier.testTag(stringResource(string.ichor_slow_sampling_row_tag))) {
                        IchorButton(
                            iconImage = Icons.Rounded.DirectionsWalk,
                            modifier = Modifier.size(32.dp),
                            contentDescription = stringResource(string.ichor_slow_sampling_speed)
                        ) {
                            viewModel.changeSampleRate(SamplingSpeed.SLOW)
                            samplingSpeedAlertRequired = false
                        }
                        if (currentSamplingSpeed == SamplingSpeed.SLOW.toString()) {
                            TickIcon()
                        }
                    }
                    Row(modifier = modifier.testTag(stringResource(string.ichor_default_sampling_row_tag))) {
                        IchorButton(
                            iconImage = Icons.Rounded.DirectionsRun,
                            modifier = Modifier.size(32.dp),
                            contentDescription = stringResource(string.ichor_default_sampling_speed)
                        ) {
                            viewModel.changeSampleRate(SamplingSpeed.DEFAULT)
                            samplingSpeedAlertRequired = false
                        }
                        if (currentSamplingSpeed == SamplingSpeed.DEFAULT.toString()) {
                            TickIcon()
                        }
                    }
                    Row(modifier = modifier.testTag(stringResource(string.ichor_fast_sampling_row_tag))) {
                        IchorButton(
                            iconImage = Icons.Rounded.DirectionsBike,
                            modifier = Modifier.size(32.dp),
                            contentDescription = stringResource(string.ichor_fast_sampling_speed)
                        ) {
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
private fun AboutButton(modifier: Modifier, onClickAbout: () -> Unit) {
    IchorButton(
        modifier = modifier
            .size(24.dp)
            .padding(all = 0.dp),
        onClick = onClickAbout,
        iconImage = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(string.ichor_about_button)
    )
}

@Composable
private fun HeartRateItem(
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
                    text = "${stringResource(string.ichor_delete_record_part_1)}${currentHeartRateData.value}${stringResource(string.ichor_delete_record_part_2)}${currentHeartRateData.date}?",
                    textAlign = TextAlign.Center
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IchorButton(
                        iconImage = Icons.Rounded.Done,
                        modifier = Modifier.size(32.dp),
                        contentDescription = stringResource(string.ichor_delete_single_confirm)
                    ) {
                        viewModel.deleteHeartRate(currentHeartRateData.pk)
                    }
                    IchorButton(
                        iconImage = Icons.Rounded.Close,
                        modifier = Modifier.size(32.dp),
                        contentDescription = stringResource(string.ichor_delete_single_reject)
                    ) {
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
                        "${currentHeartRateData.value} ${stringResource(string.ichor_bpm)}",
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
fun MainIcon() {
    Icon(
        imageVector = Icons.Rounded.MonitorHeart,
        contentDescription = stringResource(string.ichor_main_heartbeat_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun DeleteHeartbeatIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Delete,
        contentDescription = stringResource(string.ichor_delete_heartbeat_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun ChangeSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Speed,
        contentDescription = stringResource(string.ichor_change_sampling_speed_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun TickIcon() {
    Icon(
        modifier = Modifier.size(32.dp),
        imageVector = Icons.Rounded.Done,
        contentDescription = stringResource(string.ichor_affirmation_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun TitleText(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.title1,
        stringResourceId = string.app_name
    )
}

@Composable
fun DisplayAvailability(modifier: Modifier, state: StateFlow<Availability>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        style = IchorTypography.body2,
        prefix = stringResource(string.ichor_availability_prefix)
    )
}

@Composable
fun DisplayLatestHeartRate(modifier: Modifier, state: StateFlow<Double>) {
    IchorStatefulText(
        state = state,
        modifier = modifier,
        suffix = stringResource(string.ichor_bpm_suffix)
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
fun IchorScreenPreview() {
    IchorScreen(viewModel = viewModel()) {}
}