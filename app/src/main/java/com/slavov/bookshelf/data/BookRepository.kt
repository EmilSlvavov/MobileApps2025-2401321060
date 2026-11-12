package com.slavov.bookshelf.data

import com.slavov.bookshelf.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun observeBooks(): Flow<List<Book>>

    suspend fun getBook(id: Long): Book?

    suspend fun addBook(book: Book): Long

    suspend fun updateBook(book: Book)

    suspend fun deleteBook(id: Long)
}
