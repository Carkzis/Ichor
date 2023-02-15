package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ScalingLazyListScope

internal fun ScalingLazyListScope.InitialInfo(modifier: Modifier) {
    item { AboutTitleIcon() }
    item { AboutIchorText(modifier) }
    item { AboutDescriptionText(modifier) }
}

internal fun ScalingLazyListScope.StartingUp(modifier: Modifier) {
    item { StartingUpText(modifier) }
    PermissionsRow(modifier)
    AboutAboutRow(modifier)
}

internal fun ScalingLazyListScope.WhatYouCanSee(modifier: Modifier) {
    item { WhatYouCanSeeText(modifier) }
    item { AvailabilitySubtitleText(modifier) }
    item { AvailabilityText(modifier) }
    item { SamplingSpeedSubtitleText(modifier) }
    item { AboutSamplingSpeedText(modifier) }
    item { BPMSubtitleText(modifier) }
    item { BPMText(modifier) }
    item { HeartrateHistorySubtitleText(modifier) }
    item { HeartrateHistoryText(modifier) }
}

internal fun ScalingLazyListScope.WhatYouCanDo(modifier: Modifier) {
    item { WhatYouCanDoText(modifier) }
    item { AboutSamplingSpeedsRow(modifier) }
    item { DeleteAllRow(modifier) }
    item { DeleteSingleRecordSubtitleText(modifier) }
    item { DeleteSingleRecordText(modifier) }
}

internal fun ScalingLazyListScope.FurtherInformation(modifier: Modifier) {
    item { FurtherInformationText(modifier) }
    item { FurtherInformationDetailsText(modifier) }
}

@Composable
internal fun AboutScreenSamplingSpeedsColumn(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        SamplingSpeedText(modifier)
        Spacer(modifier = Modifier.height(8.dp))
        SlowSamplingRow(modifier)
        Spacer(modifier = Modifier.height(4.dp))
        DefaultSamplingRow(modifier)
        Spacer(modifier = Modifier.height(4.dp))
        FastSamplingRow(modifier)
    }
}