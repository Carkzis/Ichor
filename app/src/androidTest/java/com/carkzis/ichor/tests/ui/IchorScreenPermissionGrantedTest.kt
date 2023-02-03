package com.carkzis.ichor.tests.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.testdoubles.DummyPermissionFacade
import com.carkzis.ichor.testdoubles.DummyViewModel
import com.carkzis.ichor.ui.IchorScreen
import com.carkzis.ichor.utils.PermissionFacade
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.carkzis.ichor.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class IchorScreenPermissionGrantedTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()
    private lateinit var dummyHeartRatePermissionFacade: PermissionFacade
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                dummyHeartRatePermissionFacade = DummyPermissionFacade(hasPermissionAlready = true)
                navController = rememberSwipeDismissableNavController()
                IchorScreenPermissionGrantedContentDescriptions.Buttons.apply {
                    about = stringResource(id = R.string.ichor_about_button)
                    speedChange = stringResource(id = R.string.ichor_sampling_speed_change)
                    slowSpeed = stringResource(id = R.string.ichor_slow_sampling_speed)
                    defaultSpeed = stringResource(id = R.string.ichor_default_sampling_speed)
                    fastSpeed = stringResource(id = R.string.ichor_fast_sampling_speed)
                    deleteAll = stringResource(id = R.string.ichor_delete_all_button)
                    deleteAllConfirm = stringResource(id = R.string.ichor_delete_all_confirm)
                    deleteAllReject = stringResource(id = R.string.ichor_delete_all_reject)
                    deleteSingleConfirm = stringResource(id = R.string.ichor_delete_single_confirm)
                    deleteSingleReject = stringResource(id = R.string.ichor_delete_single_reject)
                }
                IchorScreen(viewModel = DummyViewModel(), heartRatePermissionFacade = dummyHeartRatePermissionFacade)
            }
        }
    }

    @Test
    fun `ichor screen items are displayed as expected`() {
        headerDisplayed()
        liveDataDisplayed()
        buttonsDisplayed()
        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `clicking sampling speed change button results expected items displayed in dialogue`() {
        // Open sampling speed dialogue.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        // Assert on header.
        composeTestRule
            .onNodeWithContentDescription( "Change heartbeat sampling speed.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Change sampling speed?")
            .assertIsDisplayed()

        // Assert on selection of speeds to choose from.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.slowSpeed)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.defaultSpeed)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.fastSpeed)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Affirmation icon.")
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed to fast is reflected on main screen`() {
        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.fastSpeed)
            .performClick()

        composeTestRule
            .onNodeWithText("Sampling Speed: Fast")
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed to slow is reflected on main screen`() {
        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.slowSpeed)
            .performClick()

        composeTestRule
            .onNodeWithText("Sampling Speed: Slow")
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed is reflected on sampling speed dialogue when reopened`() {
        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()
        composeTestRule
            .onNodeWithTag("Default sampling with affirmation")
            .onChildren()
            .filter(hasContentDescription("Affirmation icon."))
            .assertCountEquals(1)

        // Change sampling speed to slow.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.slowSpeed)
            .performClick()

        // Reopen dialogue.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()
        composeTestRule
            .onNodeWithTag("Slow sampling with affirmation")
            .onChildren()
            .filter(hasContentDescription("Affirmation icon."))
            .assertCountEquals(1)

        // Change sampling speed to fast.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.fastSpeed)
            .performClick()

        // Reopen dialogue.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()
        composeTestRule
            .onNodeWithTag("Fast sampling with affirmation")
            .onChildren()
            .filter(hasContentDescription("Affirmation icon."))
            .assertCountEquals(1)
    }

    @Test
    fun `sample speed does not change when exiting sampling speed dialogue`() {
        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        // Swipe to exit dialogue (this will be the latter root).
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeRight()
            }

        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
    }

    @Test
    fun `clicking delete all button results expected items displayed in dialogue`() {
        // Open delete all dialogue.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAll)
            .performClick()

        // Assert on header.
        composeTestRule
            .onNodeWithContentDescription( "Delete heartbeat icon for app.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Delete all your heartbeats? This cannot be undone.")
            .assertIsDisplayed()

        // Assert on deletion options.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAllConfirm)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAllReject)
            .assertIsDisplayed()
    }

    @Test
    fun `confirming deletion of heartrates is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAll)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAllConfirm)
            .performClick()

        heartRateItemCardsAreNotDisplayed()
    }

    @Test
    fun `rejecting deletion of heartrates is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAll)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAllReject)
            .performClick()

        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `heartrates not deleted when exiting delete all dialogue`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAll)
            .performClick()

        // Swipe to exit dialogue (this will be the latter root).
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeRight()
            }

        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `swiping on single item raises dialogue with expected items`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeUp()
            }

        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .onFirst()
            .performTouchInput {
                swipeRight()
            }

        // Assert on header.
        composeTestRule
            .onNodeWithContentDescription( "Delete heartbeat icon for app.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Delete your heartbeat record of 100.0 bpm dated 2022-12-25 12:30:30?")
            .assertIsDisplayed()

        // Assert on deletion options.
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteSingleConfirm)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteSingleReject)
    }

    @Test
    fun `confirming deletion of single heartrate is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeUp()
            }

        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .onFirst()
            .performTouchInput {
                swipeRight()
            }

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteSingleConfirm)
            .performClick()

        heartRateItemCardWithExpectedDataIsNotDisplayed()
    }

    @Test
    fun `rejecting deletion of single heartrate is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeUp()
            }

        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .onFirst()
            .performTouchInput {
                swipeRight()
            }

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteSingleReject)
            .performClick()

        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `heartrates not deleted when exiting delete single heartrate dialogue`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeUp()
            }

        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .onFirst()
            .performTouchInput {
                swipeRight()
            }

        // Swipe to exit dialogue (this will be the latter root).
        composeTestRule
            .onAllNodes(isRoot())
            .onLast()
            .performTouchInput {
                swipeRight()
            }

        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `change to current heartrate is displayed on screen`() {
        composeTestRule
            .onNodeWithText("0.0 bpm")
            .assertIsDisplayed()
        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithText("1.0 bpm")
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun headerDisplayed() {
        composeTestRule
            .onNodeWithText("Ichor")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Main heartbeat icon for app.")
    }

    private fun liveDataDisplayed() {
        composeTestRule
            .onNodeWithText("Availability: Unknown")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Sampling Speed: Default")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("0.0 bpm")
            .assertIsDisplayed()
    }

    private fun buttonsDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.about)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.deleteAll)
            .assertIsDisplayed()
    }

    private fun heartRateItemCardWithExpectedDataDisplayed() {
        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .onFirst()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("2022-12-25 12:30:30")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("100.0 bpm")
            .assertIsDisplayed()
    }

    private fun heartRateItemCardWithExpectedDataIsNotDisplayed() {
        composeTestRule
            .onAllNodesWithText("2022-12-25 12:30:30")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("100.0 bpm")
            .assertCountEquals(0)
    }

    private fun heartRateItemCardsAreNotDisplayed() {
        composeTestRule
            .onAllNodesWithTag("Heart Rate Item Card")
            .assertCountEquals(0)
    }
}

object IchorScreenPermissionGrantedText {
    object Header {
        var title: String = ""
    }

    object Body {
        var bpmSubstring: String = ""
        var permissionNotGranted: String = ""
    }
}

object IchorScreenPermissionGrantedContentDescriptions {
    object Icons {
        var main: String = ""
    }

    object Buttons {
        var about: String = ""
        var speedChange: String = ""
        var slowSpeed: String = ""
        var defaultSpeed: String = ""
        var fastSpeed: String = ""
        var deleteAll: String = ""
        var deleteAllConfirm: String = ""
        var deleteAllReject: String = ""
        var deleteSingleConfirm: String = ""
        var deleteSingleReject: String = ""
    }
}