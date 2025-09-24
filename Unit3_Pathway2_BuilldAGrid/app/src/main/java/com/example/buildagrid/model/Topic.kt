package com.example.buildagrid.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val title: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)
