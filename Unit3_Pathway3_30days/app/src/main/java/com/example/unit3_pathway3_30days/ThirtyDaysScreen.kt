package com.example.unit3_pathway3_30days

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unit3_pathway3_30days.model.DayTip
import com.example.unit3_pathway3_30days.model.TipsRepository
import com.example.unit3_pathway3_30days.ui.theme.Unit3_Pathway3_30daysTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel


// Sửa lại DayTipCard: Không còn tự quản lý state `expanded` nữa
@Composable
fun DayTipCard(
    tip: DayTip,
    onClick: () -> Unit, // Nhận vào một sự kiện onClick
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .clickable(onClick = onClick) // Gọi sự kiện onClick
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Ngày ${tip.day}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = tip.titleRes),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Image(
                painter = painterResource(id = tip.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )

            // Đọc trạng thái `expanded` trực tiếp từ đối tượng `tip`
            if (tip.isExpanded) {
                Text(
                    text = stringResource(id = tip.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// Sửa lại ThirtyDaysList: Nhận vào danh sách và sự kiện
@Composable
fun ThirtyDaysList(
    tips: List<DayTip>,
    onCardClick: (Int) -> Unit, // Nhận vào sự kiện với tham số là day number
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tips) { tip ->
            DayTipCard(
                tip = tip,
                onClick = { onCardClick(tip.day) }, // Truyền sự kiện xuống
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

// Sửa lại ThirtyDaysApp: Khởi tạo ViewModel và kết nối tất cả
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirtyDaysApp(
    viewModel: ThirtyDaysViewModel = viewModel() // Khởi tạo ViewModel
) {
    // Lắng nghe sự thay đổi trạng thái từ ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        ThirtyDaysList(
            tips = uiState.tips, // Lấy danh sách từ uiState
            onCardClick = viewModel::toggleExpansion, // Gửi sự kiện tới ViewModel
            contentPadding = paddingValues
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayTipCardPreview() {
    Unit3_Pathway3_30daysTheme {
        DayTipCard(
            tip = TipsRepository.tips[0].copy(isExpanded = false), // Preview với trạng thái không mở rộng
            onClick = {}
        )
    }
}
