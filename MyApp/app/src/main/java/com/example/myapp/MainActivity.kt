package com.example.navsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- Định nghĩa các "địa chỉ" (route) cho các màn hình ---
enum class AppScreen {
    Login,
    Home,
    Detail,
    Profile
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()

            // Lấy tên màn hình hiện tại để cập nhật AppBar
            val currentScreen = AppScreen.valueOf(
                backStackEntry?.destination?.route?.substringBefore("/") ?: AppScreen.Login.name
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentScreen.name) },
                        navigationIcon = {
                            // Chỉ hiển thị nút back nếu có màn hình để quay lại
                            if (navController.previousBackStackEntry != null) {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                // --- Đây là "Bản đồ" NavGraph ---
                NavHost(
                    navController = navController,
                    startDestination = AppScreen.Login.name,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(route = AppScreen.Login.name) {
                        LoginScreen(
                            onLoginClick = {
                                // Đi tới Home và XÓA màn hình Login khỏi back stack
                                navController.navigate(AppScreen.Home.name) {
                                    popUpTo(AppScreen.Login.name) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(route = AppScreen.Home.name) {
                        HomeScreen(
                            onGoToDetailClick = { itemId ->
                                // Đi tới Detail và truyền tham số `itemId`
                                navController.navigate("${AppScreen.Detail.name}/$itemId")
                            },
                            onGoToProfileClick = {
                                navController.navigate(AppScreen.Profile.name)
                            }
                        )
                    }

                    // Định nghĩa route có tham số là "itemId"
                    composable(
                        route = "${AppScreen.Detail.name}/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getInt("itemId")
                        DetailScreen(itemId = itemId)
                    }

                    composable(route = AppScreen.Profile.name) {
                        ProfileScreen(
                            onLogoutClick = {
                                // Quay về Login và XÓA TOÀN BỘ back stack
                                navController.navigate(AppScreen.Login.name) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- Các màn hình Composable đơn giản ---

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Màn hình Đăng nhập")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLoginClick) {
            Text("Đăng nhập")
        }
    }
}

@Composable
fun HomeScreen(onGoToDetailClick: (Int) -> Unit, onGoToProfileClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Màn hình Trang chủ")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onGoToDetailClick(123) }) {
            Text("Xem chi tiết mục #123")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onGoToProfileClick) {
            Text("Đi đến trang Cá nhân")
        }
    }
}

@Composable
fun DetailScreen(itemId: Int?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Màn hình Chi tiết")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Bạn đang xem mục có ID: $itemId")
    }
}

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Màn hình Cá nhân")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogoutClick) {
            Text("Đăng xuất")
        }
    }
}