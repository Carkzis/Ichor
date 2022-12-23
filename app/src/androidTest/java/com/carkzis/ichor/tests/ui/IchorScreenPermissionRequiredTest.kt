package com.carkzis.ichor.tests.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.R
import com.carkzis.ichor.testdoubles.DummyPermissionFacade
import com.carkzis.ichor.testdoubles.DummyViewModel
import com.carkzis.ichor.ui.IchorScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IchorScreenPermissionRequiredTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    lateinit var heartRatePermissionFacade: DummyPermissionFacade
    lateinit var navController: NavHostController
    lateinit var delayedSetUp: () -> Unit
    lateinit var permissionNotGrantedText: String

    @Before
    fun setUp() {
        delayedSetUp = {
            hiltRule.inject()
            composeTestRule.apply {
                setContent {
                    navController = rememberSwipeDismissableNavController()
                    permissionNotGrantedText = stringResource(id = R.string.app_permission_was_denied)
                    IchorScreen(viewModel = DummyViewModel(), heartRatePermissionFacade = heartRatePermissionFacade)
                }
            }
        }
    }

    @Test
    fun `when no permission previously requested can get permission dialogue which after denial message provided on screen`() {
        heartRatePermissionFacade = DummyPermissionFacade(willGivePermission = false, permissionPreviouslyDenied = false)
        delayedSetUp()

        // Assert on the initial screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button")
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithText(permissionNotGrantedText)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .assertCountEquals(0)

        // Move to next screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button")
            .performClick()

        // Assert the new screen after "denying" permissions.
        composeTestRule
            .onNodeWithText(permissionNotGrantedText)
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithContentDescription("Permission request button")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .assertCountEquals(0)
    }

    @Test
    fun `when no permission previously requested can get permission dialogue which after acceptance heart rate data screen visible`() {
        heartRatePermissionFacade = DummyPermissionFacade(willGivePermission = true, permissionPreviouslyDenied = false)
        delayedSetUp()

        // Assert on the initial screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button")
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithText(permissionNotGrantedText)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .assertCountEquals(0)

        // Move to next screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button")
            .performClick()

        // Assert the new screen after "accepting" permissions.
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .onFirst()
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithContentDescription("Permission request button")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(permissionNotGrantedText)
            .assertCountEquals(0)
    }

    @Test
    fun `when permission previously granted automatically go to heart rate data screen visible`() {

    }

    @Test
    fun `when permission previously denied message provided on screen and permission cannot be requested again within app`() {

    }
    
}