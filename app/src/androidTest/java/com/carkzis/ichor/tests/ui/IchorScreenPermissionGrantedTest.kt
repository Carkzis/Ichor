package com.carkzis.ichor.tests.ui

import androidx.compose.runtime.Composable
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
                obtainStringResourcesForIchorScreenWithPermissionsGranted()
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
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.samplingSpeed)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedQuery)
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
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.speedAffirmation)
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed to fast is reflected on main screen`() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.fastSpeed)
            .performClick()

        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedFast)
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed to slow is reflected on main screen`() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.slowSpeed)
            .performClick()

        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedSlow)
            .assertIsDisplayed()
    }

    @Test
    fun `change of sampling speed is reflected on sampling speed dialogue when reopened`() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Buttons.speedChange)
            .performClick()

        composeTestRule
            .onNodeWithTag(IchorScreenPermissionGrantedTags.Rows.defaultSpeedAffirmationRow)
            .onChildren()
            .filter(hasContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.speedAffirmation))
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
            .onNodeWithTag(IchorScreenPermissionGrantedTags.Rows.slowSpeedAffirmationRow)
            .onChildren()
            .filter(hasContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.speedAffirmation))
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
            .onNodeWithTag(IchorScreenPermissionGrantedTags.Rows.fastSpeedAffirmationRow)
            .onChildren()
            .filter(hasContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.speedAffirmation))
            .assertCountEquals(1)
    }

    @Test
    fun `sample speed does not change when exiting sampling speed dialogue`() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
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
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
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
            .onNodeWithContentDescription( IchorScreenPermissionGrantedContentDescriptions.Icons.delete)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.deleteAllFinalWarning)
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
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
            .onFirst()
            .performTouchInput {
                swipeRight()
            }

        // Assert on header.
        val dummyBpm = "100.0"
        val dummyDate = "2022-12-25 12:30:30"
        val constructedDeletionQueryMessage = "${IchorScreenPermissionGrantedText.Body.deleteRecordMessagePreBpm}$dummyBpm${IchorScreenPermissionGrantedText.Body.deleteRecordMessagePreDate}$dummyDate?"
        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.delete)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(constructedDeletionQueryMessage)
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
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
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
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
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
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
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
        val initialBpmText = "0.0${IchorScreenPermissionGrantedText.Body.bpmSuffix}"
        val finalBpmText = "1.0${IchorScreenPermissionGrantedText.Body.bpmSuffix}"
        composeTestRule
            .onNodeWithText(initialBpmText)
            .assertIsDisplayed()

        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithText(finalBpmText)
                .fetchSemanticsNodes().size == 1
        }
    }

    @Composable
    private fun obtainStringResourcesForIchorScreenWithPermissionsGranted() {
        obtainTextStringResourcesForIchorScreenWithPermissionsGranted()
        obtainContentDescriptionStringResourcesForIchorScreenWithPermissionsGranted()
        obtainTestTagStringResourcesForIchorScreenWithPermissionsGranted()
    }

    @Composable
    private fun obtainTextStringResourcesForIchorScreenWithPermissionsGranted() {
        IchorScreenPermissionGrantedText.Header.title = stringResource(id = R.string.app_name)

        IchorScreenPermissionGrantedText.Body.apply {
            bpmSuffix = stringResource(id = R.string.ichor_bpm_suffix)
            currentSamplingSpeedQuery =
                stringResource(id = R.string.ichor_change_sampling_speed_question)
            currentSamplingSpeedPrefix = stringResource(id = R.string.ichor_sample_speed_prefix)
            currentSamplingSpeedDefault = "${currentSamplingSpeedPrefix}Default"
            currentSamplingSpeedSlow = "${currentSamplingSpeedPrefix}Slow"
            currentSamplingSpeedFast = "${currentSamplingSpeedPrefix}Fast"
            currentAvailabilityPrefix = stringResource(id = R.string.ichor_availability_prefix)
            deleteAllFinalWarning = stringResource(id = R.string.ichor_delete_all_final)
            deleteRecordMessagePreBpm = stringResource(id = R.string.ichor_delete_record_part_1)
            deleteRecordMessagePreDate = stringResource(id = R.string.ichor_delete_record_part_2)
        }
    }

    @Composable
    private fun obtainContentDescriptionStringResourcesForIchorScreenWithPermissionsGranted() {
        IchorScreenPermissionGrantedContentDescriptions.Icons.apply {
            main = stringResource(id = R.string.app_name)
            samplingSpeed = stringResource(id = R.string.ichor_main_heartbeat_icon)
            speedAffirmation = stringResource(id = R.string.ichor_affirmation_icon)
            delete = stringResource(id = R.string.ichor_delete_heartbeat_icon)
        }

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
    }

    @Composable
    private fun obtainTestTagStringResourcesForIchorScreenWithPermissionsGranted() {
        IchorScreenPermissionGrantedTags.Cards.heartRateCard =
            stringResource(id = R.string.card_heart_rate_item)

        IchorScreenPermissionGrantedTags.Rows.apply {
            defaultSpeedAffirmationRow =
                stringResource(id = R.string.ichor_default_sampling_row_tag)
            slowSpeedAffirmationRow = stringResource(id = R.string.ichor_slow_sampling_row_tag)
            fastSpeedAffirmationRow = stringResource(id = R.string.ichor_fast_sampling_row_tag)
        }
    }

    private fun headerDisplayed() {
        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Header.title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(IchorScreenPermissionGrantedContentDescriptions.Icons.main)
    }

    private fun liveDataDisplayed() {
        composeTestRule
            .onNodeWithText("${IchorScreenPermissionGrantedText.Body.currentAvailabilityPrefix}Unknown")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(IchorScreenPermissionGrantedText.Body.currentSamplingSpeedDefault)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("0.0${IchorScreenPermissionGrantedText.Body.bpmSuffix}")
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
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
            .onFirst()
            .assertIsDisplayed()

        val dummyDate = "2022-12-25 12:30:30"
        composeTestRule
            .onNodeWithText(dummyDate)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("100.0${IchorScreenPermissionGrantedText.Body.bpmSuffix}")
            .assertIsDisplayed()
    }

    private fun heartRateItemCardWithExpectedDataIsNotDisplayed() {
        val dummyDate = "2022-12-25 12:30:30"
        composeTestRule
            .onAllNodesWithText(dummyDate)
            .assertCountEquals(0)

        composeTestRule
            .onAllNodesWithText("100.0${IchorScreenPermissionGrantedText.Body.bpmSuffix}")
            .assertCountEquals(0)
    }

    private fun heartRateItemCardsAreNotDisplayed() {
        composeTestRule
            .onAllNodesWithTag(IchorScreenPermissionGrantedTags.Cards.heartRateCard)
            .assertCountEquals(0)
    }
}

object IchorScreenPermissionGrantedText {
    object Header {
        var title: String = ""
    }

    object Body {
        var bpmSuffix: String = ""
        var currentSamplingSpeedPrefix: String = ""
        var currentSamplingSpeedQuery: String = ""
        var currentSamplingSpeedSlow: String = ""
        var currentSamplingSpeedDefault: String = ""
        var currentSamplingSpeedFast: String = ""
        var currentAvailabilityPrefix: String = ""
        var deleteAllFinalWarning: String = ""
        var deleteRecordMessagePreBpm: String = ""
        var deleteRecordMessagePreDate: String = ""
    }
}

object IchorScreenPermissionGrantedContentDescriptions {
    object Icons {
        var main: String = ""
        var samplingSpeed: String = ""
        var speedAffirmation: String = ""
        var delete: String = ""
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

object IchorScreenPermissionGrantedTags {
    object Rows {
        var defaultSpeedAffirmationRow: String = ""
        var slowSpeedAffirmationRow: String = ""
        var fastSpeedAffirmationRow: String = ""
    }

    object Cards {
        var heartRateCard: String = ""
    }
}