package com.slavov.bookshelf.domain

import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus

data class BookDraft(
    val title: String = "",
    val author: String = "",
    val totalPages: String = "",
    val pagesRead: String = "",
    val status: ReadingStatus = ReadingStatus.TO_READ,
)

fun BookDraft.toBook(id: Long = 0): Book {
    val total = totalPages.trim().toInt()
    val read = pagesRead.trim().toIntOrNull() ?: 0
    return Book(
        id = id,
        title = title.trim(),
        author = author.trim(),
        totalPages = total,
        pagesRead = when (status) {
            ReadingStatus.TO_READ -> 0
            ReadingStatus.READING -> read
            ReadingStatus.FINISHED -> total
        },
        status = status,
    )
}
