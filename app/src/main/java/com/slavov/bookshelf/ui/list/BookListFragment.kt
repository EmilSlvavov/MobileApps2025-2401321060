package com.slavov.bookshelf.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.FragmentBookListBinding

class BookListFragment : Fragment(R.layout.fragment_book_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookListBinding.bind(view)
        binding.addBookButton.setOnClickListener {
            findNavController().navigate(R.id.action_bookList_to_bookEdit)
        }
    }
}
