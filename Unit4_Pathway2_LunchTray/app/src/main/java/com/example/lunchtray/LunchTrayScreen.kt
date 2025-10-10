// app/src/main/java/com/example/lunchtray/LunchTrayScreen.kt
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lunchtray.ui.OrderViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.StartOrderScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.formatPrice
import com.example.lunchtray.R

// 1) Khai báo enum màn hình với tiêu đề (title) tương ứng trong strings.xml
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.start_order),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}

// 2) AppBar hiển thị tiêu đề, nút back (khi có thể) và nút Cancel (trừ màn Start)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    onNavigateUp: () -> Unit,
    onCancelOrder: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = currentScreen.title)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen != LunchTrayScreen.Start) {
                TextButton(onClick = onCancelOrder) {
                    Text(text = stringResource(R.string.cancel), style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp() {
    // 3) Tạo NavController
    val navController = rememberNavController()

    // 4) Tạo ViewModel
    val viewModel: OrderViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Theo dõi màn hình hiện tại
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = runCatching {
        LunchTrayScreen.valueOf(backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name)
    }.getOrElse { LunchTrayScreen.Start }

    // Hàm tiện ích: quay về Start và reset đơn
    fun cancelAndGoToStart() {
        viewModel.resetOrder()
        navController.navigate(LunchTrayScreen.Start.name) {
            popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                onNavigateUp = { navController.navigateUp() },
                onCancelOrder = { cancelAndGoToStart() }
            )
        }
    ) { innerPadding ->
        // 5) NavHost + các destination
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
        ) {
            // Start
            composable(LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        viewModel.resetOrder() // bắt đầu đơn mới
                        navController.navigate(LunchTrayScreen.Entree.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Entree
            composable(LunchTrayScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = { cancelAndGoToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.SideDish.name)
                    },
                    onSelectionChanged = { entree ->
                        viewModel.updateEntree(entree)
                    },
                    modifier = Modifier
                )
            }

            // Side Dish
            composable(LunchTrayScreen.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = { cancelAndGoToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.Accompaniment.name)
                    },
                    onSelectionChanged = { side ->
                        viewModel.updateSideDish(side)
                    },
                    modifier = Modifier
                )
            }

            // Accompaniment
            composable(LunchTrayScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = { cancelAndGoToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.Checkout.name)
                    },
                    onSelectionChanged = { acc ->
                        viewModel.updateAccompaniment(acc)
                    },
                    modifier = Modifier
                )
            }

            // Checkout
            composable(LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelAndGoToStart() },
                    onNextButtonClicked = {
                        cancelAndGoToStart()
                    },
                    modifier = Modifier
                )
            }
        }
    }
}




