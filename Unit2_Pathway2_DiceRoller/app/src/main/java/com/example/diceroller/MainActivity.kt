package com.example.diceroller

import android.os.Bundle
import android.util.Log // <--- 1. Import lớp Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diceroller.ui.theme.DiceRollerTheme

// 2. Tạo một TAG hằng số để lọc log dễ dàng
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // LOG: Ghi lại khi Activity được tạo
        Log.d(TAG, "onCreate: Activity được tạo.")

        setContent {
            var dark by rememberSaveable { mutableStateOf(false) }

            DiceRollerTheme(darkTheme = dark) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp(
                        dark = dark,
                        onToggleDark = { isDark ->
                            // LOG: Ghi lại sự kiện thay đổi theme
                            Log.d(TAG, "onToggleDark: Chuyển sang Dark Mode = $isDark")
                            dark = isDark
                            AppCompatDelegate.setDefaultNightMode(
                                if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                                else AppCompatDelegate.MODE_NIGHT_NO
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerAppPreview() {
    DiceRollerTheme(darkTheme = true) {
        DiceRollerApp(dark = true, onToggleDark = {})
    }
}

@Composable
fun DiceRollerApp(
    dark: Boolean,
    onToggleDark: (Boolean) -> Unit
) {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        dark = dark,
        onToggleDark = onToggleDark
    )
}

@Composable
fun DiceWithButtonAndImage(
    modifier: Modifier = Modifier,
    dark: Boolean,
    onToggleDark: (Boolean) -> Unit
) {
    // LOG: Quan sát khi Composable này được vẽ lại (recomposed)
    // Bạn sẽ thấy log này xuất hiện mỗi khi trạng thái (result, rotationTarget) thay đổi.
    Log.d(TAG, "DiceWithButtonAndImage: Composable được vẽ lại.")

    var result by rememberSaveable { mutableStateOf(1) }
    var rotationTarget by remember { mutableStateOf(0f) }
    val rotationDeg by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "dice-rotation"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemeToggleRow(dark = dark, onToggleDark = onToggleDark)
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = result,
            transitionSpec = {
                fadeIn(tween(150)) togetherWith fadeOut(tween(150))
            },
            label = "dice-crossfade"
        ) { value ->
            Image(
                painter = painterResource(
                    when (value) {
                        1 -> R.drawable.dice_1
                        2 -> R.drawable.dice_2
                        3 -> R.drawable.dice_3
                        4 -> R.drawable.dice_4
                        5 -> R.drawable.dice_5
                        else -> R.drawable.dice_6
                    }
                ),
                contentDescription = value.toString(),
                modifier = Modifier.rotate(rotationDeg)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val haptics = LocalHapticFeedback.current
        Button(onClick = {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            result = (1..6).random()
            rotationTarget += 360f

            // LOG: Ghi lại kết quả mới mỗi khi tung xúc xắc
            Log.d(TAG, "Button onClick: Tung xúc xắc! Kết quả mới là $result")
        }) {
            Text(text = stringResource(R.string.roll))
        }
    }
}

@Composable
fun ThemeToggleRow(
    dark: Boolean,
    onToggleDark: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Dark Mode")
        Spacer(Modifier.width(8.dp))
        Switch(
            checked = dark,
            onCheckedChange = onToggleDark
        )
    }
}