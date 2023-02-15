package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ScalingLazyListScope
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorText

@Composable
internal fun AboutDescriptionText(modifier: Modifier) {
    IchorText(
        modifier = modifier,
        style = IchorTypography.body2,
        stringResourceId = R.string.about_description
    )
}

@Composable
internal fun AboutIchorText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_ichor,
        modifier = modifier,
        style = IchorTypography.title1
    )
}

@Composable
internal fun StartingUpText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_starting_up,
        modifier = modifier,
        style = IchorTypography.title3
    )
}

@Composable
internal fun AboutAboutText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_about,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun PermissionsText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_permissions,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun DeleteSingleRecordText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_one,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun DeleteSingleRecordSubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_one_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun WhatYouCanDoText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_what_you_can_do,
        modifier = modifier,
        style = IchorTypography.title3
    )
}

@Composable
internal fun FurtherInformationDetailsText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_further_information_details,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun FurtherInformationText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_further_information,
        modifier = modifier,
        style = IchorTypography.title3
    )
}

@Composable
internal fun SamplingSpeedText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_sampling_speed,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun DeleteAllText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_all,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun SlowSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_slow_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun FastSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_fast_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun DefaultSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_default_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun HeartrateHistoryText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_history,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun HeartrateHistorySubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_history_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun BPMText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_bpm,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun BPMSubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_bpm_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun AboutSamplingSpeedText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_sampling_speed_display,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun SamplingSpeedSubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_sampling_speed_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun AvailabilityText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_availability,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AvailabilitySubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_availability_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun WhatYouCanSeeText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_what_you_can_see,
        modifier = modifier,
        style = IchorTypography.title3
    )
}

