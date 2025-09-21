package com.example.diceroller

import android.os.Bundle
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Hoist trạng thái dark mode lên đây và truyền vào Theme
            var dark by rememberSaveable { mutableStateOf(false) }

            DiceRollerTheme(darkTheme = dark) {
                // Sơn nền theo theme (rất quan trọng để thấy nền đổi màu)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp(
                        dark = dark,
                        onToggleDark = { isDark ->
                            dark = isDark
                            // (tuỳ chọn) đồng bộ AppCompat cho toàn app
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
    var result by rememberSaveable { mutableStateOf(1) }

    // Điều khiển góc quay mỗi lần bấm (mỗi lần +360 độ)
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
        // Toggle Dark Mode (điều khiển trực tiếp theme Compose)
        ThemeToggleRow(dark = dark, onToggleDark = onToggleDark)

        Spacer(modifier = Modifier.height(16.dp))

        // Chuyển ảnh mượt khi result đổi
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
