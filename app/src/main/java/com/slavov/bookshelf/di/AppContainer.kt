package com.slavov.bookshelf.di

import android.content.Context
import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.data.RoomBookRepository
import com.slavov.bookshelf.data.local.BookDatabase

class AppContainer(context: Context) {
    private val database: BookDatabase by lazy { BookDatabase.create(context) }

    val bookRepository: BookRepository by lazy { RoomBookRepository(database.bookDao()) }
}
