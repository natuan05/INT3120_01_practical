package com.example.unit5_pathway2_bookshelf.data // Thay bằng package của bạn

import com.example.unit5_pathway2_bookshelf.network.BooksApi
import com.example.unit5_pathway2_bookshelf.network.BooksApiService

/**
 * Interface định nghĩa "hợp đồng" cho lớp dữ liệu.
 */
interface BooksRepository {
    /**
     * Lấy danh sách URL hình ảnh sách từ một truy vấn tìm kiếm.
     */
    suspend fun getBookPhotos(query: String): List<String>
}

/**
 * Lớp triển khai, lấy dữ liệu từ nguồn mạng.
 */
class NetworkBooksRepository(
    private val booksApiService: BooksApiService
) : BooksRepository {
    /**
     * Lấy danh sách ID sách từ API tìm kiếm, sau đó dùng từng ID để
     * lấy URL hình ảnh và trả về một danh sách các URL.
     */
    override suspend fun getBookPhotos(query: String): List<String> {
        val photoUrls = mutableListOf<String>()
        // Bước 1: Tìm kiếm để lấy danh sách ID sách
        val searchResult = booksApiService.searchBooks(query)

        searchResult.items?.forEach { item ->
            // Bước 2: Với mỗi ID, gọi API để lấy thông tin chi tiết
            val bookInfo = booksApiService.getBookInfo(item.id)
            // Lấy URL hình ảnh và thay thế http bằng https
            bookInfo.volumeInfo?.imageLinks?.thumbnail?.let {
                photoUrls.add(it.replace("http", "https"))
            }
        }
        return photoUrls
    }
}