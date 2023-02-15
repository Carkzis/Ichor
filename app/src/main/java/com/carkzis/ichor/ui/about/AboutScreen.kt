package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.*
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
import com.carkzis.ichor.R.string
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorText

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
            InitialInfo(modifier)
            StartingUp(modifier)
            WhatYouCanSee(modifier)
            WhatYouCanDo(modifier)
            FurtherInformation(modifier)
        }
    }
}

internal fun ScalingLazyListScope.StartingUp(modifier: Modifier) {
    item { AboutScreenStartingUpText(modifier) }
    AboutPermissionsRow(modifier)
    AboutAboutRow(modifier)
}

private fun ScalingLazyListScope.WhatYouCanDo(modifier: Modifier) {
    item { AboutScreenWhatYouCanDoText(modifier) }
    item { AboutSamplingSpeedsRow(modifier) }
    item { DeleteAllRow(modifier) }
    item { AboutScreenDeleteSingleRecordSubtitleText(modifier) }
    item { AboutScreenDeleteSingleRecordText(modifier) }
}

private fun ScalingLazyListScope.WhatYouCanSee(modifier: Modifier) {
    item {
        IchorText(
            stringResourceId = string.about_what_you_can_see,
            modifier = modifier,
            style = IchorTypography.title3
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_availability_subtitle,
            modifier = modifier,
            style = IchorTypography.body2.plus(
                TextStyle(fontWeight = FontWeight.Bold)
            )
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_availability,
            modifier = modifier.padding(start = 8.dp),
            style = IchorTypography.body2
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_sampling_speed_subtitle,
            modifier = modifier,
            style = IchorTypography.body2.plus(
                TextStyle(fontWeight = FontWeight.Bold)
            )
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_sampling_speed_display,
            modifier = modifier.padding(start = 8.dp),
            style = IchorTypography.body2
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_bpm_subtitle,
            modifier = modifier,
            style = IchorTypography.body2.plus(
                TextStyle(fontWeight = FontWeight.Bold)
            )
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_bpm,
            modifier = modifier.padding(start = 8.dp),
            style = IchorTypography.body2
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_history_subtitle,
            modifier = modifier,
            style = IchorTypography.body2.plus(
                TextStyle(fontWeight = FontWeight.Bold)
            )
        )
    }
    item {
        IchorText(
            stringResourceId = string.about_history,
            modifier = modifier.padding(start = 8.dp),
            style = IchorTypography.body2
        )
    }
}

internal fun ScalingLazyListScope.InitialInfo(modifier: Modifier) {
    item { AboutTitleIcon() }
    item { AboutIchorText(modifier) }
    item { AboutScreenDescriptionText(modifier) }
}

internal fun ScalingLazyListScope.FurtherInformation(modifier: Modifier) {
    item { FurtherInformationText(modifier) }
    item { FurtherInformationDetailsText(modifier) }
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