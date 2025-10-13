package com.example.marsphotos.data

import com.example.marsphotos.network.MarsPhoto

/**
 * Interface định nghĩa các phương thức để lấy dữ liệu ảnh sao Hỏa.
 */
interface MarsPhotosRepository {
    /** Lấy danh sách ảnh sao Hỏa từ nguồn dữ liệu. */
    suspend fun getMarsPhotos(): List<MarsPhoto>
}