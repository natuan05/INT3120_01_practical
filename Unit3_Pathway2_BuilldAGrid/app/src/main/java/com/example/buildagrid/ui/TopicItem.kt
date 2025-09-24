package com.example.buildagrid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildagrid.R
import com.example.buildagrid.model.Topic
import com.example.buildagrid.ui.theme.BuildAGridTheme

@Composable
fun TopicItem(
    topic: Topic,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = topic.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(68.dp)            // ✅ ảnh 68x68
            )

            Spacer(Modifier.width(16.dp))   // ✅ khoảng cách 16dp

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(id = topic.title),
                    style = MaterialTheme.typography.bodyMedium   // ✅ bodyMedium
                )

                Spacer(Modifier.height(8.dp))  // ✅ khoảng cách 8dp

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))  // ✅ khoảng cách 8dp
                    Text(
                        text = topic.courseCount.toString(),
                        style = MaterialTheme.typography.labelMedium  // ✅ labelMedium
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopicItemPreview() {
    BuildAGridTheme {
        TopicItem(
            topic = Topic(R.string.architecture, 58, R.drawable.architecture),
            modifier = Modifier.width(180.dp)   // ép rộng giống 1 cell của grid
        )
    }
}