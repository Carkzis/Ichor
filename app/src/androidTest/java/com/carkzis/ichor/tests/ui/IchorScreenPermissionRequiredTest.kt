package com.carkzis.ichor.tests.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.IchorNavHost
import com.carkzis.ichor.IchorScreens
import com.carkzis.ichor.testdoubles.DummyPermissionFacade
import com.carkzis.ichor.testdoubles.DummyViewModel
import com.carkzis.ichor.ui.IchorScreen
import com.carkzis.ichor.utils.DefaultPermissionFacade
import com.carkzis.ichor.utils.PermissionFacade
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

class IchorScreenPermissionRequiredTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    val heartRatePermissionFacade: PermissionFacade = DummyPermissionFacade
    lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                navController = rememberSwipeDismissableNavController()
                IchorScreen(viewModel = DummyViewModel(), heartRatePermissionFacade = heartRatePermissionFacade)
//                IchorNavHost(viewModel = DummyViewModel(), navHostController = navController, startDestination = IchorScreens.ICHOR.toString())
            }
        }
    }
    
}