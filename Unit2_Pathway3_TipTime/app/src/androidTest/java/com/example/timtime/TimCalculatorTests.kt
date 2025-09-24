package com.example.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

class TipCalculatorTests {

    @Test
    fun calculateTip_20PercentNoRoundup() {
        // Arrange
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)

        // Act
        val actualTip = calculateTip(
            amount = amount,
            tipPercent = tipPercent,
            roundUp = false
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_Default15Percent_NoRoundUp() {
        // Arrange
        val amount = 50.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(7.5)

        // Act: không truyền tipPercent để dùng default = 15.0
        val actualTip = calculateTip(
            amount = amount,
            roundUp = false
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_18Percent_RoundUp() {
        // Arrange
        val amount = 33.33
        val tipPercent = 18.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(6)

        // Act
        val actualTip = calculateTip(
            amount = amount,
            tipPercent = tipPercent,
            roundUp = true
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_ZeroAmount() {
        // Arrange
        val amount = 0.0
        val tipPercent = 20.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(0)

        // Act
        val actualTip = calculateTip(
            amount = amount,
            tipPercent = tipPercent,
            roundUp = false
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_SmallAmount_RoundUp() {
        // Arrange
        val amount = 1.0
        val tipPercent = 20.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(1)

        // Act
        val actualTip = calculateTip(
            amount = amount,
            tipPercent = tipPercent,
            roundUp = true
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }

    @Test
    fun calculateTip_SmallAmount_NoRoundUp() {
        // Arrange
        val amount = 1.0
        val tipPercent = 20.0
        val expectedTip = NumberFormat.getCurrencyInstance().format(0.2)

        // Act
        val actualTip = calculateTip(
            amount = amount,
            tipPercent = tipPercent,
            roundUp = false
        )

        // Assert
        assertEquals(expectedTip, actualTip)
    }
}
