package com.example.buildagrid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildagrid.model.Topic
import com.example.buildagrid.ui.theme.BuildAGridTheme

@Composable
fun CourseGrid(
    topics: List<Topic>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(topics) { topic ->
            TopicItem(topic = topic)
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
fun CourseGridPreview() {
    BuildAGridTheme {
        CourseGrid(topics = com.example.buildagrid.data.DataSource.topics)
    }
}
