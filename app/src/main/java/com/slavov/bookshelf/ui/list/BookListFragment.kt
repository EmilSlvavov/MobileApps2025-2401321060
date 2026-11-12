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
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.FragmentBookListBinding
import kotlinx.coroutines.launch

class BookListFragment : Fragment(R.layout.fragment_book_list) {
    private val viewModel: BookListViewModel by viewModels { BookListViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookListBinding.bind(view)
        val adapter = BookAdapter()
        binding.bookList.adapter = adapter
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
}
