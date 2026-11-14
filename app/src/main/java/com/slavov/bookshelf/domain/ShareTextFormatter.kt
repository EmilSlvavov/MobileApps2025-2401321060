package com.slavov.bookshelf.domain

import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus

data class ShareTemplates(
    val toRead: String,
    val reading: String,
    val finished: String,
)

class ShareTextFormatter(private val templates: ShareTemplates) {
    fun format(book: Book): String = when (book.status) {
        ReadingStatus.TO_READ -> templates.toRead.format(book.title, book.author)
        ReadingStatus.READING -> templates.reading.format(book.title, book.author, book.progressPercent)
        ReadingStatus.FINISHED -> templates.finished.format(book.title, book.author)
    }
}
