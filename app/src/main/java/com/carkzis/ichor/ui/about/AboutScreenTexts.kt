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
internal fun AboutScreenDescriptionText(modifier: Modifier) {
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
internal fun AboutScreenStartingUpText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_starting_up,
        modifier = modifier,
        style = IchorTypography.title3
    )
}

@Composable
internal fun AboutScreenAboutText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_about,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenPermissionsText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_permissions,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenDeleteSingleRecordText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_one,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenDeleteSingleRecordSubtitleText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_one_subtitle,
        modifier = modifier,
        style = IchorTypography.body2.plus(
            TextStyle(fontWeight = FontWeight.Bold)
        )
    )
}

@Composable
internal fun AboutScreenWhatYouCanDoText(modifier: Modifier) {
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
internal fun AboutScreenSamplingSpeedText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_sampling_speed,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenDeleteAllText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_delete_all,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenSlowSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_slow_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenFastSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_fast_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}

@Composable
internal fun AboutScreenDefaultSamplingDescriptionText(modifier: Modifier) {
    IchorText(
        stringResourceId = R.string.about_default_sampling,
        modifier = modifier.padding(start = 8.dp),
        style = IchorTypography.body2
    )
}
