package com.freedu.mypdfbooks.view

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.freedu.mypdfbooks.R
import com.freedu.mypdfbooks.databinding.ActivityAddBookBinding
import com.freedu.mypdfbooks.model.Book
import com.freedu.mypdfbooks.viewmodel.BookViewModel

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var viewModel: BookViewModel
    private lateinit var pdfUri: Uri
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        binding.ivPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.btnPickPdf.setOnClickListener {
            pickPdf.launch("application/pdf")
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val version = binding.etVersion.text.toString()
            val year = binding.etYear.text.toString()

            val book = Book(0, title, author, version, year, imageUri.toString(), pdfUri.toString())
            viewModel.insert(book)
            Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            imageUri = it
            binding.ivPickImage.setImageURI(it)
        }
    }

    private val pickPdf = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            pdfUri = it
        }
    }
}