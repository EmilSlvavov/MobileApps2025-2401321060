package com.slavov.bookshelf.domain

import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class BookDraftTest {
    private val draft = BookDraft(
        title = "  Solaris ",
        author = " Stanislaw Lem",
        totalPages = " 204 ",
        pagesRead = "60",
        status = ReadingStatus.READING,
    )

    @Test
    fun `text fields are trimmed`() {
        val book = draft.toBook()

        assertEquals("Solaris", book.title)
        assertEquals("Stanislaw Lem", book.author)
        assertEquals(204, book.totalPages)
    }

    @Test
    fun `reading book keeps the entered pages`() {
        assertEquals(60, draft.toBook().pagesRead)
    }

    @Test
    fun `book to read starts from zero pages`() {
        val book = draft.copy(status = ReadingStatus.TO_READ).toBook()

        assertEquals(0, book.pagesRead)
    }

    @Test
    fun `finished book has all pages read`() {
        val book = draft.copy(status = ReadingStatus.FINISHED).toBook()

        assertEquals(204, book.pagesRead)
    }

    @Test
    fun `blank pages read counts as zero`() {
        assertEquals(0, draft.copy(pagesRead = "").toBook().pagesRead)
    }

    @Test
    fun `id is passed through when updating`() {
        assertEquals(7L, draft.toBook(id = 7).id)
    }

    @Test
    fun `book converts back to a draft`() {
        val book = Book(
            id = 3,
            title = "Solaris",
            author = "Stanislaw Lem",
            totalPages = 204,
            pagesRead = 60,
            status = ReadingStatus.READING,
        )

        val expected = BookDraft("Solaris", "Stanislaw Lem", "204", "60", ReadingStatus.READING)
        assertEquals(expected, book.toDraft())
    }
}
