package com.example.unit5_pathway2_amphibians.data

import com.example.unit5_pathway2_amphibians.network.AmphibiansApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Interface định nghĩa các phần phụ thuộc sẽ được cung cấp.
 */
interface AppContainer {
    val amphibiansRepository: AmphibiansRepository
}

/**
 * Lớp triển khai mặc định, cung cấp các phần phụ thuộc thật sự cho ứng dụng.
 */
class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://android-kotlin-fun-mars-server.appspot.com"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: AmphibiansApiService by lazy {
        retrofit.create(AmphibiansApiService::class.java)
    }

    // Cung cấp một thực thể của NetworkAmphibiansRepository
    override val amphibiansRepository: AmphibiansRepository by lazy {
        NetworkAmphibiansRepository(retrofitService)
    }
}