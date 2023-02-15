@file:OptIn(ExperimentalMaterialApi::class)

package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Dialog
import com.carkzis.ichor.R
import com.carkzis.ichor.data.domain.DomainHeartRate
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.ui.IchorCard
import com.carkzis.ichor.ui.MainViewModel

@Composable
internal fun HeartRateItem(
    viewModel: MainViewModel,
    currentHeartRateData: DomainHeartRate,
    modifier: Modifier
) {
    val deleteAlertRequired = remember { mutableStateOf(false) }
    val dismissState = swipingDismissStateForHeartRateItem(deleteAlertRequired)
    if (!deleteAlertRequired.value) {
        LaunchedEffect(Unit) {
            dismissState.reset()
        }
    }

    DismissibleHeartRateItemCard(dismissState, modifier, currentHeartRateData)

    Dialog(
        showDialog = deleteAlertRequired.value,
        onDismissRequest = {
            deleteAlertRequired.value = false
        },
        content = {
            DeleteOneDialogContent(deleteAlertRequired, currentHeartRateData, viewModel)
        }
    )
}

@Composable
private fun DismissibleHeartRateItemCard(
    dismissState: DismissState,
    modifier: Modifier,
    currentHeartRateData: DomainHeartRate
) {
    SwipeToDismiss(
        state = dismissState,
        background = { Box(modifier = modifier.fillMaxSize()) },
        dismissContent = {
            IchorCard(
                time = currentHeartRateData.date,
                content = {
                    Text(
                        "${currentHeartRateData.value} ${stringResource(R.string.ichor_bpm)}",
                        color = IchorColorPalette.onSecondary
                    )
                }
            )
        }
    )
}

@Composable
private fun swipingDismissStateForHeartRateItem(deleteAlertRequired: MutableState<Boolean>) =
    rememberDismissState {
        if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
            deleteAlertRequired.value = true
        }
        true
    }