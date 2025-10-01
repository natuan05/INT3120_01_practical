package com.example.unit4_pathway3_hanoitoiyeu.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Category(
    @StringRes val titleResId: Int,
    val places: List<Place>
)

data class Place(
    @StringRes val nameResId: Int,
    @StringRes val descriptionResId: Int,
    @DrawableRes val imageResId: Int
)