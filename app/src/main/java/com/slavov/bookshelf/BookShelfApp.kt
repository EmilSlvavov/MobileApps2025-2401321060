package com.slavov.bookshelf

import android.app.Application
import com.slavov.bookshelf.di.AppContainer

class BookShelfApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
