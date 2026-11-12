package com.slavov.bookshelf.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        private const val DATABASE_NAME = "bookshelf.db"

        fun create(context: Context): BookDatabase =
            Room.databaseBuilder(context.applicationContext, BookDatabase::class.java, DATABASE_NAME).build()
    }
}
