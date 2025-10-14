package com.example.inventory.data // Thay bằng package của bạn nếu khác

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) để tương tác với bảng "items" trong cơ sở dữ liệu.
 */
@Dao
interface ItemDao {

    /**
     * Chèn một mặt hàng vào bảng. Nếu có xung đột (ID đã tồn tại),
     * mục mới sẽ bị bỏ qua.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    /**
     * Cập nhật một mặt hàng đã có trong bảng.
     */
    @Update
    suspend fun update(item: Item)

    /**
     * Xóa một mặt hàng khỏi bảng.
     */
    @Delete
    suspend fun delete(item: Item)

    /**
     * Lấy một mặt hàng cụ thể từ bảng bằng ID của nó.
     * Trả về một Flow để giao diện có thể tự động cập nhật khi dữ liệu thay đổi.
     */
    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    /**
     * Lấy tất cả các mặt hàng từ bảng, sắp xếp theo tên tăng dần.
     * Trả về một Flow để giao diện có thể tự động cập nhật khi danh sách thay đổi.
     */
    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
}