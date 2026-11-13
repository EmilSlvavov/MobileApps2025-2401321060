package com.slavov.bookshelf.domain

enum class BookField {
    TITLE,
    AUTHOR,
    TOTAL_PAGES,
    PAGES_READ,
}

enum class FieldError {
    REQUIRED,
    TOO_LONG,
    NOT_A_NUMBER,
    MUST_BE_POSITIVE,
    EXCEEDS_TOTAL,
}

object BookValidator {
    const val MAX_TEXT_LENGTH = 120

    fun validate(draft: BookDraft): Map<BookField, FieldError> {
        val errors = mutableMapOf<BookField, FieldError>()
        textError(draft.title)?.let { errors[BookField.TITLE] = it }
        textError(draft.author)?.let { errors[BookField.AUTHOR] = it }

        val total = draft.totalPages.trim().toIntOrNull()
        totalPagesError(draft.totalPages, total)?.let { errors[BookField.TOTAL_PAGES] = it }
        pagesReadError(draft.pagesRead, total)?.let { errors[BookField.PAGES_READ] = it }
        return errors
    }

    private fun textError(value: String): FieldError? = when {
        value.isBlank() -> FieldError.REQUIRED
        value.trim().length > MAX_TEXT_LENGTH -> FieldError.TOO_LONG
        else -> null
    }

    private fun totalPagesError(raw: String, total: Int?): FieldError? = when {
        raw.isBlank() -> FieldError.REQUIRED
        total == null -> FieldError.NOT_A_NUMBER
        total <= 0 -> FieldError.MUST_BE_POSITIVE
        else -> null
    }

    private fun pagesReadError(raw: String, total: Int?): FieldError? {
        if (raw.isBlank()) return null
        val read = raw.trim().toIntOrNull()
        return when {
            read == null || read < 0 -> FieldError.NOT_A_NUMBER
            total != null && read > total -> FieldError.EXCEEDS_TOTAL
            else -> null
        }
    }
}
