package com.slavov.bookshelf.data

import com.slavov.bookshelf.data.local.BookDao
import com.slavov.bookshelf.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomBookRepository(private val bookDao: BookDao) : BookRepository {
    override fun observeBooks(): Flow<List<Book>> =
        bookDao.observeAll().map { entities -> entities.map { it.toBook() } }

    override suspend fun getBook(id: Long): Book? = bookDao.findById(id)?.toBook()

    override suspend fun addBook(book: Book): Long = bookDao.insert(book.toEntity())

    override suspend fun updateBook(book: Book) = bookDao.update(book.toEntity())

    override suspend fun deleteBook(id: Long) = bookDao.deleteById(id)
}
