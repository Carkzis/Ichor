package com.carkzis.ichor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.carkzis.ichor.theme.IchorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IchorUI()
        }
    }
}

@Composable
fun IchorUI() {
    IchorTheme {
        Text("Hello")
    }
}

@Preview
@Composable
fun IchorUIPreview() {
    IchorUI()
}