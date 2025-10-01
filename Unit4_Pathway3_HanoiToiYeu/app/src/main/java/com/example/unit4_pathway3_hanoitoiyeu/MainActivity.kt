package com.example.unit4_pathway3_hanoitoiyeu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.unit4_pathway3_hanoitoiyeu.ui.MyCityApp
import com.example.unit4_pathway3_hanoitoiyeu.ui.theme.Unit4_Pathway3_HanoiToiYeuTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Unit4_Pathway3_HanoiToiYeuTheme {
                Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val windowSize = calculateWindowSizeClass(this)
                MyCityApp(windowSize = windowSize.widthSizeClass)
            }
            }
        }
    }
}