package com.example.unit5_pathway2_bookshelf.network // Thay bằng package của bạn

import com.example.unit5_pathway2_bookshelf.model.BookInfo
import com.example.unit5_pathway2_bookshelf.model.BookSearchResult
import com.example.unit5_pathway2_bookshelf.model.VolumeInfo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"

/**
 * Cấu hình Json để bỏ qua các key không xác định, giúp ứng dụng không bị crash
 * nếu API trả về các trường mà data class của chúng ta không có.
 */
private val json = Json { ignoreUnknownKeys = true }

/**
 * Sử dụng Retrofit builder để tạo một đối tượng retrofit.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Interface định nghĩa cách Retrofit giao tiếp với máy chủ Google Books API.
 */
interface BooksApiService {
    /**
     * Lấy danh sách ID sách dựa trên một truy vấn tìm kiếm.
     */
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BookSearchResult

    /**
     * Lấy thông tin chi tiết (chủ yếu là link ảnh) của một cuốn sách cụ thể bằng ID.
     */
    @GET("volumes/{id}")
    suspend fun getBookInfo(@Path("id") id: String): BookInfo
}

/**
 * Một đối tượng công khai để khởi tạo API Service một cách lười biếng (lazy).
 */
object BooksApi {
    val retrofitService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }
}