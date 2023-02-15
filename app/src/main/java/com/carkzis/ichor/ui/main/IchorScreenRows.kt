package com.carkzis.ichor.ui.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ScalingLazyListScope
import com.carkzis.ichor.R
import com.carkzis.ichor.theme.IchorTypography
import com.carkzis.ichor.ui.IchorStatefulText
import com.carkzis.ichor.ui.MainViewModel

@Composable
internal fun ButtonsRow(
    viewModel: MainViewModel,
    modifier: Modifier,
    onClickAbout: () -> Unit
) {
    Row {
        SamplingSpeedChangeButton(viewModel = viewModel, modifier = modifier)
        Spacer(modifier = Modifier.width(8.dp))
        AboutButton(modifier = modifier, onClickAbout)
        Spacer(modifier = Modifier.width(8.dp))
        DeleteAllButton(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
internal fun SamplingSpeedRow(
    modifier: Modifier,
    viewModel: MainViewModel
) {
    Row {
        IchorStatefulText(
            modifier = modifier,
            style = IchorTypography.body2,
            state = viewModel.currentSamplingSpeed,
            prefix = stringResource(R.string.ichor_sample_speed_prefix)
        )
    }
}