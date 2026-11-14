package com.slavov.bookshelf.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BookValidatorTest {
    private val validDraft = BookDraft(
        title = "The Hobbit",
        author = "J. R. R. Tolkien",
        totalPages = "310",
        pagesRead = "42",
    )

    @Test
    fun `valid draft has no errors`() {
        assertTrue(BookValidator.validate(validDraft).isEmpty())
    }

    @Test
    fun `blank title is required`() {
        val errors = BookValidator.validate(validDraft.copy(title = "   "))

        assertEquals(FieldError.REQUIRED, errors[BookField.TITLE])
    }

    @Test
    fun `blank author is required`() {
        val errors = BookValidator.validate(validDraft.copy(author = ""))

        assertEquals(FieldError.REQUIRED, errors[BookField.AUTHOR])
    }

    @Test
    fun `title longer than the limit is rejected`() {
        val longTitle = "a".repeat(BookValidator.MAX_TEXT_LENGTH + 1)

        val errors = BookValidator.validate(validDraft.copy(title = longTitle))

        assertEquals(FieldError.TOO_LONG, errors[BookField.TITLE])
    }

    @Test
    fun `missing total pages is required`() {
        val errors = BookValidator.validate(validDraft.copy(totalPages = ""))

        assertEquals(FieldError.REQUIRED, errors[BookField.TOTAL_PAGES])
    }

    @Test
    fun `total pages must be a number`() {
        val errors = BookValidator.validate(validDraft.copy(totalPages = "many"))

        assertEquals(FieldError.NOT_A_NUMBER, errors[BookField.TOTAL_PAGES])
    }

    @Test
    fun `total pages must be positive`() {
        val errors = BookValidator.validate(validDraft.copy(totalPages = "0", pagesRead = ""))

        assertEquals(FieldError.MUST_BE_POSITIVE, errors[BookField.TOTAL_PAGES])
    }

    @Test
    fun `blank pages read is allowed`() {
        val errors = BookValidator.validate(validDraft.copy(pagesRead = ""))

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `negative pages read is rejected`() {
        val errors = BookValidator.validate(validDraft.copy(pagesRead = "-5"))

        assertEquals(FieldError.NOT_A_NUMBER, errors[BookField.PAGES_READ])
    }

    @Test
    fun `pages read cannot exceed total pages`() {
        val errors = BookValidator.validate(validDraft.copy(pagesRead = "311"))

        assertEquals(FieldError.EXCEEDS_TOTAL, errors[BookField.PAGES_READ])
    }

    @Test
    fun `all invalid fields are reported together`() {
        val errors = BookValidator.validate(BookDraft())

        assertEquals(setOf(BookField.TITLE, BookField.AUTHOR, BookField.TOTAL_PAGES), errors.keys)
    }
}
