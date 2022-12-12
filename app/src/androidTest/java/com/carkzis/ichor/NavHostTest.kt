package com.carkzis.ichor

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavHostTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                IchorNavHost(viewModel = DummyViewModel())
                navController = rememberSwipeDismissableNavController()
            }
        }
    }

    @Test
    fun `about button is displayed`() {
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
    }
}