package com.example.unit5_pathway2_bookshelf.data // Thay bằng package của bạn

import com.example.unit5_pathway2_bookshelf.network.BooksApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * Interface định nghĩa các phần phụ thuộc sẽ được cung cấp.
 */
interface AppContainer {
    val booksRepository: BooksRepository
}

/**
 * Lớp triển khai mặc định, cung cấp các phần phụ thuộc thật sự cho ứng dụng.
 */
class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }

    override val booksRepository: BooksRepository by lazy {
        NetworkBooksRepository(retrofitService)
    }
}