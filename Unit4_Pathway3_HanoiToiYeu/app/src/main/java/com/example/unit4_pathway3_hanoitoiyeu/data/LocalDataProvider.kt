package com.example.unit4_pathway3_hanoitoiyeu.data

import com.example.unit4_pathway3_hanoitoiyeu.R

object LocalDataProvider {
    val categories = listOf(
        Category(
            titleResId = R.string.category_coffee,
            places = listOf(
                Place(R.string.coffee_phe_la_name, R.string.coffee_phe_la_desc, R.drawable.coffee_phe_la),
                Place(R.string.coffee_ban_cong_name, R.string.coffee_ban_cong_desc, R.drawable.coffee_ban_cong),
                Place(R.string.coffee_cong_cafe_name, R.string.coffee_cong_cafe_desc, R.drawable.coffee_cong_cafe)
            )
        ),
        Category(
            titleResId = R.string.category_parks,
            places = listOf(
                Place(R.string.park_yen_so_name, R.string.park_yen_so_desc, R.drawable.park_yen_so),
                Place(R.string.park_thu_le_name, R.string.park_thu_le_desc, R.drawable.park_thu_le),
                Place(R.string.park_thong_nhat_name, R.string.park_thong_nhat_desc, R.drawable.park_thong_nhat)
            )
        ),
        Category(
            titleResId = R.string.category_malls,
            places = listOf(
                Place(R.string.mall_times_city_name, R.string.mall_times_city_desc, R.drawable.mall_times_city),
                Place(R.string.mall_aeon_long_bien_name, R.string.mall_aeon_long_bien_desc, R.drawable.mall_aeon_long_bien),
                Place(R.string.mall_trang_tien_name, R.string.mall_trang_tien_desc, R.drawable.mall_trang_tien)
            )
        ),
        Category(
            titleResId = R.string.category_bun_dau,
            places = listOf(
                Place(R.string.bundau_trung_huong_name, R.string.bundau_trung_huong_desc, R.drawable.bundau_trung_huong),
                Place(R.string.bundau_hai_anh_name, R.string.bundau_hai_anh_desc, R.drawable.bundau_hai_anh),
                Place(R.string.bundau_dong_thai_name, R.string.bundau_dong_thai_desc, R.drawable.bundau_dong_thai)
            )
        )
    )
}