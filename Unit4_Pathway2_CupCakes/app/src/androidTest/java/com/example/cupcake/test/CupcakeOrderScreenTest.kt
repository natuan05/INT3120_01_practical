package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import com.example.cupcake.R
import com.example.cupcake.ui.SelectOptionScreen
import org.junit.Test

class CupcakeOrderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Các bài kiểm thử sẽ được viết ở đây
    @Test
    fun selectOptionScreen_verifyContent() {
        // 1. Chuẩn bị dữ liệu giả
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        val subtotal = "$100"

        // 2. Tải Composable cần kiểm thử với dữ liệu giả
        composeTestRule.setContent {
            SelectOptionScreen(
                subtotal = subtotal,
                options = flavors,
                // Để trống các hàm callback vì chúng ta không kiểm thử điều hướng ở đây
                onNextButtonClicked = {},
                onCancelButtonClicked = {}
            )
        }

        // 3. Kiểm tra xem tất cả các lựa chọn hương vị có hiển thị không
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        // 4. Kiểm tra xem giá tiền tạm tính có hiển thị đúng định dạng không
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        // 5. Kiểm tra xem nút "Next" có bị vô hiệu hóa (disabled) không
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }
}