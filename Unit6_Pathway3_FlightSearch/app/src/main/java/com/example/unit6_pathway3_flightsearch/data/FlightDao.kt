package com.example.unit6_pathway3_flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) để tương tác với cơ sở dữ liệu chuyến bay.
 */
@Dao
interface FlightDao {
    /**
     * Lấy danh sách các sân bay có tên hoặc mã IATA khớp với truy vấn.
     */
    @Query(
        """
        SELECT * FROM airport 
        WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%' 
        ORDER BY passengers DESC
        """
    )
    fun getAirportsByQuery(query: String): Flow<List<Airport>>

    /**
     * Lấy thông tin một sân bay cụ thể bằng mã IATA.
     */
    @Query("SELECT * FROM airport WHERE iata_code = :code")
    fun getAirportByCode(code: String): Flow<Airport>

    /**
     * Lấy tất cả các chặng bay yêu thích.
     */
    @Query("SELECT * FROM favorite ORDER BY id DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    /**
     * Lấy tất cả các sân bay TRỪ sân bay có mã IATA được cung cấp.
     * Dùng để tìm các điểm đến khả thi.
     */
    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getPossibleDestinations(departureCode: String): Flow<List<Airport>>

    /**
     * Thêm một chặng bay vào danh sách yêu thích.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(flight: Favorite)

    /**
     * Xóa một chặng bay khỏi danh sách yêu thích.
     */
    @Delete
    suspend fun removeFavorite(flight: Favorite)

    @Query("SELECT * FROM airport WHERE iata_code = :code")
    suspend fun getAirportByCodeSuspend(code: String): Airport?
}