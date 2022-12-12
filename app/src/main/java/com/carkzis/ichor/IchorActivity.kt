package com.carkzis.ichor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.theme.IchorTheme
import com.carkzis.ichor.ui.AboutBody
import com.carkzis.ichor.ui.IchorBody
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IchorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val viewModel by viewModels<MainViewModel>()
        val viewModel by viewModels<MainViewModel>()
        setContent {
            IchorTheme {
                IchorNavHost(viewModel = viewModel)
                //IchorBody(viewModel = viewModel)
            }
        }
    }

}

@Composable
fun IchorNavHost(
    navHostController: NavHostController = rememberSwipeDismissableNavController(),
    startDestination: String = IchorScreens.ICHOR.toString(),
    viewModel: MainViewModel
    ) {
    SwipeDismissableNavHost(navHostController, startDestination = startDestination) {
        composable(startDestination) {
            IchorBody(viewModel = viewModel, onClickAbout = {
                navHostController.navigate(IchorScreens.ABOUT.toString())
            })
        }
        composable(IchorScreens.ABOUT.toString()) {
            AboutBody()
        }
    }
}

enum class IchorScreens(val descriptor: String) {
    ICHOR("ichor_screen"),
    ABOUT("about_screen");

    override fun toString() : String {
        return descriptor
    }
}