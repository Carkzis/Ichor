package com.carkzis.ichor

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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
                navController = rememberSwipeDismissableNavController()
                IchorNavHost(viewModel = DummyViewModel(), navHostController = navController)
            }
        }
    }

    @Test
    fun `about button is displayed`() {
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
    }

    @Test
    fun `navigates to about screen using button and title of new screen displayed`() {
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .performClick()
        composeTestRule
            .onNodeWithText("About Ichor")
            .assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route, `is`(IchorScreens.ABOUT.toString()))
    }

    @Test
    fun `navigates to about screen using navController directly and title of new screen displayed`() {
        runBlocking {
            withContext(Dispatchers.Main) {
                navController.navigate(IchorScreens.ABOUT.toString())
            }
        }
        composeTestRule
            .onNodeWithText("About Ichor")
            .assertIsDisplayed()
    }

    @Test
    fun `navigates back to ichor screen from about screen by swiping`() {
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .performClick()
        composeTestRule
            .onNodeWithText("About Ichor")
            .assertIsDisplayed()
        composeTestRule.onRoot().performTouchInput {
            swipeRight()
        }

        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
    }

    @Test
    fun `navigates back to ichor screen using navController directly and about button displayed again`() {
        runBlocking {
            withContext(Dispatchers.Main) {
                navController.navigate(IchorScreens.ABOUT.toString())
                navController.navigate(IchorScreens.ICHOR.toString())
            }
        }
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
    }

}