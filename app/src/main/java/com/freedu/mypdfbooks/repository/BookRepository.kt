package com.freedu.mypdfbooks.repository

import androidx.lifecycle.LiveData
import com.freedu.mypdfbooks.database.BookDao
import com.freedu.mypdfbooks.model.Book

class BookRepository(private val dao:BookDao) {
    val allBooks: LiveData<List<Book>> = dao.getAllBooks()
    suspend fun insert(book: Book){
        dao.insert(book)
    }

    suspend fun delete(book: Book) {
        dao.delete(book)
    }

    suspend fun updateLastPage(bookId: Int, page: Int) {
        dao.updateLastReadPage(bookId, page)
    }
}