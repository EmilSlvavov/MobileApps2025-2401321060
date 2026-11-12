package com.slavov.bookshelf.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.slavov.bookshelf.domain.model.ReadingStatus

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    @ColumnInfo(name = "total_pages") val totalPages: Int,
    @ColumnInfo(name = "pages_read") val pagesRead: Int,
    val status: ReadingStatus,
)
