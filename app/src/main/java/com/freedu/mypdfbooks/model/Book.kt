package com.freedu.mypdfbooks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val title:String,
    val author:String,
    val version:String,
    val year:String,
    val imageUri:String,
    val pdfUri:String
)
