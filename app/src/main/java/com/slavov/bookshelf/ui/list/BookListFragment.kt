package com.slavov.bookshelf.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.FragmentBookListBinding
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.domain.model.ReadingStatus
import com.slavov.bookshelf.ui.edit.BookEditViewModel
import kotlinx.coroutines.launch

class BookListFragment : Fragment(R.layout.fragment_book_list) {
    private val viewModel: BookListViewModel by viewModels { BookListViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookListBinding.bind(view)
        val adapter = BookAdapter(
            onBookClick = ::openBook,
        )
        binding.bookList.adapter = adapter
        attachSwipeToDelete(binding, adapter)
        setUpFilterChips(binding)
        binding.addBookButton.setOnClickListener {
            findNavController().navigate(R.id.action_bookList_to_bookEdit)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.books)
                    binding.emptyText.isVisible = !state.isLoading && state.books.isEmpty()
                }
            }
        }
    }

    private fun openBook(book: Book) {
        findNavController().navigate(
            R.id.action_bookList_to_bookEdit,
            Bundle().apply { putLong(BookEditViewModel.ARG_BOOK_ID, book.id) },
        )
    }

    private fun setUpFilterChips(binding: FragmentBookListBinding) {
        binding.filterChips.setOnCheckedStateChangeListener { _, checkedIds ->
            val status = when (checkedIds.firstOrNull()) {
                R.id.chipToRead -> ReadingStatus.TO_READ
                R.id.chipReading -> ReadingStatus.READING
                R.id.chipFinished -> ReadingStatus.FINISHED
                else -> null
            }
            viewModel.setFilter(status)
        }
    }

    private fun attachSwipeToDelete(binding: FragmentBookListBinding, adapter: BookAdapter) {
        val swipeDirections = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val callback = object : ItemTouchHelper.SimpleCallback(0, swipeDirections) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val book = adapter.bookAt(viewHolder.bindingAdapterPosition)
                viewModel.delete(book)
                Snackbar.make(binding.root, getString(R.string.book_deleted, book.title), Snackbar.LENGTH_LONG)
                    .setAnchorView(binding.addBookButton)
                    .setAction(R.string.undo) { viewModel.restore(book) }
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(binding.bookList)
    }
}
