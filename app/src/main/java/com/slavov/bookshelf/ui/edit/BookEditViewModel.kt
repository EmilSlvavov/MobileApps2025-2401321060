package com.slavov.bookshelf.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slavov.bookshelf.BookShelfApp
import com.slavov.bookshelf.data.BookRepository
import com.slavov.bookshelf.domain.BookDraft
import com.slavov.bookshelf.domain.BookField
import com.slavov.bookshelf.domain.BookValidator
import com.slavov.bookshelf.domain.FieldError
import com.slavov.bookshelf.domain.toBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookEditUiState(
    val errors: Map<BookField, FieldError> = emptyMap(),
    val isDone: Boolean = false,
)

class BookEditViewModel(private val repository: BookRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(BookEditUiState())
    val uiState: StateFlow<BookEditUiState> = _uiState.asStateFlow()

    fun save(draft: BookDraft) {
        val errors = BookValidator.validate(draft)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }
        viewModelScope.launch {
            repository.addBook(draft.toBook())
            _uiState.update { it.copy(errors = emptyMap(), isDone = true) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BookShelfApp
                BookEditViewModel(app.container.bookRepository)
            }
        }
    }
}
