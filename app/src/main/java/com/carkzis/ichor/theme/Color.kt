package com.carkzis.ichor.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val primaryPeach = Color(0xffef5350)
val primaryPeachVariant = Color(0xffff867c)
val primaryPurple = Color(0xff9c27b0)
val primaryPurpleVariant = Color(0xffd05ce3)
val errorRed = Color(0xffb61827)

val IchorColorPalette: Colors = Colors(
    primary = primaryPeach,
    primaryVariant = primaryPeachVariant,
    secondary = primaryPurple,
    secondaryVariant = primaryPurpleVariant,
    error = errorRed,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)