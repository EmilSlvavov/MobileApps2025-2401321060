package com.slavov.bookshelf.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slavov.bookshelf.BookShelfApp
import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookListUiState(
    val books: List<Book> = emptyList(),
    val filter: ReadingStatus? = null,
    val isLoading: Boolean = true,
)

class BookListViewModel(private val repository: BookRepository) : ViewModel() {
    private val filter = MutableStateFlow<ReadingStatus?>(null)

    val uiState: StateFlow<BookListUiState> =
        combine(repository.observeBooks(), filter) { books, selected ->
            BookListUiState(
                books = books.filter { selected == null || it.status == selected },
                filter = selected,
                isLoading = false,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS), BookListUiState())

    fun setFilter(status: ReadingStatus?) {
        filter.value = status
    }

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
