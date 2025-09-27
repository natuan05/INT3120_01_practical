package com.example.unit3_pathway3_superheroes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.unit3_pathway3_superheroes.model.Hero
import com.example.unit3_pathway3_superheroes.model.HeroesRepository
import com.example.unit3_pathway3_superheroes.ui.theme.Unit3_Pathway3_SuperHeroesTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
@Composable
fun HeroListItem(
    hero: Hero,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
        shape = MaterialTheme.shapes.medium // Bán kính bo góc 16dp (thường là medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cột chứa tên và mô tả
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(hero.nameRes),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = stringResource(hero.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            // Khoảng cách giữa văn bản và hình ảnh
            Spacer(modifier = Modifier.width(16.dp))
            // Box chứa hình ảnh
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.small) // Bán kính bo góc 8dp (thường là small)
            ) {
                Image(
                    painter = painterResource(hero.imageRes),
                    contentDescription = null,
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}


@Composable
fun HeroesList(
    heroes: List<Hero>,
    modifier: Modifier = Modifier,
    // Thêm contentPadding để xử lý padding cho toàn danh sách
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách giữa các item
    ) {
        items(heroes) { hero ->
            HeroListItem(
                hero = hero,
                modifier = Modifier.padding(horizontal = 16.dp) // Padding trái phải cho mỗi item
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperheroesTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
        },
        modifier = modifier
    )
}


@Composable
fun SuperheroesApp() {
    Scaffold(
        topBar = {
            SuperheroesTopAppBar()
        }
    ) { it -> // 'it' chứa giá trị padding mà TopAppBar chiếm dụng
        HeroesList(
            heroes = HeroesRepository.heroes,
            contentPadding = it // Truyền padding này vào LazyColumn
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeroesPreview() {
    // Bạn cần tạo theme cho Superheroes (hoặc dùng theme mặc định)
    // Giả sử bạn đã có SuperheroesTheme
    Unit3_Pathway3_SuperHeroesTheme {

    }
}