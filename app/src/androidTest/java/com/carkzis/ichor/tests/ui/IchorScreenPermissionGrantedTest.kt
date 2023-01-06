package com.carkzis.ichor.tests.ui

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
                IchorScreen(viewModel = DummyViewModel(), heartRatePermissionFacade = dummyHeartRatePermissionFacade)
            }
        }
    }

    @Test
    fun `items are displayed as expected`() {
        headerDisplayed()
        liveDataDisplayed()
        buttonsDisplayed()
        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `clicking sampling speed change button results expected items displayed in dialogue`() {
        // Open sampling speed dialogue.
        composeTestRule
            .onNodeWithContentDescription("Sampling Speed Change Button")
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
            .onNodeWithContentDescription("Button for slow sampling speed.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Button for default medium sampling speed.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Button for fast sampling speed.")
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
            .onNodeWithContentDescription("Sampling Speed Change Button")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Button for fast sampling speed.")
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
            .onNodeWithContentDescription("Sampling Speed Change Button")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Button for slow sampling speed.")
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
            .onNodeWithContentDescription("Sampling Speed Change Button")
            .performClick()
        composeTestRule
            .onNodeWithTag("Default sampling with affirmation")
            .onChildren()
            .filter(hasContentDescription("Affirmation icon."))
            .assertCountEquals(1)

        // Change sampling speed to slow.
        composeTestRule
            .onNodeWithContentDescription("Button for slow sampling speed.")
            .performClick()

        // Reopen dialogue.
        composeTestRule
            .onNodeWithContentDescription("Sampling Speed Change Button")
            .performClick()
        composeTestRule
            .onNodeWithTag("Slow sampling with affirmation")
            .onChildren()
            .filter(hasContentDescription("Affirmation icon."))
            .assertCountEquals(1)

        // Change sampling speed to fast.
        composeTestRule
            .onNodeWithContentDescription("Button for fast sampling speed.")
            .performClick()

        // Reopen dialogue.
        composeTestRule
            .onNodeWithContentDescription("Sampling Speed Change Button")
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
            .onNodeWithContentDescription("Sampling Speed Change Button")
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
            .onNodeWithContentDescription("Delete All Button")
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
            .onNodeWithContentDescription("Button for confirming delete all.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Button for rejecting delete all.")
            .assertIsDisplayed()
    }

    @Test
    fun `confirming deletion of heartrates is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Delete All Button")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Button for confirming delete all.")
            .performClick()

        heartRateItemCardWithExpectedDataIsNotDisplayed()
    }

    @Test
    fun `rejecting deletion of heartrates is reflected on main screen`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Delete All Button")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Button for rejecting delete all.")
            .performClick()

        heartRateItemCardWithExpectedDataDisplayed()
    }

    @Test
    fun `heartrates not deleted when exiting delete all dialogue`() {
        heartRateItemCardWithExpectedDataDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Delete All Button")
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
            .onNodeWithContentDescription("Confirm deletion of single heartrate.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Reject deletion of single heartrate.")
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
            .onNodeWithContentDescription("Confirm deletion of single heartrate.")
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
            .onNodeWithContentDescription("Reject deletion of single heartrate.")
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
    
    // TODO: Changes to current heartrate displayed on screen

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
            .onNodeWithContentDescription("Sampling Speed Change Button")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("About Button")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Delete All Button")
            .assertIsDisplayed()
    }

    private fun heartRateItemCardWithExpectedDataDisplayed() {
        composeTestRule
            .onNodeWithTag("Heart Rate Item Card")
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
            .onAllNodesWithTag("Heart Rate Item Card")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("2022-12-25 12:30:30")
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText("100.0 bpm")
            .assertCountEquals(0)
    }
}