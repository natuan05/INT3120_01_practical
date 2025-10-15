package com.example.busschedule.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class đại diện cho một bảng tên là "schedule" trong cơ sở dữ liệu.
 */
@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey
    val id: Int,

    @NonNull
    @ColumnInfo(name = "stop_name")
    val stopName: String,

    @NonNull
    @ColumnInfo(name = "arrival_time")
    val arrivalTime: Int
)