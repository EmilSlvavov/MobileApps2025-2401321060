package com.slavov.bookshelf.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slavov.bookshelf.R
import com.slavov.bookshelf.databinding.ItemBookBinding
import com.slavov.bookshelf.domain.model.Book
import com.slavov.bookshelf.ui.common.labelRes

class BookAdapter(
    private val onBookClick: (Book) -> Unit,
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        val binding = holder.binding
        val context = binding.root.context
        binding.titleText.text = book.title
        binding.authorText.text = book.author
        binding.statusText.setText(book.status.labelRes)
        binding.progressText.text = context.getString(R.string.pages_progress, book.pagesRead, book.totalPages)
        binding.progressBar.progress = book.progressPercent
        binding.root.setOnClickListener { onBookClick(book) }
    }

    fun bookAt(position: Int): Book = getItem(position)

    class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)

    private object BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean = oldItem == newItem
    }
}
