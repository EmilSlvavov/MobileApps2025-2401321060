package com.slavov.bookshelf.ui.common

import androidx.annotation.StringRes
import com.slavov.bookshelf.R
import com.slavov.bookshelf.domain.model.ReadingStatus

@get:StringRes
val ReadingStatus.labelRes: Int
    get() = when (this) {
        ReadingStatus.TO_READ -> R.string.status_to_read
        ReadingStatus.READING -> R.string.status_reading
        ReadingStatus.FINISHED -> R.string.status_finished
    }
