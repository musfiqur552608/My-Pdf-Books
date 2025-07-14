package com.freedu.mypdfbooks.view.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freedu.mypdfbooks.R
import com.github.chrisbanes.photoview.PhotoView

class PdfPagerAdapter(
    private val pdfRenderer: PdfRenderer
) : RecyclerView.Adapter<PdfPagerAdapter.PageViewHolder>() {

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photoView: PhotoView = view.findViewById(R.id.photoView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf_page, parent, false)
        return PageViewHolder(view)
    }

    override fun getItemCount(): Int = pdfRenderer.pageCount

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        pdfRenderer.openPage(position).use { page ->
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            holder.photoView.setImageBitmap(bitmap)
        }
    }
}