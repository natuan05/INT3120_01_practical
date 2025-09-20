package com.example.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstapp.ui.theme.FirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    BusinessCardScreen()
                }
            }
        }
    }
}

@Composable
fun BusinessCardScreen() {
    // Màu xanh ngọc (mint). Bạn có thể đổi mã tùy ý.
    val mint = Color(0xFFDCEFE5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mint)
    ) {
        // Profile ở chính giữa
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileSection(
                name = "Nguyen Anh Tuan",
                title = "Android Developer Extraordinaire"
            )
        }

        // Contact ở góc dưới-trái
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp)
        ) {
            ContactSection(
                phone = "+84 868 185 758",
                email = "nguyenanhtuan070305@gmail.com",
                social = "@AndroidDev"
            )
        }
    }
}


@Composable
fun ProfileSection(
    name: String,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        LogoBadge()
        Spacer(Modifier.height(16.dp))
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
@Composable
fun LogoBadge() {
    Box(
        modifier = Modifier
            .size(120.dp) // kích thước khung vuông
            .clip(RoundedCornerShape(8.dp)) // bo góc nhẹ
            .background(Color.Black),       // nền đen
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.android_logo), // ảnh PNG/SVG nền trong suốt
            contentDescription = null,
            modifier = Modifier.size(72.dp),   // kích thước logo bên trong
            contentScale = ContentScale.Fit
        )
    }
}
@Composable
fun ContactSection(
    phone: String,
    email: String,
    social: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContactRow(icon = {
            Icon(imageVector = Icons.Filled.Phone, contentDescription = "Phone", tint = Color(0xFF3DDC84))
        }, text = phone)

        ContactRow(icon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email", tint = Color(0xFF3DDC84))
        }, text = email)

        ContactRow(icon = {
            Icon(imageVector = Icons.Filled.Person, contentDescription = "Social", tint = Color(0xFF3DDC84))
        }, text = social)
    }
}

@Composable
fun ContactRow(
    icon: @Composable () -> Unit,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        icon()
        Text(text = text, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBusinessCard() {
    FirstAppTheme {
        BusinessCardScreen()
    }
}
