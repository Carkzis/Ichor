package com.carkzis.ichor.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AboutScreenSamplingSpeedsColumn(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        AboutScreenSamplingSpeedText(modifier)
        Spacer(modifier = Modifier.height(8.dp))
        AboutScreenSlowSamplingRow(modifier)
        Spacer(modifier = Modifier.height(4.dp))
        AboutScreenDefaultSamplingRow(modifier)
        Spacer(modifier = Modifier.height(4.dp))
        AboutScreenFastSamplingRow(modifier)
    }
}