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

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.apply {
            setContent {
                AboutScreenStrings.Header.apply {
                    title = stringResource(id = R.string.about_ichor)
                    aboutDescription = stringResource(id = R.string.about_description)
                }
                AboutScreenStrings.StartingUp.apply {
                    startingUp = stringResource(id = R.string.about_starting_up)
                    permissions = stringResource(id = R.string.about_permissions)
                    aboutAbout = stringResource(id = R.string.about_about)
                }
                AboutScreenStrings.WhatYouCanSee.apply {
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
                AboutScreenStrings.WhatYouCanDo.apply {
                    whatYouCanDo = stringResource(id = R.string.about_what_you_can_do)
                    samplingSpeed = stringResource(id = R.string.about_sampling_speed)
                    slowSampling = stringResource(id = R.string.about_slow_sampling)
                    defaultSampling = stringResource(id = R.string.about_default_sampling)
                    fastSampling = stringResource(id = R.string.about_fast_sampling)
                    deleteAll = stringResource(id = R.string.about_delete_all)
                    deleteOneSubtitle = stringResource(id = R.string.about_delete_one_subtitle)
                    deleteOne = stringResource(id = R.string.about_delete_one)
                }
                AboutScreenStrings.FurtherInformation.apply {
                    furtherInformation = stringResource(id = R.string.about_further_information)
                    furtherInformationDetails = stringResource(id = R.string.about_further_information_details)
                }

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

    private fun headerDisplayed() {
        composeTestRule
            .onNodeWithContentDescription("Learn more about Ichor.")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(AboutScreenStrings.Header.title)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(AboutScreenStrings.Header.aboutDescription)
            .assertIsDisplayed()
    }

    private fun startingUpItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenStrings.StartingUp.startingUp))
            .performScrollToNode(hasText(AboutScreenStrings.StartingUp.permissions))
            .performScrollToNode(hasText(AboutScreenStrings.StartingUp.aboutAbout))
    }

    private fun whatYouCanSeeItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.whatYouCanSee))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.availabilitySubtitle))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.availability))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.samplingSpeedSubtitle))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.samplingSpeedDisplay))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.bpmSubtitle))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.bpm))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.historySubtitle))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanSee.history))
    }

    private fun whatYouCanDoItemsDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.whatYouCanDo))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.samplingSpeed))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.defaultSampling))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.fastSampling))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.deleteAll))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.deleteOneSubtitle))
            .performScrollToNode(hasText(AboutScreenStrings.WhatYouCanDo.deleteOne))
    }

    private fun furtherInformationDisplayed() {
        composeTestRule
            .onNode(hasScrollToKeyAction())
            .performScrollToNode(hasText(AboutScreenStrings.FurtherInformation.furtherInformation))
            .performScrollToNode(hasText(AboutScreenStrings.FurtherInformation.furtherInformationDetails))
    }

    object AboutScreenStrings {
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
}