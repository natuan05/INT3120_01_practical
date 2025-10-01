package com.example.unit4_pathway3_hanoitoiyeu.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unit4_pathway3_hanoitoiyeu.R
import com.example.unit4_pathway3_hanoitoiyeu.data.Category
import com.example.unit4_pathway3_hanoitoiyeu.data.Place
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.unit4_pathway3_hanoitoiyeu.ui.utils.MyCityContentType

@Composable
fun MyCityApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: MyCityViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val contentType = when (windowSize) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> MyCityContentType.ListOnly
        WindowWidthSizeClass.Expanded -> MyCityContentType.ListAndDetail
        else -> MyCityContentType.ListOnly
    }

    Scaffold(
        topBar = {
            MyCityAppBar(
                selectedCategory = uiState.selectedCategory,
                selectedPlace = uiState.selectedPlace,
                onNavigateUp = {
                    if (contentType == MyCityContentType.ListAndDetail) {
                        viewModel.navigateToCategories()
                    } else {
                        if (uiState.selectedPlace != null) {
                            viewModel.navigateToPlaces()
                        } else if (uiState.selectedCategory != null) {
                            viewModel.navigateToCategories()
                        }
                    }
                },
                canNavigateBack = uiState.selectedCategory != null
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (contentType == MyCityContentType.ListAndDetail) {
            MyCityListAndDetailContent(
                uiState = uiState,
                categories = viewModel.categories,
                onCategorySelect = { viewModel.selectCategory(it) },
                onPlaceSelect = { viewModel.selectPlace(it) },
                onNavigateToPlaces = { viewModel.navigateToPlaces() },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            uiState.selectedPlace?.let { selectedPlace ->
                PlaceDetailScreen(
                    place = selectedPlace,
                    modifier = Modifier.padding(innerPadding)
                )
                BackHandler { viewModel.navigateToPlaces() }
            } ?: uiState.selectedCategory?.let { selectedCategory ->
                PlaceScreen(
                    category = selectedCategory,
                    onPlaceClick = { viewModel.selectPlace(it) },
                    modifier = Modifier.padding(innerPadding)
                )
                BackHandler { viewModel.navigateToCategories() }
            } ?: CategoryScreen(
                categories = viewModel.categories,
                onCategoryClick = { viewModel.selectCategory(it) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

/**
 * Bố cục hai ngăn cho màn hình lớn
 */
@Composable
fun MyCityListAndDetailContent(
    uiState: MyCityUiState,
    categories: List<Category>,
    onCategorySelect: (Category) -> Unit,
    onPlaceSelect: (Place) -> Unit,
    onNavigateToPlaces: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        // Ngăn 1: Luôn hiển thị danh sách danh mục
        CategoryScreen(
            categories = categories,
            onCategoryClick = onCategorySelect,
            modifier = Modifier.weight(1f)
        )
        // Ngăn 2: Hiển thị danh sách địa điểm hoặc chi tiết
        Column(modifier = Modifier.weight(2f)) {
            // Chỉ hiển thị ngăn 2 nếu một danh mục đã được chọn
            uiState.selectedCategory?.let { selectedCategory ->
                uiState.selectedPlace?.let { selectedPlace ->
                    PlaceDetailScreen(place = selectedPlace)
                    BackHandler { onNavigateToPlaces() } // Quay về danh sách địa điểm
                } ?: PlaceScreen(
                    category = selectedCategory,
                    onPlaceClick = onPlaceSelect
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityAppBar(
    selectedCategory: Category?,
    selectedPlace: Place?,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
)  {
    // Xác định tiêu đề dựa trên trạng thái hiện tại
    val title = when {
        selectedPlace != null -> stringResource(id = selectedPlace.nameResId)
        selectedCategory != null -> stringResource(id = selectedCategory.titleResId)
        else -> stringResource(id = R.string.app_name)
    }

    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            // Chỉ hiển thị nút back khi canNavigateBack là true
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back" // Hoặc stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


// --- Các Composable đã tạo ở bước trước ---

@Composable
fun CategoryListItem(
    category: Category,
    onItemClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(category) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = category.titleResId),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun PlaceListItem(
    place: Place,
    onItemClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(place) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = place.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.small),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = place.nameResId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = place.descriptionResId),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CategoryScreen(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(categories) { category ->
            CategoryListItem(
                category = category,
                onItemClick = onCategoryClick
            )
        }
    }
}

@Composable
fun PlaceScreen(
    category: Category,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(category.places) { place ->
            PlaceListItem(
                place = place,
                onItemClick = onPlaceClick
            )
        }
    }
}

@Composable
fun PlaceDetailScreen(
    place: Place,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = place.imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = place.nameResId),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = place.descriptionResId),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}