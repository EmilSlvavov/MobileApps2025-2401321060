package com.slavov.bookshelf.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.FragmentBookEditBinding

class BookEditFragment : Fragment(R.layout.fragment_book_edit) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookEditBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }
}
