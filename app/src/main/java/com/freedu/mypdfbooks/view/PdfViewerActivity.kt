package com.freedu.mypdfbooks.view

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.freedu.mypdfbooks.R
import com.freedu.mypdfbooks.view.adapter.PdfPagerAdapter
import com.freedu.mypdfbooks.viewmodel.BookViewModel

class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var viewPager: ViewPager2
    private var fileDescriptor: ParcelFileDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        viewPager = findViewById(R.id.pdfViewPager)
        val uriString = intent.getStringExtra("pdfUri") ?: return
        val uri = Uri.parse(uriString)
        val bookId = intent.getIntExtra("bookId", -1)
        val lastPage = intent.getIntExtra("lastPage", 0)


        contentResolver.openFileDescriptor(uri, "r")?.let { descriptor ->
            fileDescriptor = descriptor
            pdfRenderer = PdfRenderer(descriptor)
            viewPager.adapter = PdfPagerAdapter(pdfRenderer)
            viewPager.setCurrentItem(lastPage, false)

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (bookId != -1) {
                        val viewModel = ViewModelProvider(this@PdfViewerActivity)[BookViewModel::class.java]
                        viewModel.updateLastPage(bookId, position)
                    }
                }
            })

        } ?: run {
            Toast.makeText(this, "Unable to open PDF", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer.close()
        fileDescriptor?.close()
    }
}