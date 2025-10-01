package com.example.cupcake.test

import androidx.navigation.NavController
import com.example.cupcake.CupcakeScreen
import org.junit.Assert.assertEquals

/**
 * Hàm mở rộng cho NavController để xác nhận route hiện tại.
 */
fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}