package com.example.busschedule.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) để tương tác với bảng "schedule".
 */
@Dao
interface ScheduleDao {
    /**
     * Lấy tất cả các lịch trình từ bảng, sắp xếp theo thời gian đến tăng dần.
     * Trả về một Flow để giao diện có thể tự động cập nhật.
     */
    @Query("SELECT * FROM schedule ORDER BY arrival_time ASC")
    fun getAll(): Flow<List<Schedule>>

    /**
     * Lấy một lịch trình cụ thể dựa trên tên điểm dừng (stopName).
     * Trả về một Flow để giao diện tự động cập nhật.
     */
    @Query("SELECT * FROM schedule WHERE stop_name = :stopName")
    fun getByStopName(stopName: String): Flow<List<Schedule>>
}