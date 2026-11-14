package com.slavov.bookshelf.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slavov.bookshelf.BookShelfApp
import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.domain.model.Book
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true,
)

class BookListViewModel(private val repository: BookRepository) : ViewModel() {
    val uiState: StateFlow<BookListUiState> =
        repository.observeBooks()
            .map { books -> BookListUiState(books = books, isLoading = false) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), BookListUiState())

    fun delete(book: Book) {
        viewModelScope.launch { repository.deleteBook(book.id) }
    }

    fun restore(book: Book) {
        viewModelScope.launch { repository.addBook(book) }
    }

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookShelfApp
                BookListViewModel(app.container.bookRepository)
            }
        }
    }
}
