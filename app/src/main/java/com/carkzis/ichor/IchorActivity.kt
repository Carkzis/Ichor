package com.carkzis.ichor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.ui.IchorBody
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IchorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel>()

        setContent {
            IchorTheme {
                IchorBody(viewModel = viewModel)
            }
        }
    }

}


