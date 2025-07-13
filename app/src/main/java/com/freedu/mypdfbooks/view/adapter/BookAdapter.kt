package com.freedu.mypdfbooks.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freedu.mypdfbooks.databinding.ItemBookBinding
import com.freedu.mypdfbooks.model.Book
import androidx.core.net.toUri

class BookAdapter(private val onClick: (Book) -> Unit) :
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
        }

        holder.itemView.setOnClickListener {
            onClick(book)
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


