package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorColorPalette

@Composable
internal fun AboutTitleIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(R.string.about_ichor_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutPermissionIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.LockPerson,
        contentDescription = stringResource(R.string.about_permissions_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutAboutIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(R.string.about_about_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.Speed,
        contentDescription = stringResource(R.string.about_sampling_speed_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutSlowSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsWalk,
        contentDescription = stringResource(R.string.about_slow_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutDefaultSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsRun,
        contentDescription = stringResource(R.string.about_default_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutFastSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsBike,
        contentDescription = stringResource(R.string.about_fast_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
internal fun AboutDeleteAllIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.Delete,
        contentDescription = stringResource(R.string.about_delete_all_icon),
        tint = IchorColorPalette.secondary
    )
}
