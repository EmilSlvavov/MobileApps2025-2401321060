package com.slavov.bookshelf.ui.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slavov.bookshelf.BookShelfApp
import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.domain.BookDraft
import com.slavov.bookshelf.domain.BookField
import com.slavov.bookshelf.domain.BookValidator
import com.slavov.bookshelf.domain.FieldError
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.toBook
import com.slavov.bookshelf.domain.toDraft
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookEditUiState(
    val book: Book? = null,
    val pendingDraft: BookDraft? = null,
    val errors: Map<BookField, FieldError> = emptyMap(),
    val isDone: Boolean = false,
) {
    val isEditing: Boolean
        get() = book != null
}

class BookEditViewModel(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val bookId: Long = savedStateHandle[ARG_BOOK_ID] ?: NEW_BOOK_ID

    private val _uiState = MutableStateFlow(BookEditUiState())
    val uiState: StateFlow<BookEditUiState> = _uiState.asStateFlow()

    init {
        if (bookId != NEW_BOOK_ID) {
            viewModelScope.launch {
                val book = repository.getBook(bookId) ?: return@launch
                _uiState.update { it.copy(book = book, pendingDraft = book.toDraft()) }
            }
        }
    }

    fun onDraftShown() {
        _uiState.update { it.copy(pendingDraft = null) }
    }

    fun save(draft: BookDraft) {
        val errors = BookValidator.validate(draft)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }
        viewModelScope.launch {
            if (bookId == NEW_BOOK_ID) {
                repository.addBook(draft.toBook())
            } else {
                repository.updateBook(draft.toBook(bookId))
            }
            _uiState.update { it.copy(errors = emptyMap(), isDone = true) }
        }
    }

    fun delete() {
        if (bookId == NEW_BOOK_ID) return
        viewModelScope.launch {
            repository.deleteBook(bookId)
            _uiState.update { it.copy(isDone = true) }
        }
    }

    companion object {
        const val ARG_BOOK_ID = "bookId"
        const val NEW_BOOK_ID = -1L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookShelfApp
                BookEditViewModel(app.container.bookRepository, createSavedStateHandle())
            }
        }
    }
}
