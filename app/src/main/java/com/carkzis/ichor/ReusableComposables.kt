package com.carkzis.ichor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carkzis.ichor.theme.IchorTheme

@Composable
fun IchorText(modifier: Modifier = Modifier, stringResourceId: Int = R.string.hello_world) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(id = stringResourceId)
    )
}

@Composable
fun IchorStatefulText(modifier: Modifier = Modifier, text: String = "0.0") {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = text
    )
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