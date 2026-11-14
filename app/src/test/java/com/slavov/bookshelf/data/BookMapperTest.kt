package com.slavov.bookshelf.data

import com.slavov.bookshelf.data.local.BookEntity
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class BookMapperTest {
    private val entity = BookEntity(
        id = 12,
        title = "Neuromancer",
        author = "William Gibson",
        totalPages = 271,
        pagesRead = 90,
        status = ReadingStatus.READING,
    )

    private val book = Book(
        id = 12,
        title = "Neuromancer",
        author = "William Gibson",
        totalPages = 271,
        pagesRead = 90,
        status = ReadingStatus.READING,
    )

    @Test
    fun `entity maps to domain book`() {
        assertEquals(book, entity.toBook())
    }

    @Test
    fun `domain book maps to entity`() {
        assertEquals(entity, book.toEntity())
    }
}
