package com.example.unit5_pathway2_amphibians
import android.app.Application
import com.example.unit5_pathway2_amphibians.data.AppContainer
import com.example.unit5_pathway2_amphibians.data.DefaultAppContainer

class AmphibiansApplication : Application() {
    /** AppContainer được sử dụng bởi các lớp khác để lấy phần phụ thuộc */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}