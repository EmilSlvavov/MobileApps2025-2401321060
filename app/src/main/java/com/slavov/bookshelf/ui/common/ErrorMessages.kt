package com.slavov.bookshelf.ui.common

import androidx.annotation.StringRes
import com.slavov.bookshelf.R
import com.slavov.bookshelf.domain.FieldError

@get:StringRes
val FieldError.messageRes: Int
    get() = when (this) {
        FieldError.REQUIRED -> R.string.error_required
        FieldError.TOO_LONG -> R.string.error_too_long
        FieldError.NOT_A_NUMBER -> R.string.error_not_a_number
        FieldError.MUST_BE_POSITIVE -> R.string.error_must_be_positive
        FieldError.EXCEEDS_TOTAL -> R.string.error_exceeds_total
    }
