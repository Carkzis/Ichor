package com.carkzis.ichor.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LockPerson
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AboutBody(modifier: Modifier = Modifier) {
    // TODO: May need to change this.
    val listState = rememberScalingLazyListState()
    Scaffold(
        timeText = { if (!listState.isScrollInProgress) TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
    ) {
        ScalingLazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth(),
            autoCentering = AutoCenteringParams(itemIndex = 1),
            state = listState
        ) {
            item { TitleAboutIcon() }
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
            item {
                IchorText(stringResourceId = string.about_starting_up, modifier = modifier, style = IchorTypography.title3)
            }
            item {
                Row(modifier = modifier.fillMaxWidth()) {
                    PermissionIcon()
                    IchorText(stringResourceId = string.about_permissions, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                }
            }
            item {
                Row(modifier = modifier.fillMaxWidth()) {
                    AboutIcon()
                    IchorText(stringResourceId = string.about_about, modifier = modifier.padding(start = 8.dp), style = IchorTypography.body2)
                }
            }
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

        }
    }
}

@Composable
fun TitleAboutIcon() {
    Icon(
        modifier = Modifier.size(48.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = "Learn more about Ichor.",
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun PermissionIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.LockPerson,
        contentDescription = "Icon for health services permission request.",
        tint = IchorColorPalette.secondary
    )
}

@Composable
fun AboutIcon() {
    Icon(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Rounded.QuestionMark,
        contentDescription = "Learn more about Ichor.",
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
    AboutBody()
}