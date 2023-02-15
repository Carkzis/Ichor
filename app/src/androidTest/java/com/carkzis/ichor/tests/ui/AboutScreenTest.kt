package com.carkzis.ichor.tests.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.carkzis.ichor.R
import com.carkzis.ichor.ui.about.AboutScreen
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

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                obtainStringResourcesForAboutScreen()

                navController = rememberSwipeDismissableNavController()
                AboutScreen()
            }
        }
    }

    @Test
    fun `about screen items are displayed as expected`() {
        headerDisplayed()
        startingUpItemsDisplayed()
        whatYouCanSeeItemsDisplayed()
        whatYouCanDoItemsDisplayed()
        furtherInformationDisplayed()
    }

    @Composable
    private fun obtainStringResourcesForAboutScreen() {
        obtainTextStringResourcesForAboutScreen()
        obtainContentDescriptionStringResourcesForAboutScreen()
        obtainTestTagStringResourcesForAboutScreen()
    }

    @Composable
    private fun obtainTextStringResourcesForAboutScreen() {
        AboutScreenText.Header.apply {
            title = stringResource(id = R.string.about_ichor)
            aboutDescription = stringResource(id = R.string.about_description)
        }
        AboutScreenText.StartingUp.apply {
            startingUp = stringResource(id = R.string.about_starting_up)
            permissions = stringResource(id = R.string.about_permissions)
            aboutAbout = stringResource(id = R.string.about_about)
        }
        AboutScreenText.WhatYouCanSee.apply {
            whatYouCanSee = stringResource(id = R.string.about_what_you_can_see)
            availabilitySubtitle = stringResource(id = R.string.about_availability_subtitle)
            availability = stringResource(id = R.string.about_availability)
            samplingSpeedSubtitle = stringResource(id = R.string.about_sampling_speed_subtitle)
            samplingSpeedDisplay = stringResource(id = R.string.about_sampling_speed_display)
            bpmSubtitle = stringResource(id = R.string.about_bpm_subtitle)
            bpm = stringResource(id = R.string.about_bpm)
            historySubtitle = stringResource(id = R.string.about_history_subtitle)
            history = stringResource(id = R.string.about_history)
        }
        AboutScreenText.WhatYouCanDo.apply {
            whatYouCanDo = stringResource(id = R.string.about_what_you_can_do)
            samplingSpeed = stringResource(id = R.string.about_sampling_speed)
            slowSampling = stringResource(id = R.string.about_slow_sampling)
            defaultSampling = stringResource(id = R.string.about_default_sampling)
            fastSampling = stringResource(id = R.string.about_fast_sampling)
            deleteAll = stringResource(id = R.string.about_delete_all)
            deleteOneSubtitle = stringResource(id = R.string.about_delete_one_subtitle)
            deleteOne = stringResource(id = R.string.about_delete_one)
        }
        AboutScreenText.FurtherInformation.apply {
            furtherInformation = stringResource(id = R.string.about_further_information)
            furtherInformationDetails =
                stringResource(id = R.string.about_further_information_details)
        }
    }

    @Composable
    private fun obtainContentDescriptionStringResourcesForAboutScreen() {
        AboutScreenContentDescriptions.Icons.apply {
            title = stringResource(id = R.string.about_ichor_icon)
            permissions = stringResource(id = R.string.about_permissions_icon)
            about = stringResource(id = R.string.about_about_icon)
            samplingSpeed = stringResource(id = R.string.about_sampling_speed_icon)
            slowSamplingSpeed = stringResource(id = R.string.about_slow_sampling_icon)
            defaultSamplingSpeed = stringResource(id = R.string.about_default_sampling_icon)
            fastSamplingSpeed = stringResource(id = R.string.about_fast_sampling_icon)
            deleteAll = stringResource(id = R.string.about_delete_all_icon)
        }
    }

    @Composable
    private fun obtainTestTagStringResourcesForAboutScreen() {
        AboutScreenTags.Rows.apply {
            permissions = stringResource(id = R.string.about_permissions_row_tag)
            about = stringResource(id = R.string.about_about_row_tag)
            samplingSpeed = stringResource(id = R.string.about_sampling_speeds_row_tag)
            slowSamplingSpeed = stringResource(id = R.string.about_slow_sampling_row_tag)
            defaultSamplingSpeed = stringResource(id = R.string.about_default_sampling_row_tag)
            fastSamplingSpeed = stringResource(id = R.string.about_fast_sampling_row_tag)
            deleteAll = stringResource(id = R.string.about_delete_all_row_tag)
        }
    }

    private fun headerDisplayed() {
        composeTestRule
            .onNodeWithContentDescription(AboutScreenContentDescriptions.Icons.title)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(AboutScreenText.Header.title)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(AboutScreenText.Header.aboutDescription)
            .assertIsDisplayed()
    }

    private fun startingUpItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenText.StartingUp.startingUp))

        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.permissions,
            iconContentDescription = AboutScreenContentDescriptions.Icons.permissions,
            bodyText = AboutScreenText.StartingUp.permissions
        )

        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.about,
            iconContentDescription = AboutScreenContentDescriptions.Icons.about,
            bodyText = AboutScreenText.StartingUp.aboutAbout
        )
    }

    private fun whatYouCanSeeItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.whatYouCanSee))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.availabilitySubtitle))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.availability))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.samplingSpeedSubtitle))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.samplingSpeedDisplay))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.bpmSubtitle))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.bpm))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.historySubtitle))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanSee.history))
    }

    private fun whatYouCanDoItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanDo.whatYouCanDo))

        sampleSpeedRowsDisplayed()

        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.slowSamplingSpeed,
            iconContentDescription = AboutScreenContentDescriptions.Icons.slowSamplingSpeed,
            bodyText = AboutScreenText.WhatYouCanDo.slowSampling
        )
        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.defaultSamplingSpeed,
            iconContentDescription = AboutScreenContentDescriptions.Icons.defaultSamplingSpeed,
            bodyText = AboutScreenText.WhatYouCanDo.defaultSampling
        )
        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.fastSamplingSpeed,
            iconContentDescription = AboutScreenContentDescriptions.Icons.fastSamplingSpeed,
            bodyText = AboutScreenText.WhatYouCanDo.fastSampling
        )

        rowItemsDisplayed(rowTestTag = AboutScreenTags.Rows.deleteAll,
            iconContentDescription = AboutScreenContentDescriptions.Icons.deleteAll,
            bodyText = AboutScreenText.WhatYouCanDo.deleteAll
        )

        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanDo.deleteOneSubtitle))
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanDo.deleteOne))
    }

    private fun rowItemsDisplayed(rowTestTag: String, iconContentDescription: String, bodyText: String) {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasTestTag(rowTestTag))
        composeTestRule.onNodeWithTag(rowTestTag)
            .onChildren()
            .filter(
                hasContentDescription(iconContentDescription)
                    .or(hasText(bodyText))
            )
            .assertCountEquals(2)
    }

    private fun sampleSpeedRowsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasTestTag(AboutScreenTags.Rows.samplingSpeed))
        composeTestRule.onNodeWithTag(AboutScreenTags.Rows.samplingSpeed)
            .performScrollToNode(hasText(AboutScreenText.WhatYouCanDo.samplingSpeed))
            .onChildren()
            .filter(hasTestTag(AboutScreenTags.Rows.slowSamplingSpeed)
                .or(hasTestTag(AboutScreenTags.Rows.defaultSamplingSpeed))
                .or(hasTestTag(AboutScreenTags.Rows.fastSamplingSpeed)))
            .assertCountEquals(3)
    }


    private fun furtherInformationDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenText.FurtherInformation.furtherInformation))
            .performScrollToNode(hasText(AboutScreenText.FurtherInformation.furtherInformationDetails))
    }

}

object AboutScreenText {
    object Header {
        var title: String = ""
        var aboutDescription: String = ""
    }
    object StartingUp {
        var startingUp: String = ""
        var permissions: String = ""
        var aboutAbout: String = ""
    }
    object WhatYouCanSee {
        var whatYouCanSee: String = ""
        var availabilitySubtitle: String = ""
        var availability: String = ""
        var samplingSpeedSubtitle: String = ""
        var samplingSpeedDisplay: String = ""
        var bpmSubtitle: String = ""
        var bpm: String = ""
        var historySubtitle: String = ""
        var history: String = ""
    }
    object WhatYouCanDo {
        var whatYouCanDo: String = ""
        var samplingSpeed: String = ""
        var slowSampling: String = ""
        var defaultSampling: String = ""
        var fastSampling: String = ""
        var deleteAll: String = ""
        var deleteOneSubtitle: String = ""
        var deleteOne: String = ""
    }
    object FurtherInformation {
        var furtherInformation: String = ""
        var furtherInformationDetails: String = ""
    }
}

object AboutScreenTags {
    object Rows {
        var permissions: String = ""
        var about: String = ""
        var samplingSpeed: String = ""
        var slowSamplingSpeed: String = ""
        var defaultSamplingSpeed: String = ""
        var fastSamplingSpeed: String = ""
        var deleteAll: String = ""
    }
}

object AboutScreenContentDescriptions {
    object Icons {
        var title: String = ""
        var permissions: String = ""
        var about: String = ""
        var samplingSpeed: String = ""
        var slowSamplingSpeed: String = ""
        var defaultSamplingSpeed: String = ""
        var fastSamplingSpeed: String = ""
        var deleteAll: String = ""
    }
}