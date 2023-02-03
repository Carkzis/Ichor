package com.carkzis.ichor.tests.ui

import androidx.compose.runtime.Composable
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

    @Before
    fun setUp() {
        delayedSetUp = {
            hiltRule.inject()
            composeTestRule.apply {
                setContent {
                    navController = rememberSwipeDismissableNavController()
                    obtainStringResourcesForIchorScreenWithoutPermissions()
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
            .onNodeWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.permissionRequest)
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
            .onNodeWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.permissionRequest)
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

    @Composable
    private fun obtainStringResourcesForIchorScreenWithoutPermissions() {
        obtainTextStringResourcesForIchorScreenWithoutPermissions()
        obtainContentDescriptionStringResourcesForIchorScreenWithoutPermissions()
    }

    @Composable
    private fun obtainTextStringResourcesForIchorScreenWithoutPermissions() {
        IchorScreenPermissionRequiredText.Header.title = stringResource(id = R.string.app_name)

        IchorScreenPermissionRequiredText.Body.apply {
            permissionNotGranted =
                stringResource(id = R.string.app_permission_was_denied)
            bpmSubstring = stringResource(id = R.string.ichor_bpm)
        }
    }

    @Composable
    private fun obtainContentDescriptionStringResourcesForIchorScreenWithoutPermissions() {
        IchorScreenPermissionRequiredContentDescriptions.Icons.main =
            stringResource(id = R.string.ichor_main_heartbeat_icon)

        IchorScreenPermissionRequiredContentDescriptions.Buttons.apply {
            permissionRequest = stringResource(id = R.string.ichor_permission_button)
            about = stringResource(id = R.string.ichor_about_button)
        }
    }

    private fun assertPermissionRequiredAndCanBeRequestedScreenVisible() {
        // Is displayed.
        assertStandardThemeItemsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.permissionRequest)
            .assertIsDisplayed()

        // Is not displayed.
        composeTestRule
            .onAllNodesWithText(IchorScreenPermissionRequiredText.Body.permissionNotGranted)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(IchorScreenPermissionRequiredText.Body.bpmSubstring, substring = true)
            .assertCountEquals(0)
    }

    private fun assertPermissionRequiredButCannotBeRequestedScreenVisible() {
        // Is displayed.
        assertStandardThemeItemsDisplayed()
        composeTestRule
            .onNodeWithText(IchorScreenPermissionRequiredText.Body.permissionNotGranted)
            .assertIsDisplayed()

        // Is not displayed.
        composeTestRule
            .onAllNodesWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.permissionRequest)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(IchorScreenPermissionRequiredText.Body.bpmSubstring, substring = true)
            .assertCountEquals(0)
    }

    private fun assertMainIchorScreenVisible() {
        // Is displayed.
        assertStandardThemeItemsDisplayed()
        composeTestRule
            .onAllNodesWithText(IchorScreenPermissionRequiredText.Body.bpmSubstring, substring = true)
            .onFirst()
            .assertIsDisplayed()

        // Is not displayed.
        composeTestRule
            .onAllNodesWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.permissionRequest)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(IchorScreenPermissionRequiredText.Body.permissionNotGranted)
            .assertCountEquals(0)
    }

    private fun assertStandardThemeItemsDisplayed() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionRequiredText.Header.title)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Icons.main)
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionRequiredContentDescriptions.Buttons.about)
            .assertIsDisplayed()
    }

}


object IchorScreenPermissionRequiredText {
    object Header {
        var title: String = ""
    }

    object Body {
        var bpmSubstring: String = ""
        var permissionNotGranted: String = ""
    }
}

object IchorScreenPermissionRequiredContentDescriptions {
    object Icons {
        var main: String = ""
    }

    object Buttons {
        var permissionRequest: String = ""
        var about: String = ""
    }
}