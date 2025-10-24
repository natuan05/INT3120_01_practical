package com.example.busschedule.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.busschedule.BusScheduleApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer cho BusScheduleViewModel
        initializer {
            BusScheduleViewModel(
                busScheduleApplication().database.scheduleDao()
            )
        }
    }
}

/**
 * Hàm mở rộng để cung cấp tham chiếu đến Application instance.
 */
fun CreationExtras.busScheduleApplication(): BusScheduleApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BusScheduleApplication)