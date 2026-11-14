package com.slavov.bookshelf.ui.common

import android.content.Context
import android.content.Intent
import com.slavov.bookshelf.R
import com.slavov.bookshelf.domain.ShareTemplates
import com.slavov.bookshelf.domain.ShareTextFormatter
import com.slavov.bookshelf.domain.model.Book

fun Context.shareBook(book: Book) {
    val templates = ShareTemplates(
        toRead = getString(R.string.share_to_read),
        reading = getString(R.string.share_reading),
        finished = getString(R.string.share_finished),
    )
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, ShareTextFormatter(templates).format(book))
    }
    startActivity(Intent.createChooser(sendIntent, getString(R.string.share_title)))
}
