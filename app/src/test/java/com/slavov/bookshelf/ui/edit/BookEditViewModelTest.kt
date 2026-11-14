package com.slavov.bookshelf.ui.edit

import androidx.lifecycle.SavedStateHandle
import com.slavov.bookshelf.domain.BookDraft
import com.slavov.bookshelf.domain.BookField
import com.slavov.bookshelf.domain.FieldError
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import com.slavov.bookshelf.testing.FakeBookRepository
import com.slavov.bookshelf.testing.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BookEditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val existing = Book(
        id = 5,
        title = "Ubik",
        author = "Philip K. Dick",
        totalPages = 202,
        pagesRead = 50,
        status = ReadingStatus.READING,
    )
    private val repository = FakeBookRepository(listOf(existing))
    private val validDraft = BookDraft(title = "Dune", author = "Frank Herbert", totalPages = "612")

    private fun newBookViewModel() = BookEditViewModel(repository, SavedStateHandle())

    private fun existingBookViewModel() =
        BookEditViewModel(repository, SavedStateHandle(mapOf(BookEditViewModel.ARG_BOOK_ID to existing.id)))

    @Test
    fun `new book screen starts empty`() {
        val state = newBookViewModel().uiState.value

        assertFalse(state.isEditing)
        assertNull(state.pendingDraft)
    }

    @Test
    fun `existing book is loaded into a draft`() {
        val state = existingBookViewModel().uiState.value

        assertTrue(state.isEditing)
        assertEquals("Ubik", state.pendingDraft?.title)
    }

    @Test
    fun `draft is cleared after it has been shown`() {
        val viewModel = existingBookViewModel()

        viewModel.onDraftShown()

        assertNull(viewModel.uiState.value.pendingDraft)
    }

    @Test
    fun `invalid draft reports errors and saves nothing`() {
        val viewModel = newBookViewModel()

        viewModel.save(validDraft.copy(title = ""))

        assertEquals(FieldError.REQUIRED, viewModel.uiState.value.errors[BookField.TITLE])
        assertFalse(viewModel.uiState.value.isDone)
        assertEquals(1, repository.currentBooks.size)
    }

    @Test
    fun `valid draft adds a new book`() {
        val viewModel = newBookViewModel()

        viewModel.save(validDraft)

        assertTrue(viewModel.uiState.value.isDone)
        assertTrue(repository.currentBooks.any { it.title == "Dune" })
    }

    @Test
    fun `saving an existing book updates it in place`() {
        val viewModel = existingBookViewModel()

        viewModel.save(BookDraft("Ubik", "Philip K. Dick", "202", "", ReadingStatus.FINISHED))

        val saved = repository.currentBooks.single()
        assertEquals(existing.id, saved.id)
        assertEquals(ReadingStatus.FINISHED, saved.status)
        assertEquals(202, saved.pagesRead)
    }

    @Test
    fun `delete removes the existing book`() {
        val viewModel = existingBookViewModel()

        viewModel.delete()

        assertTrue(repository.currentBooks.isEmpty())
        assertTrue(viewModel.uiState.value.isDone)
    }

    @Test
    fun `delete does nothing for a new book`() {
        val viewModel = newBookViewModel()

        viewModel.delete()

        assertFalse(viewModel.uiState.value.isDone)
        assertEquals(1, repository.currentBooks.size)
    }
}
