package com.example.unit5_pathway2_bookshelf // Thay bằng package của bạn

import android.app.Application
import com.example.unit5_pathway2_bookshelf.data.AppContainer
import com.example.unit5_pathway2_bookshelf.data.DefaultAppContainer

class BookshelfApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}