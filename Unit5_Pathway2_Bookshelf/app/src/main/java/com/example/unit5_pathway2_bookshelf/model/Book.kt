package com.example.unit5_pathway2_bookshelf.model

import kotlinx.serialization.Serializable


@Serializable
data class BookInfo(
    val volumeInfo: VolumeInfo? = null
)

/**
 * Data class này đại diện cho một mục trong kết quả tìm kiếm.
 * Nó chỉ chứa ID của cuốn sách.
 */
@Serializable
data class BookSearchResult(
    val items: List<BookItem>? = null
)

@Serializable
data class BookItem(
    val id: String
)

/**
 * Data class này đại diện cho thông tin chi tiết của một cuốn sách,
 * đặc biệt là các link ảnh.
 */
@Serializable
data class VolumeInfo(
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val thumbnail: String? = null
)