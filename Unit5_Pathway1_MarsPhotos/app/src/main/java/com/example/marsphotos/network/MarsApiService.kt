// In network/MarsApiService.kt

package com.example.marsphotos.network

import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
// URL cơ sở của web service
private const val BASE_URL =
    "https://android-kotlin-fun-mars-server.appspot.com"

// Xây dựng đối tượng Retrofit
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

// Interface định nghĩa cách Retrofit giao tiếp với server
// In network/MarsApiService.kt
interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

// Đối tượng Singleton để khởi tạo dịch vụ Retrofit
object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}