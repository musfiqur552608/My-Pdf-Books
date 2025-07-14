package com.freedu.mypdfbooks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.freedu.mypdfbooks.model.Book

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book:Book)

    @Delete
    suspend fun delete(book: Book)

    @Update
    suspend fun update(book: Book)

    @Query("SELECT * FROM books")
    fun  getAllBooks():LiveData<List<Book>>

    @Query("UPDATE books SET lastPageRead = :page WHERE id = :bookId")
    suspend fun updateLastReadPage(bookId: Int, page: Int)


    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%' OR version LIKE '%' || :query || '%' OR year LIKE '%' || :query || '%'")
    fun searchBooks(query: String): LiveData<List<Book>>
}