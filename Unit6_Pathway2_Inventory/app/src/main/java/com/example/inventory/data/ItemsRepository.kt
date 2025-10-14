package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository cung cấp các hàm chèn, cập nhật, xóa, và lấy [Item] từ một nguồn dữ liệu.
 */
interface ItemsRepository {
    /**
     * Lấy tất cả các mặt hàng dưới dạng một luồng (stream).
     */
    fun getAllItemsStream(): Flow<List<Item>>

    /**
     * Lấy một mặt hàng cụ thể bằng [id] dưới dạng một luồng.
     */
    fun getItemStream(id: Int): Flow<Item?>

    /**
     * Chèn một mặt hàng vào nguồn dữ liệu.
     */
    suspend fun insertItem(item: Item)

    /**
     * Xóa một mặt hàng khỏi nguồn dữ liệu.
     */
    suspend fun deleteItem(item: Item)

    /**
     * Cập nhật một mặt hàng trong nguồn dữ liệu.
     */
    suspend fun updateItem(item: Item)
}