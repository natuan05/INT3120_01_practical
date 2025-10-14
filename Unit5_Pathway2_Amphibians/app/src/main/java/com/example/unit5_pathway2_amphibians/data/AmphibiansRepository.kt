package com.example.unit5_pathway2_amphibians.data

import com.example.unit5_pathway2_amphibians.model.Amphibian
import com.example.unit5_pathway2_amphibians.network.AmphibiansApiService

/**
 * Interface định nghĩa "hợp đồng" cho lớp dữ liệu.
 */
interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

/**
 * Lớp triển khai, lấy dữ liệu từ nguồn mạng.
 */
class NetworkAmphibiansRepository(
    private val amphibiansApiService: AmphibiansApiService
) : AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibian> {
        return amphibiansApiService.getAmphibians()
    }
}