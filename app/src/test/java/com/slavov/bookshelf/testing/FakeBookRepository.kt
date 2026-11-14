package com.slavov.bookshelf.testing

import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeBookRepository(initialBooks: List<Book> = emptyList()) : BookRepository {
    private val books = MutableStateFlow(initialBooks)
    private var nextId = (initialBooks.maxOfOrNull { it.id } ?: 0L) + 1

    val currentBooks: List<Book>
        get() = books.value

    override fun observeBooks(): Flow<List<Book>> = books

    override suspend fun getBook(id: Long): Book? = books.value.firstOrNull { it.id == id }

    override suspend fun addBook(book: Book): Long {
        val id = if (book.id == 0L) nextId++ else book.id
        books.update { it + book.copy(id = id) }
        return id
    }

    override suspend fun updateBook(book: Book) {
        books.update { current -> current.map { if (it.id == book.id) book else it } }
    }

    override suspend fun deleteBook(id: Long) {
        books.update { current -> current.filterNot { it.id == id } }
    }
}
