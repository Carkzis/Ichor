package com.carkzis.ichor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.carkzis.ichor.*
import com.carkzis.ichor.R.*
import com.carkzis.ichor.theme.IchorColorPalette
import com.carkzis.ichor.theme.IchorTypography

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val listState = rememberScalingLazyListState()
    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState,
        ) {
            // Initial info.
            item { AboutTitleIcon() }
            item {
                IchorText(stringResourceId = string.about_ichor, modifier = modifier, style = IchorTypography.title1)
            }
            item {
                IchorText(
                    modifier = modifier,
                    style = IchorTypography.body2,
                    stringResourceId = string.about_description
                )
            }

            // Starting up.
            item {
                IchorText(stringResourceId = string.about_starting_up, modifier = modifier, style = IchorTypography.title3)
            }
            item {
                Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_permissions_row_tag))) {
                    AboutPermissionIcon()
                    IchorText(stringResourceId = string.about_permissions, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                }
            }
            item {
                Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_about_row_tag))) {
                    AboutAboutIcon()
                    IchorText(stringResourceId = string.about_about, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                }
            }

            // What you can see.
            item {
                IchorText(stringResourceId = string.about_what_you_can_see, modifier = modifier, style = IchorTypography.title3)
            }
            item {
                IchorText(stringResourceId = string.about_availability_subtitle, modifier = modifier, style = IchorTypography.body2.plus(
                    TextStyle(fontWeight = FontWeight.Bold)
                ))
            }
            item {
                IchorText(stringResourceId = string.about_availability, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }
            item {
                IchorText(stringResourceId = string.about_sampling_speed_subtitle, modifier = modifier, style = IchorTypography.body2.plus(
                    TextStyle(fontWeight = FontWeight.Bold)
                ))
            }
            item {
                IchorText(stringResourceId = string.about_sampling_speed_display, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }
            item {
                IchorText(stringResourceId = string.about_bpm_subtitle, modifier = modifier, style = IchorTypography.body2.plus(
                    TextStyle(fontWeight = FontWeight.Bold)
                ))
            }
            item {
                IchorText(stringResourceId = string.about_bpm, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }
            item {
                IchorText(stringResourceId = string.about_history_subtitle, modifier = modifier, style = IchorTypography.body2.plus(
                    TextStyle(fontWeight = FontWeight.Bold)
                ))
            }
            item {
                IchorText(stringResourceId = string.about_history, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }

            // What you can do.
            item {
                IchorText(stringResourceId = string.about_what_you_can_do, modifier = modifier, style = IchorTypography.title3)
            }
            item {
                Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_sampling_speeds_row_tag))) {
                    AboutSamplingSpeedIcon()
                    Column(modifier = modifier.fillMaxWidth()) {
                        IchorText(stringResourceId = string.about_sampling_speed, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_slow_sampling_row_tag))) {
                            AboutSlowSamplingSpeedIcon()
                            IchorText(stringResourceId = string.about_slow_sampling, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_default_sampling_row_tag))) {
                            AboutDefaultSamplingSpeedIcon()
                            IchorText(stringResourceId = string.about_default_sampling, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_fast_sampling_row_tag))) {
                            AboutFastSamplingSpeedIcon()
                            IchorText(stringResourceId = string.about_fast_sampling, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                        }
                    }

                }
            }
            item {
                Row(modifier = modifier.fillMaxWidth().testTag(stringResource(string.about_delete_all_row_tag))) {
                    AboutDeleteAllIcon()
                    IchorText(stringResourceId = string.about_delete_all, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                }
            }
            item {
                IchorText(stringResourceId = string.about_delete_one_subtitle, modifier = modifier, style = IchorTypography.body2.plus(
                    TextStyle(fontWeight = FontWeight.Bold)
                ))
            }
            item {
                IchorText(stringResourceId = string.about_delete_one, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }

            // Further information.
            item {
                IchorText(stringResourceId = string.about_further_information, modifier = modifier, style = IchorTypography.title3)
            }
            item {
                IchorText(stringResourceId = string.about_further_information_details, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
            }
        }
    }
}

@Composable
fun AboutTitleIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(string.about_ichor_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutPermissionIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.LockPerson,
        contentDescription = stringResource(string.about_permissions_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutAboutIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = stringResource(string.about_about_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.Speed,
        contentDescription = stringResource(string.about_sampling_speed_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutSlowSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsWalk,
        contentDescription = stringResource(string.about_slow_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutDefaultSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsRun,
        contentDescription = stringResource(string.about_default_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutFastSamplingSpeedIcon() {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = Icons.Rounded.DirectionsBike,
        contentDescription = stringResource(string.about_fast_sampling_icon),
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutDeleteAllIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.Delete,
        contentDescription = stringResource(string.about_delete_all_icon),
        tint = IchorColorPalette.secondary
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
fun AboutScreenPreview() {
    AboutScreen()
}