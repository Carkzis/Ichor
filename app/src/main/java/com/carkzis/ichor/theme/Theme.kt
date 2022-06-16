package com.carkzis.ichor.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun IchorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = IchorColorPalette,
        typography = IchorTypography,
        content = content
    )
}