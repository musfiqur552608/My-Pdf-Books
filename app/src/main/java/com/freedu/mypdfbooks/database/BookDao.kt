package com.freedu.mypdfbooks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.freedu.mypdfbooks.model.Book

@Dao
interface BookDao {
    @Insert
    suspend fun insert(book:Book)

    @Query("SELECT * FROM books")
    fun  getAllBooks():LiveData<List<Book>>
}