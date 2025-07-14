package com.freedu.mypdfbooks.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
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
import java.io.File
import java.io.FileOutputStream

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var viewModel: BookViewModel
    private lateinit var pdfUri: Uri
    private lateinit var imageUri: Uri
    private var isEditMode = false
    private var currentBookId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookViewModel::class.java]

        binding.ivPickImage.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.btnPickPdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            pdfPickerLauncher.launch(intent)
        }

        val receivedBook = intent.getParcelableExtra<Book>("bookToEdit")
        if (receivedBook != null) {
            isEditMode = true
            currentBookId = receivedBook.id

            // Pre-fill the form
            binding.etTitle.setText(receivedBook.title)
            binding.etAuthor.setText(receivedBook.author)
            binding.etVersion.setText(receivedBook.version)
            binding.etYear.setText(receivedBook.year)
            imageUri = Uri.parse(receivedBook.imageUri)
            pdfUri = Uri.parse(receivedBook.pdfUri)

            binding.ivPickImage.setImageURI(imageUri)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val author = binding.etAuthor.text.toString()
            val version = binding.etVersion.text.toString()
            val year = binding.etYear.text.toString()

            val book = Book(id = currentBookId ?: 0, title, author, version, year, imageUri.toString(), pdfUri.toString())
            if (isEditMode) {
                viewModel.update(book)
                Toast.makeText(this, "Book updated!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insert(book)
                Toast.makeText(this, "Book saved!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            val savedUri = copyUriToInternalStorage(uri, "cover_${System.currentTimeMillis()}.jpg")
            imageUri = savedUri
            findViewById<ImageView>(R.id.ivPickImage).setImageURI(savedUri)
        }
    }

    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            try {
                contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            pdfUri = uri
            Toast.makeText(this, "PDF Selected", Toast.LENGTH_SHORT).show()
        }
    }
    private fun copyUriToInternalStorage(uri: Uri, fileName: String): Uri {
        val inputStream = contentResolver.openInputStream(uri) ?: return uri
        val file = File(filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return Uri.fromFile(file)
    }

}