package com.slavov.bookshelf.ui.list

import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import com.slavov.bookshelf.testing.FakeBookRepository
import com.slavov.bookshelf.testing.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BookListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val dune = Book(id = 1, title = "Dune", author = "Frank Herbert", totalPages = 612)
    private val emma = Book(
        id = 2,
        title = "Emma",
        author = "Jane Austen",
        totalPages = 400,
        pagesRead = 120,
        status = ReadingStatus.READING,
    )
    private val repository = FakeBookRepository(listOf(dune, emma))
    private val viewModel by lazy { BookListViewModel(repository) }

    private fun TestScope.collectState() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
    }

    @Test
    fun `initial state is loading`() {
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `all books are shown when no filter is selected`() = runTest {
        collectState()

        assertEquals(listOf(dune, emma), viewModel.uiState.value.books)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `filter keeps only books with the selected status`() = runTest {
        collectState()

        viewModel.setFilter(ReadingStatus.READING)

        assertEquals(listOf(emma), viewModel.uiState.value.books)
        assertEquals(ReadingStatus.READING, viewModel.uiState.value.filter)
    }

    @Test
    fun `clearing the filter shows every book again`() = runTest {
        collectState()
        viewModel.setFilter(ReadingStatus.FINISHED)

        viewModel.setFilter(null)

        assertEquals(2, viewModel.uiState.value.books.size)
    }

    @Test
    fun `delete removes the book from the list`() = runTest {
        collectState()

        viewModel.delete(dune)

        assertEquals(listOf(emma), viewModel.uiState.value.books)
    }

    @Test
    fun `restore brings a deleted book back with the same id`() = runTest {
        collectState()
        viewModel.delete(dune)

        viewModel.restore(dune)

        assertTrue(dune in repository.currentBooks)
    }
}
