package com.carkzis.ichor.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*
import com.carkzis.ichor.IchorText
import com.carkzis.ichor.R

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
            autoCentering = AutoCenteringParams(itemIndex = 0),
            state = listState
        ) {
            item {
                IchorText(stringResourceId = R.string.about_ichor)
            }
        }
    }
}