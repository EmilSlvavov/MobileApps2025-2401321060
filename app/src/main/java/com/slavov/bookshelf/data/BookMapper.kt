package com.slavov.bookshelf.data

import com.slavov.bookshelf.data.local.BookEntity
import com.slavov.bookshelf.domain.model.Book

fun BookEntity.toBook(): Book = Book(
    id = id,
    title = title,
    author = author,
    totalPages = totalPages,
    pagesRead = pagesRead,
    status = status,
)

fun Book.toEntity(): BookEntity = BookEntity(
    id = id,
    title = title,
    author = author,
    totalPages = totalPages,
    pagesRead = pagesRead,
    status = status,
)
