package com.example.unit3_pathway3_30days.model // Thay bằng package của bạn

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class DayTip(
    val day: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)