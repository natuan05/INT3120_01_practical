package com.example.lemonade

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "Lemonade",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black
                                )
                            },
                            modifier = Modifier.height(56.dp),
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color(0xFFFFEB3B)
                            )
                        )
                    }
                ) { innerPadding ->
                    LemonApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LemonApp(modifier: Modifier = Modifier) {
    // SỬA LẠI Ở ĐÂY: Dùng rememberSaveable để lưu trạng thái khi xoay màn hình
    var currentStep by rememberSaveable { mutableStateOf(1) }
    var squeezeCount by rememberSaveable { mutableStateOf(0) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (currentStep == 3 && isLandscape) {
            LandscapeDrinkScreen()
        } else {
            val imageRes: Int
            val textRes: Int
            val descRes: Int
            val onImageClick: () -> Unit

            when (currentStep) {
                1 -> {
                    imageRes = R.drawable.lemon_tree
                    textRes = R.string.lemon_select
                    descRes = R.string.lemon_tree_content_description
                    onImageClick = {
                        currentStep = 2
                        squeezeCount = (2..4).random()
                    }
                }
                2 -> {
                    imageRes = R.drawable.lemon_squeeze
                    textRes = R.string.lemon_squeeze
                    descRes = R.string.lemon_content_description
                    onImageClick = {
                        squeezeCount--
                        if (squeezeCount == 0) {
                            currentStep = 3
                        }
                    }
                }
                3 -> {
                    imageRes = R.drawable.lemon_drink
                    textRes = R.string.lemon_drink
                    descRes = R.string.lemonade_content_description
                    onImageClick = { currentStep = 4 }
                }
                else -> {
                    imageRes = R.drawable.lemon_restart
                    textRes = R.string.lemon_empty_glass
                    descRes = R.string.empty_glass_content_description
                    onImageClick = { currentStep = 1 }
                }
            }

            LemonTextAndImage(
                textRes = textRes,
                imageRes = imageRes,
                contentDescRes = descRes,
                onImageClick = onImageClick
            )
        }
    }
}


@Composable
private fun LandscapeDrinkScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.lemon_drink_no_ice),
            contentDescription = stringResource(R.string.lemonade_content_description),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = stringResource(R.string.ice_melted),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
    }
}

@Composable
fun LemonTextAndImage(
    @StringRes textRes: Int,
    @DrawableRes imageRes: Int,
    @StringRes contentDescRes: Int,
    onImageClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(textRes),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(contentDescRes),
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE0F7FA))
                .border(
                    width = 2.dp,
                    color = Color(0xFF69CDD8),
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable(onClick = onImageClick)
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LemonadeTheme { LemonApp() }
}