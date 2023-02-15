package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorColorPalette

@Composable
fun MainIcon() {
    Icon(
        imageVector = Icons.Rounded.MonitorHeart,
        contentDescription = stringResource(R.string.ichor_main_heartbeat_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun DeleteHeartbeatIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Delete,
        contentDescription = stringResource(R.string.ichor_delete_heartbeat_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun ChangeSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.Speed,
        contentDescription = stringResource(R.string.ichor_change_sampling_speed_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun TickIcon() {
    Icon(
        modifier = Modifier.size(32.dp),
        imageVector = Icons.Rounded.Done,
        contentDescription = stringResource(R.string.ichor_affirmation_icon),
        tint = IchorColorPalette.secondary
    )
}