package com.carkzis.ichor.tests.ui

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.health.services.client.data.Availability
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
    private lateinit var aboutAbout: String
    private lateinit var whatYouCanSee: String
    private lateinit var availabilitySubtitle: String
    private lateinit var availability: String
    private lateinit var samplingSpeedSubtitle: String
    private lateinit var samplingSpeedDisplay: String
    private lateinit var bpmSubtitle: String
    private lateinit var bpm: String
    private lateinit var historySubtitle: String
    private lateinit var history: String
    private lateinit var whatYouCanDo: String
    private lateinit var samplingSpeed: String
    private lateinit var slowSampling: String
    private lateinit var defaultSampling: String
    private lateinit var fastSampling: String
    private lateinit var deleteAll: String
    private lateinit var deleteOneSubtitle: String
    private lateinit var deleteOne: String
    private lateinit var furtherInformation: String
    private lateinit var furtherInformationDetails: String

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                title = stringResource(id = R.string.about_ichor)
                aboutDescription = stringResource(id = R.string.about_description)
                startingUp = stringResource(id = R.string.about_starting_up)
                permissions = stringResource(id = R.string.about_permissions)
                aboutAbout = stringResource(id = R.string.about_about)
                whatYouCanSee = stringResource(id = R.string.about_what_you_can_see)
                availabilitySubtitle = stringResource(id = R.string.about_availability_subtitle)
                availability = stringResource(id = R.string.about_availability)
                samplingSpeedSubtitle = stringResource(id = R.string.about_sampling_speed_subtitle)
                samplingSpeedDisplay = stringResource(id = R.string.about_sampling_speed_display)
                bpmSubtitle = stringResource(id = R.string.about_bpm_subtitle)
                bpm = stringResource(id = R.string.about_bpm)
                historySubtitle = stringResource(id = R.string.about_history_subtitle)
                history = stringResource(id = R.string.about_history)
                whatYouCanDo = stringResource(id = R.string.about_what_you_can_do)
                samplingSpeed = stringResource(id = R.string.about_sampling_speed)
                slowSampling = stringResource(id = R.string.about_slow_sampling)
                defaultSampling = stringResource(id = R.string.about_default_sampling)
                fastSampling = stringResource(id = R.string.about_fast_sampling)
                deleteAll = stringResource(id = R.string.about_delete_all)
                deleteOneSubtitle = stringResource(id = R.string.about_delete_one_subtitle)
                deleteOne = stringResource(id = R.string.about_delete_one)
                furtherInformation = stringResource(id = R.string.about_further_information)
                furtherInformationDetails = stringResource(id = R.string.about_further_information_details)

                navController = rememberSwipeDismissableNavController()
                AboutScreen()
            }
        }
    }

    @Test
    fun `about screen text items are displayed as expected`() {
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