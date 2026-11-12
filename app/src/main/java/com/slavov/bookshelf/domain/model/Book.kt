package com.slavov.bookshelf.domain.model

data class Book(
    val id: Long = 0,
    val title: String,
    val author: String,
    val totalPages: Int,
    val pagesRead: Int = 0,
    val status: ReadingStatus = ReadingStatus.TO_READ,
) {
    val progressPercent: Int
        get() = if (totalPages <= 0) 0 else (pagesRead * 100 / totalPages).coerceIn(0, 100)

    val pagesLeft: Int
        get() = (totalPages - pagesRead).coerceAtLeast(0)
}
