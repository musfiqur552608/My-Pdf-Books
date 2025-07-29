package com.freedu.mypdfbooks.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.freedu.mypdfbooks.R
import com.freedu.mypdfbooks.database.BookDatabase
import com.freedu.mypdfbooks.databinding.FragmentFavoriteBooksBinding
import com.freedu.mypdfbooks.model.Book
import com.freedu.mypdfbooks.view.adapter.BookAdapter
import com.freedu.mypdfbooks.viewmodel.BookViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteBooksFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBooksBinding
    private lateinit var viewModel: BookViewModel
    private lateinit var favadapter: BookAdapter
    fun showDeleteDialog(book: Book) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Book")
            .setMessage("Are you sure you want to delete '${book.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    val dao = BookDatabase.getDatabase(requireContext()).bookDao()
                    withContext(Dispatchers.IO) { dao.delete(book) }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun toggleFavorite(book: Book) {
        val updatedBook = book.copy(isFavorite = !book.isFavorite)
        viewModel.update(updatedBook)
    }

    private fun toggleImportant(book: Book) {
        val updatedBook = book.copy(isImportant = !book.isImportant)
        viewModel.update(updatedBook)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBooksBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.customToolbar)

        val titleTextView: TextView = binding.customToolbar.findViewById(R.id.action_bar_title)
        titleTextView.text = "Favorite Books"

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        favadapter = BookAdapter(
            onClick = {
                val intent = Intent(requireContext(), PdfViewerActivity::class.java)
                intent.putExtra("pdfUri", it.pdfUri)
                intent.putExtra("bookId", it.id)
                intent.putExtra("bookTitle", it.title)
                intent.putExtra("lastPage", it.lastPageRead)
                startActivity(intent)
            },
            onLongClick = {
                val options = arrayOf("Edit", "Delete", "Favorite", "Important")
                AlertDialog.Builder(requireContext())
                    .setTitle(it.title)
                    .setItems(options) { _, which ->
                        when (which) {
                            // Edit
                            0 -> {
                                val intent = Intent(requireContext(), AddBookActivity::class.java)
                                intent.putExtra("bookToEdit", it)
                                startActivity(intent)
                            }
                            1-> {
                                showDeleteDialog(it)
                            }
                            2 ->{
                                toggleFavorite(it)
                            }
                            3 ->{
                                toggleImportant(it)
                            }
                        }
                    }
                    .show()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = favadapter


        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        viewModel.favoriteBooks.observe(viewLifecycleOwner) { books -> favadapter.submitList(books.toList()) }



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchBooks(it).observe(viewLifecycleOwner) {
                        favadapter.submitList(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchBooks(it).observe(viewLifecycleOwner) {
                        favadapter.submitList(it)
                    }
                }
                return true
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Exit App")
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            requireActivity().finishAffinity()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        )
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        viewModel.favoriteBooks.observe(viewLifecycleOwner) { books -> favadapter.submitList(books.toList()) }
    }

}