package com.carkzis.ichor

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LockPerson
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import com.carkzis.ichor.theme.IchorTheme
import kotlinx.coroutines.flow.StateFlow

@Composable
fun IchorText(modifier: Modifier = Modifier, stringResourceId: Int = R.string.app_name) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(id = stringResourceId)
    )
}

@Composable
@Suppress("IMPLICIT_CAST_TO_ANY")
fun <T> IchorStatefulText(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel(), state: StateFlow<T>) {
    val stateValue by state.collectAsState()
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,

        text = when (stateValue) {
            is Double -> String.format("%.1f", stateValue)
            else -> stateValue
        } as String
    )
}

@Composable
fun IchorButton(modifier: Modifier = Modifier, iconModifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(modifier = modifier.padding(4.dp), onClick = { onClick() }) {
        Icon(
           imageVector = Icons.Rounded.LockPerson,
            contentDescription = "Requests permission to access heartrate.",
            modifier = iconModifier
        )
    }
}

@Preview(
    group = "Text",
    widthDp = WEAR_PREVIEW_ROW_WIDTH_DP,
    heightDp = WEAR_PREVIEW_ROW_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun TextComposablePreview() {
    IchorTheme {
        IchorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        )
    }
}