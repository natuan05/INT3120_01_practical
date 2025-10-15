package com.example.busschedule

import android.app.Application
import com.example.busschedule.data.AppDatabase

class BusScheduleApplication : Application() {
    // Khởi tạo cơ sở dữ liệu một cách lười biếng (lazy)
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}