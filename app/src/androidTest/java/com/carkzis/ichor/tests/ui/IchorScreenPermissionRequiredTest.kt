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
    private lateinit var dummyHeartRatePermissionFacade: DummyPermissionFacade
    private lateinit var navController: NavHostController
    private lateinit var _delayedSetUp: () -> Unit
    var delayedSetUp: () -> Unit
        get() = _delayedSetUp
        set(value) {
            _delayedSetUp = value
        }
    private lateinit var permissionNotGrantedText: String

    @Before
    fun setUp() {
        delayedSetUp = {
            hiltRule.inject()
            composeTestRule.apply {
                setContent {
                    navController = rememberSwipeDismissableNavController()
                    permissionNotGrantedText = stringResource(id = R.string.app_permission_was_denied)
                    IchorScreen(viewModel = DummyViewModel(), heartRatePermissionFacade = dummyHeartRatePermissionFacade)
                }
            }
        }
    }

    @Test
    fun `when no permission previously requested can get permission dialogue which after denial message provided on screen`() {
        dummyHeartRatePermissionFacade = DummyPermissionFacade(willGivePermission = false, permissionPreviouslyDenied = false)
        delayedSetUp()

        // Assert on the initial screen.
        assertPermissionRequiredAndCanBeRequestedScreenVisible()

        // Move to next screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button.")
            .performClick()

        // Assert the new screen after "denying" permissions.
        assertPermissionRequiredButCannotBeRequestedScreenVisible()
    }

    @Test
    fun `when no permission previously requested can get permission dialogue which after acceptance heart rate data screen visible`() {
        dummyHeartRatePermissionFacade = DummyPermissionFacade(willGivePermission = true, permissionPreviouslyDenied = false)
        delayedSetUp()

        // Assert on the initial screen.
        assertPermissionRequiredAndCanBeRequestedScreenVisible()

        // Move to next screen.
        composeTestRule
            .onNodeWithContentDescription("Permission request button.")
            .performClick()

        // Assert the new screen after "accepting" permissions.
        assertMainIchorScreenVisible()
    }

    @Test
    fun `when permission previously granted automatically go to heart rate data screen visible`() {
        dummyHeartRatePermissionFacade = DummyPermissionFacade(hasPermissionAlready = true)
        delayedSetUp()

        // Assert that we automatically go to the main screen.
        assertMainIchorScreenVisible()
    }

    @Test
    fun `when permission previously denied message provided on screen and permission cannot be requested again within app`() {
        dummyHeartRatePermissionFacade = DummyPermissionFacade(willGivePermission = true, permissionPreviouslyDenied = true)
        delayedSetUp()

        // Assert that we automatically get a message advised permission previously denied.
        assertPermissionRequiredButCannotBeRequestedScreenVisible()
    }

    private fun assertPermissionRequiredAndCanBeRequestedScreenVisible() {
        assertStandardThemeItemsDisplayed()
        
        composeTestRule
            .onNodeWithContentDescription("Permission request button.")
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithText(permissionNotGrantedText)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .assertCountEquals(0)
    }

    private fun assertPermissionRequiredButCannotBeRequestedScreenVisible() {
        assertStandardThemeItemsDisplayed()

        composeTestRule
            .onNodeWithText(permissionNotGrantedText)
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithContentDescription("Permission request button.")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .assertCountEquals(0)
    }

    private fun assertMainIchorScreenVisible() {
        assertStandardThemeItemsDisplayed()

        composeTestRule
            .onAllNodesWithText("bpm", substring = true)
            .onFirst()
            .assertIsDisplayed()
        composeTestRule
            .onAllNodesWithContentDescription("Permission request button.")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(permissionNotGrantedText)
            .assertCountEquals(0)
    }

    private fun assertStandardThemeItemsDisplayed() {
        composeTestRule
            .onNodeWithText("Ichor")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Main heartbeat icon for app.")
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
    }

    
}