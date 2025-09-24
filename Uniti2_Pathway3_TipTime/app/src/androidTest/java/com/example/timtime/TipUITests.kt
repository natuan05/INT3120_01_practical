package com.example.tiptime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat
import com.example.tiptime.ui.theme.TipTimeTheme

class TipUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }

        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("10")

        composeTestRule.onNodeWithText("Tip Percentage")
            .performTextInput("20")

        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "No node with this text was found."
        )
    }


    @Test
    fun calculate_default_15_percent_no_round() {
        composeTestRule.setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }

        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("50")
        composeTestRule.onNodeWithText("Tip Percentage")
            .performTextInput("15")

        val expectedTip = NumberFormat.getCurrencyInstance().format(7.5)
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "Expected 15% tip without rounding to be shown."
        )
    }

    @Test
    fun calculate_18_percent_with_round_up() {
        composeTestRule.setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }



        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("33.33")
        composeTestRule.onNodeWithText("Tip Percentage")
            .performTextInput("18")

        val expectedTip = NumberFormat.getCurrencyInstance().format(6)

        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "Expected rounded tip to be shown as 6."
        )
    }
}
