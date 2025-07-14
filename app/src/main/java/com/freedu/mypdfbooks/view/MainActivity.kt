package com.freedu.mypdfbooks.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.freedu.mypdfbooks.R
import com.freedu.mypdfbooks.database.BookDatabase
import com.freedu.mypdfbooks.databinding.ActivityMainBinding
import com.freedu.mypdfbooks.model.Book
import com.freedu.mypdfbooks.view.adapter.BookAdapter
import com.freedu.mypdfbooks.viewmodel.BookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: BookViewModel
    private lateinit var adapter: BookAdapter
    fun showDeleteDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete '${book.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    val dao = BookDatabase.getDatabase(this@MainActivity).bookDao()
                    withContext(Dispatchers.IO) { dao.delete(book) }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BookAdapter(
            onClick = {
                val intent = Intent(this, PdfViewerActivity::class.java)
                intent.putExtra("pdfUri", it.pdfUri)
                startActivity(intent)
            },
            onLongClick = {
                showDeleteDialog(it)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        viewModel.allBooks.observe(this) { books -> adapter.submitList(books) }

        binding.fab.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }



    }
}