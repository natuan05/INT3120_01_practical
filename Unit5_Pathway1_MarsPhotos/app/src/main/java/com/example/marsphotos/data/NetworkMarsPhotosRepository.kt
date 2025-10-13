package com.example.marsphotos.data

import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.network.MarsApiService

/**
 * Lớp này triển khai interface MarsPhotosRepository để lấy dữ liệu từ mạng.
 */
class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService // <-- Nhận vào qua constructor
) : MarsPhotosRepository {
    override suspend fun getMarsPhotos(): List<MarsPhoto> {
        return marsApiService.getPhotos() // <-- Dùng đối tượng được truyền vào
    }
}