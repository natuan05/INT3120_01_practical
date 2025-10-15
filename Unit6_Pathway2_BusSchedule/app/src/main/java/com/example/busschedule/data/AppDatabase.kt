package com.example.busschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Lớp cơ sở dữ liệu với một đối tượng Instance kiểu singleton.
 */
@Database(entities = [Schedule::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Nếu Instance không null, trả về nó.
            // Nếu null, thì tạo cơ sở dữ liệu.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    /**
                     * Tạo và điền sẵn dữ liệu cho cơ sở dữ liệu từ một tệp
                     * trong thư mục assets.
                     */
                    .createFromAsset("database/bus_schedule.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}