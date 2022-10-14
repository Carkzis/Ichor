package com.carkzis.ichor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LockPerson
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AppCard
import androidx.wear.compose.material.Button
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.theme.IchorTypography
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.runner.Description

@Composable
fun IchorText(
    modifier: Modifier = Modifier,
    style: TextStyle = IchorTypography.body1,
    stringResourceId: Int = R.string.app_name
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        style = style,
        text = stringResource(id = stringResourceId)
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
fun IchorTextPreview() {
    IchorTheme {
        IchorText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        )
    }
}


@Composable
@Suppress("IMPLICIT_CAST_TO_ANY")
fun <T> IchorStatefulText(modifier: Modifier = Modifier, state: StateFlow<T>) {
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

@Preview(
    group = "StatefulText",
    widthDp = WEAR_PREVIEW_ROW_WIDTH_DP,
    heightDp = WEAR_PREVIEW_ROW_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun IchorStatefulTextPreview() {
    IchorTheme {
        val stateFlow = MutableStateFlow(123.456)
        IchorStatefulText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            state = stateFlow
        )
    }
}

@Composable
fun IchorButton(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    iconImage: ImageVector = Icons.Rounded.LockPerson,
    onClick: () -> Unit = {}
) {
    Button(modifier = modifier.padding(4.dp), onClick = { onClick() }) {
        Icon(
            imageVector = iconImage,
            contentDescription = "Requests permission to access heartrate.",
            modifier = iconModifier
        )
    }
}

@Preview(
    group = "Button",
    widthDp = WEAR_PREVIEW_ROW_WIDTH_DP,
    heightDp = WEAR_PREVIEW_ROW_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun IchorButtonPreview(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    IchorTheme {
        IchorButton(
            modifier = modifier
                .wrapContentSize()
                .padding(all = 8.dp),
            iconModifier = iconModifier,
            onClick = onClick
        )
    }
}

@Composable
fun IchorCard(
    modifier: Modifier = Modifier,
    description: String = "Heartrate",
    time: String,
    mainInfo: String,
    additionalInfo: () -> String = { "" }
) {
    AppCard(
        modifier = Modifier,
        onClick = { /*TODO*/ },
        appImage = {
            Icon(
                imageVector = Icons.Rounded.MonitorHeart,
                contentDescription = "Icon for the current heart rate.",
                modifier = modifier
            )
        },
        appName = { Text(description) },
        time = { Text(time) },
        title = { Text(mainInfo) }) {
        Text(additionalInfo())
    }
}

@Preview(
    group = "Button",
    widthDp = WEAR_PREVIEW_ROW_WIDTH_DP,
    heightDp = WEAR_PREVIEW_ROW_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun IchorCardPreview() {
    IchorTheme {
        IchorCard(
            description = "Heartrate",
            time = "End of Time",
            mainInfo = "140 bpm",
            additionalInfo = { "There is some additional content here." }
        )
    }
}
