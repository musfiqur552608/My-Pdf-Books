package com.freedu.mypdfbooks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.freedu.mypdfbooks.model.Book

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase:RoomDatabase() {
    abstract fun bookDao():BookDao

    companion object{
        @Volatile
        private var INSTANCE: BookDatabase?=null

        fun getDatabase(context:Context):BookDatabase{
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(context.applicationContext,
                    BookDatabase::class.java, "book_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}