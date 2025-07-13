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
import com.freedu.mypdfbooks.R
class PdfViewerActivity : AppCompatActivity() {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var currentPage: PdfRenderer.Page
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    private lateinit var imageView: ImageView
    private lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        imageView = findViewById(R.id.pdfImageView)
        seekBar = findViewById(R.id.pageSeekBar)

        val uriString = intent.getStringExtra("pdfUri")
        val pdfUri = uriString?.let { Uri.parse(it) }

        if (pdfUri != null) {
            contentResolver.openFileDescriptor(pdfUri, "r")?.use { descriptor ->
                parcelFileDescriptor = descriptor
                pdfRenderer = PdfRenderer(descriptor)
                seekBar.max = pdfRenderer.pageCount - 1
                showPage(0)

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        showPage(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
        } else {
            Toast.makeText(this, "Invalid PDF Uri", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showPage(index: Int) {
        if (::currentPage.isInitialized) {
            currentPage.close()
        }

        currentPage = pdfRenderer.openPage(index)
        val bitmap = Bitmap.createBitmap(currentPage.width, currentPage.height, Bitmap.Config.ARGB_8888)
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentPage.close()
        pdfRenderer.close()
        parcelFileDescriptor?.close()
    }
}