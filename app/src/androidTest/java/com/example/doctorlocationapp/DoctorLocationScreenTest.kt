package com.example.doctorlocationapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.doctorlocationapp.ui.theme.screens.ByLocationTab
import com.example.doctorlocationapp.viewmodel.DoctorLocationViewModel
import org.junit.Rule
import org.junit.Test

class DoctorLocationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testZipCodeInput() {
        val viewModel = DoctorLocationViewModel()

        composeTestRule.setContent {
            ByLocationTab(viewModel = viewModel)
        }

        // Test zip code
        val zipCodeTextField = composeTestRule.onNodeWithTag("zipCodeTextField")
        zipCodeTextField.assertExists()

        // Test sample input
        zipCodeTextField.performTextInput("12345")
        zipCodeTextField.assertTextEquals("12345")

        // Test sample invalid input
        zipCodeTextField.performTextInput("1234a")
        zipCodeTextField.assertTextEquals("1234")
    }

    @Test
    fun testSearchByZipButton() {
        val viewModel = DoctorLocationViewModel()

        // composable
        composeTestRule.setContent {
            ByLocationTab(viewModel = viewModel)
        }

        // Test button click
        val searchButton = composeTestRule.onNodeWithText("SEARCH BY ZIP")
        searchButton.assertExists()
        searchButton.performClick()

    }

    @Test
    fun testLocationPermissionButton() {
        val viewModel = DoctorLocationViewModel()

        // composable
        composeTestRule.setContent {
            ByLocationTab(viewModel = viewModel)
        }

        // Test button click
        val locationButton = composeTestRule.onNodeWithText("USE MY LOCATION")
        locationButton.assertExists()
        locationButton.performClick()

    }

    @Test
    fun testSearchByCountyClick() {
        val viewModel = DoctorLocationViewModel()

        // composable
        composeTestRule.setContent {
            ByLocationTab(viewModel = viewModel)
        }

        // Test Search by County
        val searchByCountyRow = composeTestRule.onNodeWithText("Search by County")
        searchByCountyRow.assertExists()
        searchByCountyRow.performClick()

        // AlertDialog
        composeTestRule.onNodeWithText("Search by County Tapped").assertExists()
    }
}
