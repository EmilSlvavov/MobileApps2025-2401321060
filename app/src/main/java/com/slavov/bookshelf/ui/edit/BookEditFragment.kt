package com.slavov.bookshelf.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.FragmentBookEditBinding
import com.slavov.bookshelf.domain.BookDraft
import com.slavov.bookshelf.domain.BookField
import com.slavov.bookshelf.domain.FieldError
import com.slavov.bookshelf.domain.model.ReadingStatus
import com.slavov.bookshelf.ui.common.messageRes
import com.slavov.bookshelf.ui.common.shareBook
import kotlinx.coroutines.launch

class BookEditFragment : Fragment(R.layout.fragment_book_edit) {
    private val viewModel: BookEditViewModel by viewModels { BookEditViewModel.Factory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBookEditBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.toolbar.inflateMenu(R.menu.menu_book_edit)
        binding.toolbar.setOnMenuItemClickListener { item -> onMenuItemClick(item.itemId) }
        binding.saveButton.setOnClickListener { viewModel.save(readDraft(binding)) }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(binding, state) }
            }
        }
    }

    private fun render(binding: FragmentBookEditBinding, state: BookEditUiState) {
        if (state.isDone) {
            findNavController().navigateUp()
            return
        }
        state.pendingDraft?.let { draft ->
            showDraft(binding, draft)
            viewModel.onDraftShown()
        }
        binding.toolbar.setTitle(if (state.isEditing) R.string.edit_book else R.string.add_book)
        binding.toolbar.menu.setGroupVisible(R.id.group_existing_book, state.isEditing)
        binding.titleLayout.error = errorText(state.errors[BookField.TITLE])
        binding.authorLayout.error = errorText(state.errors[BookField.AUTHOR])
        binding.totalPagesLayout.error = errorText(state.errors[BookField.TOTAL_PAGES])
        binding.pagesReadLayout.error = errorText(state.errors[BookField.PAGES_READ])
    }

    private fun readDraft(binding: FragmentBookEditBinding): BookDraft = BookDraft(
        title = binding.titleInput.text?.toString().orEmpty(),
        author = binding.authorInput.text?.toString().orEmpty(),
        totalPages = binding.totalPagesInput.text?.toString().orEmpty(),
        pagesRead = binding.pagesReadInput.text?.toString().orEmpty(),
        status = when (binding.statusGroup.checkedRadioButtonId) {
            R.id.statusReading -> ReadingStatus.READING
            R.id.statusFinished -> ReadingStatus.FINISHED
            else -> ReadingStatus.TO_READ
        },
    )

    private fun showDraft(binding: FragmentBookEditBinding, draft: BookDraft) {
        binding.titleInput.setText(draft.title)
        binding.authorInput.setText(draft.author)
        binding.totalPagesInput.setText(draft.totalPages)
        binding.pagesReadInput.setText(draft.pagesRead)
        binding.statusGroup.check(
            when (draft.status) {
                ReadingStatus.TO_READ -> R.id.statusToRead
                ReadingStatus.READING -> R.id.statusReading
                ReadingStatus.FINISHED -> R.id.statusFinished
            },
        )
    }

    private fun errorText(error: FieldError?): String? = error?.let { getString(it.messageRes) }

    private fun onMenuItemClick(itemId: Int): Boolean = when (itemId) {
        R.id.action_share -> {
            viewModel.uiState.value.book?.let { requireContext().shareBook(it) }
            true
        }

        R.id.action_delete -> {
            confirmDelete()
            true
        }

        else -> false
    }

    private fun confirmDelete() {
        val title = viewModel.uiState.value.book?.title.orEmpty()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_book_title)
            .setMessage(getString(R.string.delete_book_message, title))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.delete) { _, _ -> viewModel.delete() }
            .show()
    }
}
