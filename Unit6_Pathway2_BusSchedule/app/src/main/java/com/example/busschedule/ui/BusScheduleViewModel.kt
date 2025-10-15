package com.example.busschedule.ui // Thay bằng package của bạn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busschedule.data.Schedule
import com.example.busschedule.data.ScheduleDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class BusScheduleViewModel(private val scheduleDao: ScheduleDao) : ViewModel() {
    // Lấy tất cả lịch trình từ cơ sở dữ liệu
    val fullSchedule: StateFlow<List<Schedule>> =
        scheduleDao.getAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf()
        )

    // Lấy lịch trình cho một điểm dừng cụ thể
    fun getScheduleForStop(stopName: String): StateFlow<List<Schedule>> =
        scheduleDao.getByStopName(stopName).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf()
        )
}