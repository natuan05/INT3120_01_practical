package com.example.marsphotos.fake

import com.example.marsphotos.data.NetworkMarsPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkMarsRepositoryTest {

    @Test
    fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList() =
        // Sử dụng runTest để tạo một coroutine scope cho bài kiểm thử
        runTest {
            // 1. Tạo một thực thể của Repository với dịch vụ mạng giả
            val repository = NetworkMarsPhotosRepository(
                marsApiService = FakeMarsApiService()
            )

            // 2. Gọi hàm suspend và xác nhận kết quả
            assertEquals(FakeDataSource.photosList, repository.getMarsPhotos())
        }
}