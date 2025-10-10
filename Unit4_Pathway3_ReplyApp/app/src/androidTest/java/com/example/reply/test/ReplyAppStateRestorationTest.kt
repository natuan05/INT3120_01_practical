package com.example.reply.test

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.reply.ui.ReplyApp
import com.example.reply.ui.theme.ReplyTheme

class ReplyAppStateRestorationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun compactDevice_selectedEmailRetained_afterConfigChange() {
        // 1. Thiết lập trình kiểm thử khôi phục trạng thái
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            ReplyTheme {
                ReplyApp(windowSize = WindowWidthSizeClass.Compact)
            }
        }

        // 2. Tương tác với UI: Nhấn vào email thứ 3
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[2].subject)
        ).performClick()

        // 3. Xác nhận đã chuyển sang màn hình chi tiết
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[2].body)
        ).assertExists()

        // 4. Giả lập một thay đổi cấu hình (như xoay màn hình)
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // 5. Xác nhận lại: Ứng dụng vẫn đang ở màn hình chi tiết của email thứ 3
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.navigation_back).assertExists()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(LocalEmailsDataProvider.allEmails[2].body)
        ).assertExists()
    }
}