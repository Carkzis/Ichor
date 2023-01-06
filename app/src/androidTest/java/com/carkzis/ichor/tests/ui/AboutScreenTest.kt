package com.carkzis.ichor.tests.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.R
import com.carkzis.ichor.ui.AboutScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AboutScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    private lateinit var navController: NavHostController

    // TODO: Extract these into a POJO, as string resources must be set in setContent block.
    private lateinit var title: String
    private lateinit var aboutDescription: String
    private lateinit var startingUp: String
    private lateinit var permissions: String

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                title = stringResource(id = R.string.about_ichor)
                aboutDescription = stringResource(id = R.string.about_description)
                startingUp = stringResource(id = R.string.about_starting_up)
                permissions = stringResource(id = R.string.about_permissions)
                navController = rememberSwipeDismissableNavController()
                AboutScreen()
            }
        }
    }

    @Test
    fun `about screen items are displayed as expected`() {
        headerDisplayed()

        composeTestRule
            .onNodeWithText(aboutDescription)
            .assertIsDisplayed()
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(startingUp))
            .performScrollToNode(hasText(aboutDescription))
            .performScrollToNode(hasText(permissions))
    }

    private fun headerDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Learn more about Ichor.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }
}