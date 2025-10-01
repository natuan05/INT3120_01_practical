package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import org.junit.Before
import org.junit.Rule
import com.example.cupcake.CupcakeScreen
import org.junit.Assert.assertEquals
import org.junit.Test

class CupcakeScreenNavigationTest {

    /**
     * Quy tắc kiểm thử (test rule) để tương tác với các Composable.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    /**
     * Bộ điều khiển điều hướng (NavController) đặc biệt dành cho việc kiểm thử.
     */
    private lateinit var navController: TestNavHostController

    /**
     * Hàm này được đánh dấu @Before, nó sẽ tự động chạy trước mỗi bài kiểm thử (@Test).
     * Nhiệm vụ của nó là thiết lập môi trường kiểm thử.
     */
    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            // 1. Khởi tạo TestNavHostController
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            // 2. "Tiêm" NavController này vào ứng dụng để chúng ta có thể kiểm soát
            CupcakeApp(navController = navController)
        }
    }

    // Các bài kiểm thử sẽ được viết ở đây trong các bước tiếp theo
    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CupcakeScreen.Start.name)
    }
}