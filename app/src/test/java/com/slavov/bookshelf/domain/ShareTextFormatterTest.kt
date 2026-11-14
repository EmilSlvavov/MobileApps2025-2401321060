package com.slavov.bookshelf.domain

import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class ShareTextFormatterTest {
    private val formatter = ShareTextFormatter(
        ShareTemplates(
            toRead = "Want: %1\$s by %2\$s",
            reading = "Reading: %1\$s by %2\$s (%3\$d%%)",
            finished = "Done: %1\$s by %2\$s",
        ),
    )

    private val book = Book(title = "Emma", author = "Jane Austen", totalPages = 400, pagesRead = 100)

    @Test
    fun `book to read uses the to read template`() {
        assertEquals("Want: Emma by Jane Austen", formatter.format(book))
    }

    @Test
    fun `book in progress includes the percentage`() {
        val reading = book.copy(status = ReadingStatus.READING)

        assertEquals("Reading: Emma by Jane Austen (25%)", formatter.format(reading))
    }

    @Test
    fun `finished book uses the finished template`() {
        val finished = book.copy(status = ReadingStatus.FINISHED)

        assertEquals("Done: Emma by Jane Austen", formatter.format(finished))
    }
}
