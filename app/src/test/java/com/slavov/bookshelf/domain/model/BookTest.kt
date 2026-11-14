package com.slavov.bookshelf.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BookTest {
    private fun book(totalPages: Int, pagesRead: Int) =
        Book(title = "Dune", author = "Frank Herbert", totalPages = totalPages, pagesRead = pagesRead)

    @Test
    fun `progress is zero for a book that has not been started`() {
        assertEquals(0, book(totalPages = 400, pagesRead = 0).progressPercent)
    }

    @Test
    fun `progress is rounded down to a whole percent`() {
        assertEquals(33, book(totalPages = 300, pagesRead = 100).progressPercent)
    }

    @Test
    fun `progress never exceeds one hundred percent`() {
        assertEquals(100, book(totalPages = 200, pagesRead = 250).progressPercent)
    }

    @Test
    fun `progress is zero when total pages is not positive`() {
        assertEquals(0, book(totalPages = 0, pagesRead = 10).progressPercent)
    }

    @Test
    fun `pages left is the difference between total and read`() {
        assertEquals(150, book(totalPages = 400, pagesRead = 250).pagesLeft)
    }

    @Test
    fun `pages left is never negative`() {
        assertEquals(0, book(totalPages = 100, pagesRead = 120).pagesLeft)
    }
}
