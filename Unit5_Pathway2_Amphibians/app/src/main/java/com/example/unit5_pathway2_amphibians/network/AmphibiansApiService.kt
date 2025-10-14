package com.example.unit5_pathway2_amphibians.network

import com.example.unit5_pathway2_amphibians.model.Amphibian
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

/**
 * Interface định nghĩa cách Retrofit giao tiếp với máy chủ web.
 */
interface AmphibiansApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): List<Amphibian>
}
