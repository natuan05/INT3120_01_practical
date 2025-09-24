package com.example.artspaceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspaceapp.ui.theme.ArtSpaceAppTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale

// ===== Data =====
data class Artwork(
    val imageRes: Int,
    val title: String,
    val author: String,
    val year: String
)

val artworks = listOf(
    Artwork(R.drawable.starrynight, "The Starry Night", "Vincent van Gogh", "1889"),
    Artwork(R.drawable.thescream, "The Scream", "Edvard Munch", "1893"),
    Artwork(R.drawable.monalisa, "Mona Lisa", "Leonardo da Vinci", "1503")
)

// ===== Activity =====
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtSpaceAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ArtSpaceScreen()
                }
            }
        }
    }
}

// ===== Screens =====
@Composable
fun ArtSpaceScreen() {
    var index by remember { mutableStateOf(0) }
    val current = artworks[index]

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 800.dp

        if (isTablet) {
            ArtSpaceTablet(
                artwork = current,
                onPrevious = {
                    index = when (index) {
                        0 -> artworks.lastIndex
                        else -> index - 1
                    }
                },
                onNext = {
                    index = when (index) {
                        artworks.lastIndex -> 0
                        else -> index + 1
                    }
                }
            )
        } else {
            ArtSpaceCompact(
                artwork = current,
                onPrevious = {
                    index = when (index) {
                        0 -> artworks.lastIndex
                        else -> index - 1
                    }
                },
                onNext = {
                    index = when (index) {
                        artworks.lastIndex -> 0
                        else -> index + 1
                    }
                }
            )
        }
    }
}

@Composable
fun ArtSpaceCompact(
    artwork: Artwork,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ArtworkWall(artwork)             // ảnh vuông trên phone
        ArtworkInfoCard(artwork)
        ControlBar(onPrevious, onNext)
    }
}

@Composable
fun ArtSpaceTablet(
    artwork: Artwork,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Cụm nội dung giữa màn hình (giới hạn bề rộng để không quá to)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 600.dp)
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState()),   // ✅ nếu nội dung cao quá thì cuộn nhẹ
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ArtworkWall(
                artwork = artwork,
                modifier = Modifier.heightIn(max = 420.dp) // ✅ giới hạn chiều cao ảnh
            )
            ArtworkInfoCard(artwork)                       // ✅ luôn còn chỗ hiển thị
        }

        // Nút trái dưới
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .widthIn(max = 200.dp)
        ) {
            Button(
                onClick = onPrevious,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Previous") }
        }

        // Nút phải dưới
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .widthIn(max = 200.dp)
        ) {
            Button(
                onClick = onNext,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Next") }
        }
    }
}

// ===== UI Pieces =====
@Composable
fun ControlBar(
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onPrevious,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.width(140.dp)
        ) { Text("Previous") }

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.width(140.dp)
        ) { Text("Next") }
    }
}

@Composable
fun ArtworkWall(
    artwork: Artwork,
    modifier: Modifier = Modifier            // ✅ thêm
) {
    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier                   // ✅ dùng modifier truyền vào
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = artwork.imageRes),
                contentDescription = artwork.title,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Composable
fun ArtworkInfoCard(artwork: Artwork) {
    Surface(
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = artwork.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = artwork.author,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "  (${artwork.year})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ===== Previews =====
@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun PreviewPhonePortrait() {
    ArtSpaceAppTheme { ArtSpaceScreen() }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun PreviewTabletLandscape() {
    ArtSpaceAppTheme { ArtSpaceScreen() }
}
