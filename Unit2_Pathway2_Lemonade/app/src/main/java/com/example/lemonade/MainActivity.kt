package com.example.lemonade

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                                    color = Color.Black        // chữ đen cho nổi bật trên nền vàng
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
    var currentStep by remember { mutableStateOf(1) }
    var squeezesLeft by remember { mutableStateOf(0) }

    @StringRes val textRes: Int
    @DrawableRes val imageRes: Int
    @StringRes val descRes: Int
    val onImageClick: () -> Unit

    when (currentStep) {
        1 -> {
            textRes = R.string.lemon_select
            imageRes = R.drawable.lemon_tree
            descRes = R.string.lemon_tree_content_description
            onImageClick = {
                squeezesLeft = (2..4).random()
                currentStep = 2
            }
        }
        2 -> {
            textRes = R.string.lemon_squeeze
            imageRes = R.drawable.lemon_squeeze
            descRes = R.string.lemon_content_description
            onImageClick = {
                squeezesLeft--
                if (squeezesLeft <= 0) currentStep = 3
            }
        }
        3 -> {
            textRes = R.string.lemon_drink
            imageRes = R.drawable.lemon_drink
            descRes = R.string.lemonade_content_description
            onImageClick = { currentStep = 4 }
        }
        else -> {
            textRes = R.string.lemon_empty_glass
            imageRes = R.drawable.lemon_restart
            descRes = R.string.empty_glass_content_description
            onImageClick = {
                squeezesLeft = 0
                currentStep = 1
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LemonTextAndImage(
            textRes = textRes,
            imageRes = imageRes,
            contentDescRes = descRes,
            onImageClick = onImageClick
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
                    color = Color(0xFF69CDD8), // RGB(105,205,216)
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
