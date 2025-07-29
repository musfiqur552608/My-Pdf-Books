package com.freedu.mypdfbooks.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freedu.mypdfbooks.databinding.ItemBookBinding
import com.freedu.mypdfbooks.model.Book

class BookAdapter(
    private val onClick: (Book) -> Unit,
    private val onLongClick: (Book) -> Unit
) :
    ListAdapter<Book, BookAdapter.BookViewHolder>(DiffCallBack()) {
    class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.binding.apply {
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvVersion.text = book.version
            tvYear.text = book.year
            ivBook.setImageURI(book.imageUri.toUri())
            favoriteIcon.visibility = if (book.isFavorite) ViewGroup.VISIBLE else ViewGroup.GONE
            importantIcon.visibility = if (book.isImportant) ViewGroup.VISIBLE else ViewGroup.GONE

            favoriteIcon.setOnClickListener {
                val updatedBook = book.copy(isFavorite = !book.isFavorite)
                onClick(updatedBook)
            }
            importantIcon.setOnClickListener {
                val updatedBook = book.copy(isImportant = !book.isImportant)
                onClick(updatedBook)
            }
        }


        holder.itemView.setOnClickListener { onClick(book) }
        holder.itemView.setOnLongClickListener {
            onLongClick(book)
            true
        }

    }

    class DiffCallBack : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }

    }


}


